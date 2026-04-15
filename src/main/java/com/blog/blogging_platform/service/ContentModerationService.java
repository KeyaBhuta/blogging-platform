package com.blog.blogging_platform.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContentModerationService {

    private static final List<String> BANNED_WORDS = List.of(
        "hate", "violence", "abuse", "spam", "kill", "terror"
    );

    public boolean containsBannedContent(String text) {
        String lower = text.toLowerCase();
        return BANNED_WORDS.stream().anyMatch(lower::contains);
    }
}