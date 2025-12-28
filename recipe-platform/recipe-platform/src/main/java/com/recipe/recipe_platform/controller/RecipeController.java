package com.recipe.recipe_platform.controller;

import com.recipe.recipe_platform.dto.CreateRecipeDraftRequest;
import com.recipe.recipe_platform.model.Recipe;
import com.recipe.recipe_platform.model.User;
import com.recipe.recipe_platform.repository.RecipeRepository;
import com.recipe.recipe_platform.repository.UserRepository;
import com.recipe.recipe_platform.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    // SECURE ENDPOINT: Create a draft recipe
    @PostMapping("/draft")
    public ResponseEntity<Recipe> createDraft(
            @RequestBody CreateRecipeDraftRequest request,
            Principal principal
    ) {
        String email = principal.getName();

        User chef = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Chef not found"));

        Recipe savedRecipe = recipeService.createDraft(request, chef.getId());
        return ResponseEntity.ok(savedRecipe);
    }

    // SOCIAL ENDPOINT: Get feed of recipes from followed chefs
    @GetMapping("/feed")
    public ResponseEntity<List<Recipe>> getMyFeed(Principal principal) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Recipe> feed = recipeService.getFollowerFeed(user.getId());
        return ResponseEntity.ok(feed);
    }

    // SECURE ENDPOINT: Publish a recipe
    @PutMapping("/{id}/publish")
    public ResponseEntity<String> publishRecipe(@PathVariable Long id, Principal principal) {
        String email = principal.getName();

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!recipe.getChef().getEmail().equals(email)) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to publish this recipe.");
        }

        recipeService.publishRecipe(id);
        return ResponseEntity.ok("Recipe sent for publishing!");
    }
}
