package com.venkat.backend.lld.auth;

import com.venkat.backend.lld.auth.AuthModels.*;

/**
 * LLD Kata: Role-Based Access Control (RBAC)
 *
 * Simple hierarchical RBAC:
 *   ADMIN  > EDITOR > VIEWER
 *   (higher ordinal = more permissions)
 *
 * Permission matrix:
 *   Resource   | Action  | Min Role
 *   -----------+---------+---------
 *   posts      | READ    | VIEWER
 *   posts      | WRITE   | EDITOR
 *   posts      | DELETE  | ADMIN
 *   users      | READ    | EDITOR
 *   users      | WRITE   | ADMIN
 *   analytics  | READ    | EDITOR
 *   settings   | *       | ADMIN
 *
 * Production additions:
 *   - Attribute-Based Access Control (ABAC): add resource ownership checks
 *     e.g. EDITOR can DELETE their own posts but not others'
 *   - Permission sets stored in DB, cached in Redis
 *   - Audit log: every checkAccess call logged with user + resource + outcome
 */
public class RbacService {

    public enum Resource { POSTS, USERS, ANALYTICS, SETTINGS }
    public enum Action    { READ, WRITE, DELETE }

    /** Returns true if the user's role meets the required minimum for (resource, action). */
    public boolean hasPermission(AuthUser user, Resource resource, Action action) {
        Role required = minRole(resource, action);
        return user.highestRole().hasAtLeast(required);
    }

    /**
     * Throws UnauthorizedException if the user lacks the required permission.
     * Use as a guard in service methods.
     */
    public void checkAccess(AuthUser user, Resource resource, Action action) {
        if (!hasPermission(user, resource, action)) {
            throw new UnauthorizedException(
                    user.email() + " cannot " + action + " " + resource
                    + " (requires " + minRole(resource, action) + ", has " + user.highestRole() + ")");
        }
    }

    // Permission matrix
    private static Role minRole(Resource resource, Action action) {
        return switch (resource) {
            case POSTS -> switch (action) {
                case READ   -> Role.VIEWER;
                case WRITE  -> Role.EDITOR;
                case DELETE -> Role.ADMIN;
            };
            case USERS -> switch (action) {
                case READ   -> Role.EDITOR;
                case WRITE, DELETE -> Role.ADMIN;
            };
            case ANALYTICS -> switch (action) {
                case READ   -> Role.EDITOR;
                case WRITE, DELETE -> Role.ADMIN;
            };
            case SETTINGS -> Role.ADMIN; // all actions require ADMIN
        };
    }
}
