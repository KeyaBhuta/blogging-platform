package com.blog.blogging_platform.service;

import com.blog.blogging_platform.dto.BlogRequest;
import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Category;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.BlogRepository;
import com.blog.blogging_platform.repository.CategoryRepository;
import com.blog.blogging_platform.repository.LikeRepository; // ← ADDED
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;  // ← ADDED
import java.util.List;
import java.util.Map;      // ← ADDED
import java.util.stream.Collectors; // ← ADDED

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepo;
    private final CategoryRepository categoryRepo;
    private final ContentModerationService moderationService;
    private final LikeRepository likeRepo; // ← ADDED

    // ← ADDED: converts a Blog into a Map so likeCount can be included
    private Map<String, Object> toMap(Blog blog) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",        blog.getId());
        map.put("title",     blog.getTitle());
        map.put("content",   blog.getContent());
        map.put("createdAt", blog.getCreatedAt());
        map.put("type",      blog.getType());
        map.put("likeCount", likeRepo.countByBlog(blog)); // ← key fix

        Map<String, Object> author = new HashMap<>();
        author.put("id",       blog.getAuthor().getId());
        author.put("username", blog.getAuthor().getUsername());
        author.put("bio",      blog.getAuthor().getBio() != null ? blog.getAuthor().getBio() : "");
        map.put("author", author);

        if (blog.getCategory() != null) {
            Map<String, Object> cat = new HashMap<>();
            cat.put("id",   blog.getCategory().getId());
            cat.put("name", blog.getCategory().getName());
            map.put("category", cat);
        } else {
            map.put("category", null);
        }

        return map;
    }

    // ← CHANGED: returns List<Map> instead of List<Blog> so likeCount is included
    public List<Map<String, Object>> getAll(String search, Long categoryId) {
        List<Blog> blogs;
        if (search != null && !search.isEmpty())
            blogs = blogRepo.findByTitleContainingIgnoreCase(search);
        else if (categoryId != null)
            blogs = blogRepo.findByCategoryId(categoryId);
        else
            blogs = blogRepo.findAll();

        return blogs.stream().map(this::toMap).collect(Collectors.toList());
    }

    // ← CHANGED: returns Map instead of Blog so likeCount is included
    public Map<String, Object> getById(Long id) {
        Blog blog = blogRepo.findById(id).orElseThrow();
        return toMap(blog);
    }

    // ← CHANGED: returns Map instead of Blog
    public Map<String, Object> create(BlogRequest req, User user) {
        if (moderationService.containsBannedContent(req.content()))
            throw new RuntimeException("Explicit content: blog can't be posted");

        Blog blog = new Blog();
        blog.setTitle(req.title());
        blog.setContent(req.content());
        blog.setAuthor(user);

        if (req.categoryId() != null) {
            Category category = categoryRepo.findById(req.categoryId()).orElse(null);
            blog.setCategory(category);
        }

        return toMap(blogRepo.save(blog));
    }

    // ← UNCHANGED
    public void delete(Long id, User user) {
        Blog blog = blogRepo.findById(id).orElseThrow();
        if (!blog.getAuthor().getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your blog");
        blogRepo.delete(blog);
    }
}