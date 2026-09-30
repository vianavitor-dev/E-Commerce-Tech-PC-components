package com.vianavitor.ecommerce_tech.models.aux.auth;

import com.vianavitor.ecommerce_tech.models.User;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    @Getter
    private Integer userId;
    private String username;
    private String password;
    private List<? extends GrantedAuthority> roles;
//
    public UserDetailsImpl(User user) {
        this.userId = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());;
    }

    public UserDetailsImpl(Integer userId, String username, String password, List<? extends GrantedAuthority> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

//    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
