package com.blog.blogging_platform.service;

import com.blog.blogging_platform.dto.BlogRequest;
import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Category;
import com.blog.blogging_platform.model.User;
import com.blog.blogging_platform.repository.BlogRepository;
import com.blog.blogging_platform.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepo;
    private final CategoryRepository categoryRepo;
    private final ContentModerationService moderationService;

    public List<Blog> getAll(String search, Long categoryId) {
        if (search != null && !search.isEmpty())
            return blogRepo.findByTitleContainingIgnoreCase(search);
        if (categoryId != null)
            return blogRepo.findByCategoryId(categoryId);
        return blogRepo.findAll();
    }

    public Blog getById(Long id) {
        return blogRepo.findById(id).orElseThrow();
    }

    public Blog create(BlogRequest req, User user) {
        if (moderationService.containsBannedContent(req.content()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Content violates community guidelines");

        Blog blog = new Blog();
        blog.setTitle(req.title());
        blog.setContent(req.content());
        blog.setAuthor(user);

        if (req.categoryId() != null) {
            Category category = categoryRepo.findById(req.categoryId()).orElse(null);
            blog.setCategory(category);
        }

        return blogRepo.save(blog);
    }

    public void delete(Long id, User user) {
        Blog blog = blogRepo.findById(id).orElseThrow();
        if (!blog.getAuthor().getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your blog");
        blogRepo.delete(blog);
    }
}