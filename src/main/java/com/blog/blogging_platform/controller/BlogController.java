package com.blog.blogging_platform.controller;

import com.blog.blogging_platform.dto.BlogRequest;
import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.service.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(blogService.getAll(search, categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getById(id));
    }

    @PostMapping
public ResponseEntity<?> create(@RequestBody BlogRequest req,
                                @AuthenticationPrincipal User user) {
    try {
        return ResponseEntity.ok(blogService.create(req, user));
    } catch (RuntimeException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", e.getMessage()));
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal User user) {
        blogService.delete(id, user);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}