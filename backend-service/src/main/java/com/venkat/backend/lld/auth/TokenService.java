package com.venkat.backend.lld.auth;

import com.venkat.backend.lld.auth.AuthModels.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * LLD Kata: JWT Token Service
 *
 * Implements HS256 JWT creation and validation from scratch using
 * Java's built-in javax.crypto.Mac — no external JWT library needed.
 *
 * JWT structure:
 *   base64url(header) . base64url(payload) . base64url(signature)
 *
 * Header:  {"alg":"HS256","typ":"JWT"}
 * Payload: {"sub":userId, "email":email, "role":role, "exp":epochMs}
 * Signature: HMAC-SHA256(header + "." + payload, secret)
 *
 * Security notes for production:
 *   - Use a long random secret (>= 256 bits)
 *   - Rotate secrets periodically
 *   - Consider RS256 (asymmetric) for multi-service verification
 *   - Store secret in a secrets manager (not in source code)
 */
public class TokenService {

    private static final String HEADER =
            base64url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
    private static final long ACCESS_TTL_MS  = 15 * 60 * 1000L;   // 15 minutes
    private static final long REFRESH_TTL_MS = 7  * 24 * 60 * 60 * 1000L; // 7 days

    private final byte[] secret;

    public TokenService(String secret) {
        if (secret.length() < 32) throw new IllegalArgumentException("Secret must be at least 32 characters");
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    /** Create a short-lived access token for the given user. */
    public String generateAccessToken(AuthUser user) {
        long exp = System.currentTimeMillis() + ACCESS_TTL_MS;
        return buildToken(user.id(), user.email(), user.highestRole(), exp);
    }

    /** Create a long-lived access token with a custom TTL (useful for tests). */
    public String generateAccessToken(AuthUser user, long ttlMs) {
        long exp = System.currentTimeMillis() + ttlMs;
        return buildToken(user.id(), user.email(), user.highestRole(), exp);
    }

    /**
     * Validate a JWT and return its claims.
     *
     * @throws InvalidTokenException if structure or signature is wrong
     * @throws TokenExpiredException if the token is past its expiry
     */
    public Claims validateToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new InvalidTokenException("Malformed token");

        // Verify signature
        String expectedSig = sign(parts[0] + "." + parts[1]);
        if (!constantTimeEquals(expectedSig, parts[2])) {
            throw new InvalidTokenException("Invalid signature");
        }

        // Decode payload
        String payloadJson = new String(Base64.getUrlDecoder().decode(padBase64(parts[1])), StandardCharsets.UTF_8);
        Claims claims = parseClaims(payloadJson);

        if (claims.isExpired()) throw new TokenExpiredException();
        return claims;
    }

    // ── Private helpers ──────────────────────────────────────────────

    private String buildToken(String userId, String email, Role role, long expiresAt) {
        String payload = base64url(
                ("{\"sub\":\"" + userId + "\","
                + "\"email\":\"" + email + "\","
                + "\"role\":\"" + role.name() + "\","
                + "\"exp\":" + expiresAt + "}").getBytes(StandardCharsets.UTF_8));
        String headerPayload = HEADER + "." + payload;
        return headerPayload + "." + sign(headerPayload);
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return base64url(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Signing failed", e);
        }
    }

    private static Claims parseClaims(String json) {
        // Simple JSON extraction — real code uses Jackson/Gson
        String userId  = extractString(json, "sub");
        String email   = extractString(json, "email");
        String roleStr = extractString(json, "role");
        long   exp     = extractLong(json, "exp");
        return new Claims(userId, email, Role.valueOf(roleStr), exp);
    }

    private static String extractString(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start < 0) throw new InvalidTokenException("Missing claim: " + key);
        start += pattern.length();
        int end = json.indexOf('"', start);
        return json.substring(start, end);
    }

    private static long extractLong(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern) + pattern.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        return Long.parseLong(json.substring(start, end));
    }

    private static String base64url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String padBase64(String s) {
        int pad = s.length() % 4;
        if (pad == 2) return s + "==";
        if (pad == 3) return s + "=";
        return s;
    }

    /** Constant-time comparison to prevent timing attacks. */
    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) result |= a.charAt(i) ^ b.charAt(i);
        return result == 0;
    }
}
