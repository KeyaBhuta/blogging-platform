package com.blog.blogging_platform.controller;

import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Like;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.BlogRepository;
import com.blog.blogging_platform.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeRepository likeRepo;
    private final BlogRepository blogRepo;

    @PostMapping("/{blogId}")
    public ResponseEntity<?> like(@PathVariable Long blogId,
                                  @AuthenticationPrincipal User user) {
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        if (!likeRepo.existsByBlogAndUser(blog, user)) {
            Like like = new Like();
            like.setBlog(blog);
            like.setUser(user);
            likeRepo.save(like);
        }
        return ResponseEntity.ok(Map.of("likes", likeRepo.countByBlog(blog)));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<?> unlike(@PathVariable Long blogId,
                                    @AuthenticationPrincipal User user) {
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        likeRepo.deleteByBlogAndUser(blog, user);
        return ResponseEntity.ok(Map.of("likes", likeRepo.countByBlog(blog)));
    }

    @GetMapping("/{blogId}/count")
    public ResponseEntity<?> count(@PathVariable Long blogId) {
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        return ResponseEntity.ok(Map.of("likes", likeRepo.countByBlog(blog)));
    }
}