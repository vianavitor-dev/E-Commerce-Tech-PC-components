package com.vianavitor.ecommerce_tech.auth.repositories;

import com.vianavitor.ecommerce_tech.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByEmailOrNameContaining(String email, String name);
}
