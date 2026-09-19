package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.dtos.request.UserModifiableFieldsDTO;
import com.vianavitor.ecommerce_tech.dtos.request.UserRegisterFormsDTO;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterFormsDTO forms) {
        service.createNew(forms);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        var result = service.getById(id);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modify(@PathVariable Integer id, @RequestBody UserModifiableFieldsDTO data) {
        var result = service.modify(id, data.email(), data.name());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody String password) {
        service.changePassword(id, password);

        return ResponseEntity.created(URI.create("http://localhost:8081/login")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        service.changeActiveStatus(id, false);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        service.changeActiveStatus(id, true);

        return ResponseEntity.ok().build();
    }

}
