package com.hyscalar.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyscalar.config.JwtUtil;
import com.hyscalar.dto.AuthRequest;
import com.hyscalar.entity.User;
import com.hyscalar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @SuppressWarnings("unused")
	private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(request.getUsername());
        if(optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password");
        }

        User user = optionalUser.get();
        if(!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password");
        }

     // AuthController.java
        String token = jwtUtil.generateToken(user.getEmail(), user.getName(),"ROLE_" + user.getRole().name());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "role", user.getRole().name()
        ));
    }

    
    
    @GetMapping("/test")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String testEndpoint() {
        return "You are logged in as EMPLOYEE!";
    }




}
