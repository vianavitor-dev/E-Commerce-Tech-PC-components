package com.vianavitor.ecommerce_tech.configs.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.vianavitor.ecommerce_tech.configs.SecurityConfiguration;
import com.vianavitor.ecommerce_tech.models.aux.auth.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.repositories.UserRepository;
import com.vianavitor.ecommerce_tech.services.auth.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserAuthenticatorFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isAuthenticationRequired(request)) {
            String token = recoverToken(request);

            if (token == null) {
                throw new IllegalArgumentException("Token cannot be null");
            }

            DecodedJWT decodedToken = jwtTokenService.decodeToken(token);
            Integer userId = decodedToken.getClaims().get("userId").asInt();
            String email = decodedToken.getSubject();

            List<? extends GrantedAuthority> roles = decodedToken.getClaim("roles")
                    .asList(String.class)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails principal = new UserDetailsImpl(
                    userId, email, null, roles
            );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");

        if (auth == null) {
            return null;
        }

        return auth.replace("Bearer ", "");
    }

    public boolean isAuthenticationRequired(HttpServletRequest req) {
        String uri = req.getRequestURI();

        if (uri.matches("/api/products/(\\d){1,}$") ||
                uri.matches("/api/users/(\\d){1,}/change-password")) {
            return false;
        }

        return !Arrays.asList(SecurityConfiguration.NO_REQUIRED_AUTHENTICATION_ENDPOINTS).contains(uri);
    }
}
