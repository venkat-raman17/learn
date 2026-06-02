package com.venkat.backend.lld.auth;

import java.util.Set;

/**
 * Domain models for the Auth system LLD kata.
 */
public final class AuthModels {
    private AuthModels() {}

    public enum Role {
        VIEWER, EDITOR, ADMIN;

        /** Returns true if this role has at least the given required role's level. */
        public boolean hasAtLeast(Role required) {
            return this.ordinal() >= required.ordinal();
        }
    }

    public record AuthUser(String id, String email, Set<Role> roles) {
        public Role highestRole() {
            return roles.stream()
                    .max(java.util.Comparator.comparingInt(Role::ordinal))
                    .orElse(Role.VIEWER);
        }
    }

    public record TokenPair(String accessToken, String refreshToken) {}

    public record Claims(String userId, String email, Role role, long expiresAt) {
        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    /** Refresh token entry stored server-side. */
    public record RefreshEntry(String userId, String email, Role role, long expiresAt) {
        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException() { super("Token has expired"); }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String msg) { super(msg); }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String msg) { super("Access denied: " + msg); }
    }
}
