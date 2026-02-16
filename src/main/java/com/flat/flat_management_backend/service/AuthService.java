package com.flat.flat_management_backend.service;

import com.flat.flat_management_backend.exception.InvalidCredentialsException;
import com.flat.flat_management_backend.repository.UserRepository;
import com.flat.flat_management_backend.model.LoginRequest;
import com.flat.flat_management_backend.model.LoginResponse;
import com.flat.flat_management_backend.model.RegisterRequest;
import com.flat.flat_management_backend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByFlatNo(request.getFlatNo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid flat number or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());

        // 🔥 Single Device Logic
        user.setActiveJwtToken(token);
        userRepository.save(user);

        return new LoginResponse(token);
    }

    public void register(RegisterRequest request) {

        if (userRepository.findByFlatNo(request.getFlatNo()).isPresent()) {
            throw new RuntimeException("Flat already registered");
        }

        User user = new User();
        user.setFlatNo(request.getFlatNo());
        user.setName(request.getName());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token provided");
        }
        String token = authHeader.substring(7);

        Long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Invalidate token
        user.setActiveJwtToken(null);
        userRepository.save(user);
        return ResponseEntity.ok("Logged out successfully");
    }
}

