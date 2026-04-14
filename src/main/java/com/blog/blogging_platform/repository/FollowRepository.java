package com.blog.blogging_platform.repository;

import com.blog.blogging_platform.model.Follow;
import com.blog.blogging_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
    long countByFollowing(User user);
}