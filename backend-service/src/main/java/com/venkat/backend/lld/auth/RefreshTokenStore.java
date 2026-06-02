package com.venkat.backend.lld.auth;

import com.venkat.backend.lld.auth.AuthModels.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LLD Kata: Refresh Token Store
 *
 * Opaque refresh tokens stored server-side, unlike JWTs which are
 * stateless. Server-side storage enables instant revocation.
 *
 * Rotation pattern (one-time use):
 *   1. Client sends refreshToken to /auth/refresh
 *   2. Server looks up RefreshEntry, validates it's not expired
 *   3. Server invalidates the old token (deletes it)
 *   4. Server issues a new TokenPair (new access + new refresh)
 *   5. Stolen token re-use is detected because the old token no longer exists
 *
 * Production additions:
 *   - Persist to Redis or DB (for multi-instance deployments)
 *   - Index by userId to revoke all sessions on password change
 *   - Limit to N active sessions per user
 */
public class RefreshTokenStore {

    private static final long REFRESH_TTL_MS = 7 * 24 * 60 * 60 * 1000L; // 7 days
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, RefreshEntry> store = new ConcurrentHashMap<>();
    private final TokenService tokenService;

    public RefreshTokenStore(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /** Issue a new refresh token for the given user. Returns the opaque token string. */
    public String issue(AuthUser user) {
        String token = generateOpaqueToken();
        long exp = System.currentTimeMillis() + REFRESH_TTL_MS;
        store.put(token, new RefreshEntry(user.id(), user.email(), user.highestRole(), exp));
        return token;
    }

    /**
     * Rotate: consume the old refresh token, issue a new TokenPair.
     * Implements the "refresh token rotation" security pattern.
     *
     * @throws InvalidTokenException if the token is unknown (possibly stolen re-use)
     * @throws TokenExpiredException if the token is past its TTL
     */
    public TokenPair rotate(String oldRefreshToken, AuthUser user) {
        RefreshEntry entry = store.remove(oldRefreshToken); // consume — single use
        if (entry == null) throw new InvalidTokenException("Unknown or already-used refresh token");
        if (entry.isExpired()) throw new TokenExpiredException();

        String newAccess  = tokenService.generateAccessToken(user);
        String newRefresh = issue(user);
        return new TokenPair(newAccess, newRefresh);
    }

    /** Revoke a specific refresh token (logout from one device). */
    public void revoke(String refreshToken) {
        store.remove(refreshToken);
    }

    /** Revoke all refresh tokens for a user (logout from all devices). */
    public void revokeAll(String userId) {
        store.entrySet().removeIf(e -> userId.equals(e.getValue().userId()));
    }

    public boolean isValid(String token) {
        RefreshEntry entry = store.get(token);
        return entry != null && !entry.isExpired();
    }

    private static String generateOpaqueToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
