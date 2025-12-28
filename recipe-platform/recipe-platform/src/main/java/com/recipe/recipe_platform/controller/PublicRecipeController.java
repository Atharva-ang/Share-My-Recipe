package com.recipe.recipe_platform.controller;

import com.recipe.recipe_platform.model.Recipe;
import com.recipe.recipe_platform.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/recipes")
@RequiredArgsConstructor
public class PublicRecipeController {

    private final RecipeRepository recipeRepository;


    @GetMapping
    public ResponseEntity<Page<Recipe>> getAllPublishedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UUID chefId) {

        // Brief req: Pagination limits (max 50 to prevent abuse)
        int pageSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("publishedAt").descending());

        Page<Recipe> recipes;
        if (chefId != null) {
            recipes = recipeRepository.findByChefIdAndIsPublishedTrue(chefId, pageable);
        } else {
            recipes = recipeRepository.findByIsPublishedTrue(pageable);
        }

        return ResponseEntity.ok(recipes);
    }
}