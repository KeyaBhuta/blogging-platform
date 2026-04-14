package com.blog.blogging_platform.service;

import com.blog.blogging_platform.dto.LoginRequest;
import com.blog.blogging_platform.dto.RegisterRequest;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.UserRepository;
import com.blog.blogging_platform.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public Map<String, String> register(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(encoder.encode(req.password()));
        userRepo.save(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("token", token, "username", user.getUsername());
    }

    public Map<String, String> login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(), req.password()));
        User user = userRepo.findByUsername(req.username()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "userId", user.getId().toString()
        );
    }
}