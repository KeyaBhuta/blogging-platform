package com.blog.blogging_platform.controller;

import com.blog.blogging_platform.model.Follow;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.FollowRepository;
import com.blog.blogging_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    @PostMapping("/{userId}")
    public ResponseEntity<?> follow(@PathVariable Long userId,
                                    @AuthenticationPrincipal User follower) {
        User toFollow = userRepo.findById(userId).orElseThrow();
        if (!followRepo.existsByFollowerAndFollowing(follower, toFollow)) {
            Follow f = new Follow();
            f.setFollower(follower);
            f.setFollowing(toFollow);
            followRepo.save(f);
        }
        return ResponseEntity.ok(Map.of(
            "message", "Following",
            "followers", followRepo.countByFollowing(toFollow)
        ));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> unfollow(@PathVariable Long userId,
                                      @AuthenticationPrincipal User follower) {
        User toUnfollow = userRepo.findById(userId).orElseThrow();
        followRepo.deleteByFollowerAndFollowing(follower, toUnfollow);
        return ResponseEntity.ok(Map.of("message", "Unfollowed"));
    }
}