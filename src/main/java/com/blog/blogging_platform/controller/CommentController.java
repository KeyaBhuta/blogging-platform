package com.blog.blogging_platform.controller;

import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Comment;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.BlogRepository;
import com.blog.blogging_platform.repository.CommentRepository;
import com.blog.blogging_platform.service.ContentModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepo;
    private final BlogRepository blogRepo;
    private final ContentModerationService moderationService;

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<?> getByBlog(@PathVariable Long blogId) {
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        return ResponseEntity.ok(commentRepo.findByBlogOrderByCreatedAtDesc(blog));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body,
                                 @AuthenticationPrincipal User user) {
        String content = (String) body.get("content");
        if (moderationService.containsBannedContent(content))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flagged content");
        Long blogId = Long.valueOf(body.get("blogId").toString());
        Blog blog = blogRepo.findById(blogId).orElseThrow();
        Comment c = new Comment();
        c.setBlog(blog);
        c.setUser(user);
        c.setContent(content);
        return ResponseEntity.ok(commentRepo.save(c));
    }
    @DeleteMapping("/{id}")
public ResponseEntity<?> delete(@PathVariable Long id,
        @AuthenticationPrincipal User user) {
    Comment comment = commentRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    if (!comment.getUser().getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your comment");
    commentRepo.deleteById(id);
    return ResponseEntity.ok(Map.of("message", "Deleted"));
}
}