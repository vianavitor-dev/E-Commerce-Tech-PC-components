package com.vianavitor.ecommerce_tech.auth.services;

import com.vianavitor.ecommerce_tech.auth.dtos.request.UserLoginFormsDTO;
import com.vianavitor.ecommerce_tech.auth.dtos.request.UserRegisterFormsDTO;
import com.vianavitor.ecommerce_tech.auth.dtos.response.UserProfileDTO;
import com.vianavitor.ecommerce_tech.auth.exceptions.DeactivatedUserException;
import com.vianavitor.ecommerce_tech.auth.exceptions.DuplicateUserException;
import com.vianavitor.ecommerce_tech.auth.exceptions.InvalidEmailOrPasswordException;
import com.vianavitor.ecommerce_tech.shared.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.auth.entities.Role;
import com.vianavitor.ecommerce_tech.auth.entities.User;
import com.vianavitor.ecommerce_tech.auth.security.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.auth.entities.enums.UserRole;
import com.vianavitor.ecommerce_tech.auth.repositories.UserRepository;
import com.vianavitor.ecommerce_tech.auth.services.JwtTokenService;
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

    public UserProfileDTO getProfile(Integer id) throws NotFoundResourceException {
        User user = this.getById(id);

        return new UserProfileDTO(user.getName(), user.getEmail());
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

    public UserProfileDTO modify(Integer id, String email, String name) throws NotFoundResourceException, DeactivatedUserException {
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

        repository.save(user);

        return new UserProfileDTO(name, email);
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
