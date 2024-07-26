package com.chatop.controller;

import com.chatop.model.User;
import com.chatop.service.UserService;
import com.chatop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Invalid input");
        }
        User registeredUser = userService.register(user);
        final String jwtToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        final String jwtToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));
        System.out.println("Generated JWT Token: " + jwtToken);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Invalid Authorization header");
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String token = authHeader.substring(7);
        System.out.println("Extracted Token: " + token);
        String username = jwtUtil.extractUsername(token);

        User user = userService.getUserByEmail(username);
        if (user == null) {
            System.out.println("User not found");
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(user);
    }

}
