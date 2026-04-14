package com.blog.blogging_platform.repository;

import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogOrderByCreatedAtDesc(Blog blog);
}