package com.university.securetracker.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.dto.LoginRequest;
import com.university.securetracker.dto.RegisterRequest;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.UserRepository;
import com.university.securetracker.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // ✅ constructor injection
    public AuthController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = new JwtUtil();
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setStatus("ACTIVE");

        repo.save(user);

        return "User created";
    }
@PostMapping("/login")
public String login(@RequestBody LoginRequest req){

    User user = repo.findByEmail(req.getEmail()).orElseThrow();

    if(!encoder.matches(req.getPassword(), user.getPassword())){
        return "Invalid password";
    }

    return jwtUtil.generateToken(user.getEmail(), user.getRole());
}

}
