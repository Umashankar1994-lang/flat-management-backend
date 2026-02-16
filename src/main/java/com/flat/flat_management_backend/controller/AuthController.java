package com.flat.flat_management_backend.controller;

import com.flat.flat_management_backend.model.LoginRequest;
import com.flat.flat_management_backend.model.RegisterRequest;
import com.flat.flat_management_backend.model.User;
import com.flat.flat_management_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        try {
            authService.register(request);
        }catch (Exception ex) {
            return ResponseEntity.ok(ex.getMessage());
        }
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Admin API is working");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request) {

        return authService.logout(request);
    }
}


