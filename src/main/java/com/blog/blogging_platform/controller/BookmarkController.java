package com.blog.blogging_platform.controller;

import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Bookmark;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.BlogRepository;
import com.blog.blogging_platform.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkRepository bookmarkRepo;
    private final BlogRepository blogRepo;

    @GetMapping
    public ResponseEntity<?> getMine(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookmarkRepo.findByUser(user));
    }

    @PostMapping("/{blogId}")
    public ResponseEntity<?> bookmark(@PathVariable Long blogId,
                                      @AuthenticationPrincipal User user) {
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        if (!bookmarkRepo.existsByBlogAndUser(blog, user)) {
            Bookmark b = new Bookmark();
            b.setBlog(blog);
            b.setUser(user);
            bookmarkRepo.save(b);
        }
        return ResponseEntity.ok(Map.of("message", "Bookmarked"));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<?> remove(@PathVariable Long blogId,
                                    @AuthenticationPrincipal User user) {
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        bookmarkRepo.deleteByBlogAndUser(blog, user);
        return ResponseEntity.ok(Map.of("message", "Removed"));
    }
}