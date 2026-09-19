package com.vianavitor.ecommerce_tech.services;

import com.password4j.Password;
import com.vianavitor.ecommerce_tech.dtos.request.UserRegisterFormsDTO;
import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    // TODO: implement authentication and authorization rules

    public void createNew(UserRegisterFormsDTO forms) throws NotFoundResourceException, DataIntegrityViolationException {
        boolean isEmailAlreadyInUse = repository.findByEmail(forms.email()).isPresent();

        if (isEmailAlreadyInUse) {
            throw new DataIntegrityViolationException("This e-mail is already in use, please enter another one");
        }

        String hashedPassword = Password.hash(forms.password()).withArgon2().getResult();

        User user = new User(null, forms.name(), forms.email(), forms.password());
        user.setPassword(hashedPassword);

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

    public User modify(Integer id, String email, String name) throws NotFoundResourceException {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(
                        "User not found"
                ));

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

        String hashedPassword = Password.hash(password).withArgon2().getResult();
        user.setPassword(hashedPassword);

        repository.save(user);
    }

    // TODO: implement deactivate user instead
//
//    public void delete(Integer id) throws NotFoundResourceException {
//        User user = repository.findById(id)
//                .orElseThrow(() -> new NotFoundResourceException(
//                        "No user found having this ID <bold>"+id+"<bold>"
//                ));
//
//        repository.delete(user);
//    }
}
