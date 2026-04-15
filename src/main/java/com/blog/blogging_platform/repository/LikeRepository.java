package com.blog.blogging_platform.repository;

import com.blog.blogging_platform.model.Blog;
import com.blog.blogging_platform.model.Like;
import com.blog.blogging_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByBlogAndUser(Blog blog, User user);
    long countByBlog(Blog blog);

    @Transactional
    void deleteByBlogAndUser(Blog blog, User user);
}