package com.vianavitor.ecommerce_tech.auth.services;

import com.vianavitor.ecommerce_tech.auth.entities.User;
import com.vianavitor.ecommerce_tech.auth.security.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username/email provided"));

        return new UserDetailsImpl(user);
    }
}
