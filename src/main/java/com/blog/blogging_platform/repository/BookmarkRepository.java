package com.blog.blogging_platform.repository;

import com.blog.blogging_platform.model.Bookmark;
import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByBlogAndUser(Blog blog, User user);
    void deleteByBlogAndUser(Blog blog, User user);
    List<Bookmark> findByUser(User user);
}