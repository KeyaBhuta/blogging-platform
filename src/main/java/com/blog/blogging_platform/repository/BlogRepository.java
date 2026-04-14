package com.blog.blogging_platform.repository;

import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByTitleContainingIgnoreCase(String title);
    List<Blog> findByCategoryId(Long categoryId);
    List<Blog> findByAuthor(User author);
    List<Blog> findByType(String type);
}