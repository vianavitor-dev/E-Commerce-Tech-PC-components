package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.dtos.request.UserLoginFormsDTO;
import com.vianavitor.ecommerce_tech.dtos.request.UserModifiableFieldsDTO;
import com.vianavitor.ecommerce_tech.dtos.request.UserRegisterFormsDTO;
import com.vianavitor.ecommerce_tech.models.aux.auth.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginFormsDTO forms) {
        var token = service.authenticate(forms);

        return ResponseEntity.ok(token);
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterFormsDTO forms) {
        service.createNew(forms);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getById(@AuthenticationPrincipal UserDetailsImpl principal) {
        var result = service.getById(principal.getUserId());

        return ResponseEntity.ok(result);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> modify(@AuthenticationPrincipal UserDetailsImpl principal, @RequestBody UserModifiableFieldsDTO data) {
        var result = service.modify(principal.getUserId(), data.email(), data.name());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody String password) {
        service.changePassword(id, password);

        return ResponseEntity.created(URI.create("http://localhost:8081/login")).build();
    }

    private ResponseEntity<?> callChangeActiveStatus__AndReturnResponse(Integer id, boolean status) {
        service.changeActiveStatus(id, false);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<?> deactivate(@AuthenticationPrincipal UserDetailsImpl principal) {
        return callChangeActiveStatus__AndReturnResponse(principal.getUserId(), false);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        return callChangeActiveStatus__AndReturnResponse(id, true);
    }

}
