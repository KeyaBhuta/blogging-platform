package com.blog.blogging_platform.repository;

import com.blog.blogging_platform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}