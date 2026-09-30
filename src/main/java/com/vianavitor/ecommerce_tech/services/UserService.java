package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.dtos.request.UserLoginFormsDTO;
import com.vianavitor.ecommerce_tech.dtos.request.UserRegisterFormsDTO;
import com.vianavitor.ecommerce_tech.exceptions.DeactivatedUserException;
import com.vianavitor.ecommerce_tech.exceptions.DuplicateUserException;
import com.vianavitor.ecommerce_tech.exceptions.InvalidEmailOrPasswordException;
import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.Role;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.models.aux.auth.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.models.aux.enums.UserRole;
import com.vianavitor.ecommerce_tech.repositories.UserRepository;
import com.vianavitor.ecommerce_tech.services.auth.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(UserLoginFormsDTO forms) throws InvalidEmailOrPasswordException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(forms.email(), forms.password());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails == null) {
                throw new NullPointerException("Error when trying to retrieve userDetails");
            }

            return jwtTokenService.generateToken(userDetails);

        } catch (UsernameNotFoundException | BadCredentialsException | DisabledException e) {
            throw new InvalidEmailOrPasswordException("Invalid e-mail or password", e);
        }
    }

    public void createNew(UserRegisterFormsDTO forms) throws NotFoundResourceException, DuplicateUserException {
        boolean isEmailAlreadyInUse = repository.findByEmail(forms.email()).isPresent();

        if (isEmailAlreadyInUse) {
            throw new DuplicateUserException("This e-mail is already in use, please enter another one");
        }

        String hashedPassword = passwordEncoder.encode(forms.password());
        List<Role> roles = List.of(new Role(null, UserRole.ROLE_CUSTOMER));

        User user = new User(null, forms.name(), forms.email(), hashedPassword, roles,true);

        repository.save(user);
    }

    public User getById(Integer id) throws NotFoundResourceException {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(
                        "User not found"
                ));
    }
    
    public User findByEmail(String email) throws NotFoundResourceException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundResourceException(
                        "No user found matching the provided e-mail"
                ));
    }

    public List<User> findByEmailOrName(String email, String name) {
        return repository.findByEmailOrNameContaining(email, name);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User modify(Integer id, String email, String name) throws NotFoundResourceException, DeactivatedUserException {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(
                        "User not found"
                ));

        if (!user.getActive()) {
            throw new DeactivatedUserException("User deactivated, cannot be modified");
        }

        name = Optional.ofNullable(name).orElse(user.getName());
        email = Optional.ofNullable(email).orElse(user.getEmail());

        user.setName(name);
        user.setEmail(email);

        return repository.save(user);
    }

    public void changePassword(Integer id, String password) throws NotFoundResourceException {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(
                        "User not found"
                ));

        if (!user.getActive()) {
            throw new DeactivatedUserException("User deactivated, cannot be modified");
        }

        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        repository.save(user);
    }

    public void changeActiveStatus(Integer id, Boolean status) throws NotFoundResourceException {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(
                        "No user found having this ID"
                ));

        user.setActive(status);
        repository.save(user);
    }
}
