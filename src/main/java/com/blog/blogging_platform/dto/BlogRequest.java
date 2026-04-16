package com.blog.blogging_platform.dto;



public record BlogRequest(
        String title,
        String content,
        Long categoryId,
        String excerpt   // optional — used by create-blog.html but not stored separately unless you add a column
) {}