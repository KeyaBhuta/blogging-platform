package com.blog.blogging_platform.controller;

import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepo;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        return userRepo.findById(id)
            .map(u -> ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "bio", u.getBio() != null ? u.getBio() : ""
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User user) {
        if (body.containsKey("bio")) user.setBio(body.get("bio"));
        if (body.containsKey("profilePic")) user.setProfilePic(body.get("profilePic"));
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "Profile updated"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail(),
            "bio", user.getBio() != null ? user.getBio() : ""
        ));
    }
}