package com.vianavitor.ecommerce_tech.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vianavitor.ecommerce_tech.models.aux.auth.UserDetailsImpl;
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
                    .withClaim("userId", user.getUserId())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new JWTCreationException("Error when creating JWT token", e);
        }
    }

    public DecodedJWT decodeToken(String token) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);

        } catch (TokenExpiredException e) {
            throw new JWTVerificationException("Expired token", e);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Invalid token", e);
        }

    }
}
