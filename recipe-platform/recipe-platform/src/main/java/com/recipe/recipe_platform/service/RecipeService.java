package com.recipe.recipe_platform.service;

import com.recipe.recipe_platform.config.RabbitMQConfig;
import com.recipe.recipe_platform.dto.CreateRecipeDraftRequest;
import com.recipe.recipe_platform.dto.RecipePublishEvent;
import com.recipe.recipe_platform.model.Recipe;
import com.recipe.recipe_platform.model.User;
import com.recipe.recipe_platform.repository.RecipeRepository;
import com.recipe.recipe_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    // 1. DRAFT LOGIC
    @Transactional
    public Recipe createDraft(CreateRecipeDraftRequest request, UUID chefId) {

        User chef = userRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef not found"));

        Recipe recipe = new Recipe();
        recipe.setTitle(request.getTitle());
        recipe.setSummary(request.getSummary());
        recipe.setSteps(request.getSteps());
        recipe.setIngredients(request.getIngredients());

        // Business invariants
        recipe.setChef(chef);
        recipe.setPublished(false); // draft == not published

        return recipeRepository.save(recipe);
    }

    // 2. SOCIAL FEED LOGIC
    public List<Recipe> getFollowerFeed(UUID userId) {
        return recipeRepository.findFeedForUser(userId);
    }

    // 3. ASYNC PUBLISH LOGIC
    @Transactional
    public void publishRecipe(Long recipeId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.publish(); // sets isPublished=true + publishedAt
        recipeRepository.save(recipe);

        RecipePublishEvent event = new RecipePublishEvent(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getIngredients(),
                recipe.getChef().getHandle()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RECIPE_PUBLISH_QUEUE,
                event
        );
    }
}
