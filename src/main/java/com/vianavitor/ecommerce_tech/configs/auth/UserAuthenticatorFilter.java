package com.vianavitor.ecommerce_tech.configs.auth;

import com.vianavitor.ecommerce_tech.configs.SecurityConfiguration;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.models.aux.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.repositories.UserRepository;
import com.vianavitor.ecommerce_tech.services.auth.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticatorFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAuthenticationRequired(request)) {
            String token = recoverToken(request);

            if (token == null) {
                throw new IllegalArgumentException("Token cannot be null");
            }

            String email = jwtTokenService.getSubjectFromToken(token);
            User user = userRepository.findByEmail(email).get();

            UserDetails userDetails = new UserDetailsImpl(user);

            Authentication authentication  = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), null, userDetails.getAuthorities()
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

        return !Arrays.asList(SecurityConfiguration.NO_REQUIRED_AUTHENTICATION_ENDPOINTS).contains(uri);
    }
}
