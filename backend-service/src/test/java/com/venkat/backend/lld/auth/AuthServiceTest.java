package com.venkat.backend.lld.auth;

import com.venkat.backend.lld.auth.AuthModels.*;
import com.venkat.backend.lld.auth.RbacService.Action;
import com.venkat.backend.lld.auth.RbacService.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private static final String SECRET = "super-secret-jwt-key-at-least-32-chars!!";

    private TokenService tokenService;
    private RefreshTokenStore refreshStore;
    private RbacService rbac;
    private AuthFlowSimulator flow;

    private final AuthUser alice   = AuthFlowSimulator.alice();
    private final AuthUser bob     = AuthFlowSimulator.bob();
    private final AuthUser charlie = AuthFlowSimulator.charlie();

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(SECRET);
        refreshStore = new RefreshTokenStore(tokenService);
        rbac  = new RbacService();
        flow  = new AuthFlowSimulator(tokenService, refreshStore);
    }

    // ── JWT round-trip ────────────────────────────────────────────────

    @Test
    void accessToken_roundTrip_claimsMatch() {
        String token = tokenService.generateAccessToken(alice);
        Claims claims = tokenService.validateToken(token);

        assertEquals(alice.id(), claims.userId());
        assertEquals(alice.email(), claims.email());
        assertEquals(Role.ADMIN, claims.role());
        assertFalse(claims.isExpired());
    }

    @Test
    void expiredToken_throwsTokenExpiredException() {
        String token = tokenService.generateAccessToken(alice, -1); // already expired
        assertThrows(TokenExpiredException.class, () -> tokenService.validateToken(token));
    }

    @Test
    void tamperedToken_throwsInvalidTokenException() {
        String token = tokenService.generateAccessToken(alice);
        String tampered = token.substring(0, token.lastIndexOf('.') + 1) + "badsig";
        assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(tampered));
    }

    // ── Refresh token rotation ────────────────────────────────────────

    @Test
    void refreshToken_rotation_issuesNewPairAndInvalidatesOld() {
        String refreshToken = refreshStore.issue(alice);
        assertTrue(refreshStore.isValid(refreshToken));

        TokenPair newPair = refreshStore.rotate(refreshToken, alice);
        assertNotNull(newPair.accessToken());
        assertNotNull(newPair.refreshToken());

        // Old token must be consumed
        assertFalse(refreshStore.isValid(refreshToken));
        // New refresh token must be valid
        assertTrue(refreshStore.isValid(newPair.refreshToken()));
    }

    @Test
    void reusedRefreshToken_throwsInvalidTokenException() {
        String refreshToken = refreshStore.issue(alice);
        refreshStore.rotate(refreshToken, alice); // first use OK
        // Second use: token was already consumed
        assertThrows(InvalidTokenException.class, () -> refreshStore.rotate(refreshToken, alice));
    }

    // ── RBAC ─────────────────────────────────────────────────────────

    @Test
    void admin_hasAllPermissions() {
        assertTrue(rbac.hasPermission(alice, Resource.POSTS, Action.DELETE));
        assertTrue(rbac.hasPermission(alice, Resource.USERS, Action.WRITE));
        assertTrue(rbac.hasPermission(alice, Resource.SETTINGS, Action.WRITE));
    }

    @Test
    void editor_canWritePosts_butNotDeleteOrManageUsers() {
        assertTrue(rbac.hasPermission(bob, Resource.POSTS, Action.WRITE));
        assertFalse(rbac.hasPermission(bob, Resource.POSTS, Action.DELETE));
        assertFalse(rbac.hasPermission(bob, Resource.USERS, Action.WRITE));
    }

    @Test
    void viewer_canOnlyReadPosts() {
        assertTrue(rbac.hasPermission(charlie, Resource.POSTS, Action.READ));
        assertFalse(rbac.hasPermission(charlie, Resource.POSTS, Action.WRITE));
        assertFalse(rbac.hasPermission(charlie, Resource.USERS, Action.READ));
    }

    @Test
    void checkAccess_throwsForInsufficientRole() {
        assertThrows(UnauthorizedException.class,
                () -> rbac.checkAccess(charlie, Resource.USERS, Action.READ));
    }

    // ── OAuth2 Authorization Code Flow ───────────────────────────────

    @Test
    void authCodeFlow_exchangeReturnsTokenPair() {
        String code = flow.generateAuthCode(alice);
        TokenPair pair = flow.exchangeCodeForTokens(code);
        assertNotNull(pair.accessToken());
        assertNotNull(pair.refreshToken());

        // Access token must be valid
        Claims claims = tokenService.validateToken(pair.accessToken());
        assertEquals(alice.id(), claims.userId());
    }

    @Test
    void authCode_isSingleUse() {
        String code = flow.generateAuthCode(alice);
        flow.exchangeCodeForTokens(code); // first use OK
        assertThrows(InvalidTokenException.class, () -> flow.exchangeCodeForTokens(code));
    }
}
