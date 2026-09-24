package com.realtime_live_bidding.Auction_Platform.controller;

import com.realtime_live_bidding.Auction_Platform.dto.AuthRequest;
import com.realtime_live_bidding.Auction_Platform.dto.AuthResponse;
import com.realtime_live_bidding.Auction_Platform.entity.User;
import com.realtime_live_bidding.Auction_Platform.repository.UserRepository;
import com.realtime_live_bidding.Auction_Platform.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest().body("Username is already taken!");
            }

            User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), "ROLE_USER");
            userRepository.save(user);

            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            System.err.println("=== ERROR IN REGISTER ===");
            e.printStackTrace(); // This forces the stack trace to print in Eclipse console!
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Register Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid username or password");
            }

            String token = jwtUtils.generateJwtToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername()));
        } catch (Exception e) {
            System.err.println("=== ERROR IN LOGIN ===");
            e.printStackTrace(); // This forces the stack trace to print in Eclipse console!
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login Error: " + e.getMessage());
        }
    }
}