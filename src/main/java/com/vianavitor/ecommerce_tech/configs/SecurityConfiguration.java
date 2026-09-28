package com.vianavitor.ecommerce_tech.configs;

import com.vianavitor.ecommerce_tech.configs.auth.UserAuthenticatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    public final static String[] NO_REQUIRED_AUTHENTICATION_ENDPOINTS = {
            "/api/users",                   // create user
            "/api/users/login",
            "/api/users/{id}/change-password",
            "/api/products",                // search
            "/api/products/{id}",           // get by ID
            "/api/products/all"             // get all
    };

    public final static String[] REQUIRED_CUSTOMER_AUTHENTICATION_ENDPOINTS = {
            "/api/products/rate",                   // rate the product
            "/api/products/compatibility-check",    // check the selected PC components
            "/api/orders",                          // get by ID, get by user, order products, refund, change status
            "api/purchased-products",               // get by order
            "/api/users/{id}"                       // get by ID, modify, deactivate
    };

    public final static String[] REQUIRED_ADMINISTRATOR_AUTHENTICATION_ENDPOINTS = {
            "/api/users/{id}/activate"  // activate account
    };

    UserAuthenticatorFilter userAuthenticatorFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(NO_REQUIRED_AUTHENTICATION_ENDPOINTS).permitAll()
                        .requestMatchers(REQUIRED_CUSTOMER_AUTHENTICATION_ENDPOINTS).hasRole("CUSTOMER")
                        .requestMatchers(REQUIRED_ADMINISTRATOR_AUTHENTICATION_ENDPOINTS).hasRole("ADMINISTRATOR")
                        .anyRequest().denyAll()
                ).addFilterBefore(userAuthenticatorFilter, UserAuthenticatorFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
