package com.vianavitor.ecommerce_tech.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vianavitor.ecommerce_tech.models.aux.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JwtTokenService {
    private static final String SECRET_KEY = "667a914cbcab7fae82bcec870c04d09e";
    private static final String ISSUER = "vianavitor-app";

    public String generateToken(UserDetailsImpl user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            List<String> roles = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(6, ChronoUnit.HOURS)) // TODO: implement refresh token strategy
                    .withSubject(user.getUsername())
                    .withClaim("roles", roles)
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new JWTCreationException("Error when creating JWT token.", e);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Invalid or expired token.", e);
        }
    }
}
