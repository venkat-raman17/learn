package com.venkat.backend.lld.auth;

import com.venkat.backend.lld.auth.AuthModels.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LLD Kata: OAuth 2.0 Authorization Code Flow — Simplified Simulation
 *
 * Full flow:
 *   1. User clicks "Login with Google"
 *   2. Browser → Authorization Server: GET /authorize?client_id=...&redirect_uri=...&state=...
 *   3. User logs in and consents
 *   4. Authorization Server → Browser: redirect to /callback?code=AUTH_CODE&state=...
 *   5. Backend → Authorization Server: POST /token { code, client_id, client_secret, redirect_uri }
 *   6. Authorization Server → Backend: { access_token, refresh_token, id_token }
 *   7. Backend issues its own session/JWT for the user
 *
 * This simulator covers steps 2–6 in-process.
 * The authorization code is single-use, short-lived (10 minutes).
 *
 * Production additions:
 *   - PKCE (Proof Key for Code Exchange) for public clients
 *   - State parameter validation (CSRF protection)
 *   - ID token (OpenID Connect) for user info
 *   - Real HTTP calls to the identity provider
 */
public class AuthFlowSimulator {

    private static final long CODE_TTL_MS = 10 * 60 * 1000L; // 10 minutes
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, CodeEntry> pendingCodes = new ConcurrentHashMap<>();
    private final TokenService tokenService;
    private final RefreshTokenStore refreshStore;

    public AuthFlowSimulator(TokenService tokenService, RefreshTokenStore refreshStore) {
        this.tokenService = tokenService;
        this.refreshStore = refreshStore;
    }

    /**
     * Step 2–3: User authenticates; server generates a one-time authorization code.
     * In reality, this happens on the authorization server after the user consents.
     */
    public String generateAuthCode(AuthUser user) {
        String code = generateOpaqueToken();
        pendingCodes.put(code, new CodeEntry(user, System.currentTimeMillis() + CODE_TTL_MS));
        return code;
    }

    /**
     * Step 5–6: Client exchanges the authorization code for tokens.
     * The code is consumed (single-use) to prevent replay attacks.
     *
     * @throws InvalidTokenException if code is unknown or already used
     * @throws TokenExpiredException  if code has expired
     */
    public TokenPair exchangeCodeForTokens(String code) {
        CodeEntry entry = pendingCodes.remove(code); // consume — single use
        if (entry == null) throw new InvalidTokenException("Unknown or already-used authorization code");
        if (System.currentTimeMillis() > entry.expiresAt()) throw new TokenExpiredException();

        AuthUser user = entry.user();
        String accessToken  = tokenService.generateAccessToken(user);
        String refreshToken = refreshStore.issue(user);
        return new TokenPair(accessToken, refreshToken);
    }

    private record CodeEntry(AuthUser user, long expiresAt) {}

    private static String generateOpaqueToken() {
        byte[] bytes = new byte[16]; // 128 bits
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Factory: creates a ready-to-use AuthFlowSimulator with sensible defaults
    public static AuthFlowSimulator create(String jwtSecret) {
        TokenService ts = new TokenService(jwtSecret);
        RefreshTokenStore rts = new RefreshTokenStore(ts);
        return new AuthFlowSimulator(ts, rts);
    }

    // Expose for testing
    public TokenService tokenService() { return tokenService; }
    public RefreshTokenStore refreshStore() { return refreshStore; }

    /** Sample users for practice */
    public static AuthUser alice() {
        return new AuthUser("user-alice", "alice@example.com", Set.of(Role.ADMIN));
    }
    public static AuthUser bob() {
        return new AuthUser("user-bob", "bob@example.com", Set.of(Role.EDITOR));
    }
    public static AuthUser charlie() {
        return new AuthUser("user-charlie", "charlie@example.com", Set.of(Role.VIEWER));
    }
}
