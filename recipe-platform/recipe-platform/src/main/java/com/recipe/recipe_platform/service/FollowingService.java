package com.recipe.recipe_platform.service;

import com.recipe.recipe_platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.recipe.recipe_platform.model.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowingService {

    private final UserRepository userRepository;

    @Transactional
    public void follow (UUID followerId, UUID chefId) {

        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        User chef = userRepository.findById(chefId).orElseThrow(() -> new RuntimeException("Chef not found"));
        // 2. Add follower to chef.getFollowers()
        if(followerId.equals(chef.getId())){
            throw new RuntimeException("Don't follow yourself");

        }
        chef.getFollowers().add(follower);

    }

    @Transactional
    public void unfollow(UUID followerId, UUID chefId) {
        User chef = userRepository.findById(chefId).orElseThrow(() -> new RuntimeException("Chef not found"));
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        chef.getFollowers().remove(follower);
    }
}