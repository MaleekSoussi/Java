package com.example.demo.security;

import org.springframework.security.oauth2.jwt.Jwt;

/**
 * L'utilisateur connecte, lu depuis le token JWT.
 */
public record CurrentUser(Long id, String email, boolean admin) {

    public static CurrentUser from(Jwt jwt) {
        Number id = jwt.getClaim("id");
        String role = jwt.getClaimAsString("role");
        return new CurrentUser(id.longValue(), jwt.getSubject(), "ADMIN".equals(role));
    }
}
