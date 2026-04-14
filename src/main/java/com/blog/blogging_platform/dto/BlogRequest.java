package com.blog.blogging_platform.dto;

public record BlogRequest(String title, String content, Long categoryId) {}