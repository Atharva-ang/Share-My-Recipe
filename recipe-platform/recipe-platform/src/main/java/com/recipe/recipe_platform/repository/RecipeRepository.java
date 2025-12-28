package com.recipe.recipe_platform.repository;

import com.recipe.recipe_platform.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Social Feed logic (Remains the same)
    @Query("SELECT r FROM Recipe r WHERE r.chef IN " +
            "(SELECT f FROM User u JOIN u.following f WHERE u.id = :userId) " +
            "AND r.isPublished = true " +
            "ORDER BY r.publishedAt DESC")
    List<Recipe> findFeedForUser(@Param("userId") UUID userId);

    // PAGINATION: Find all published recipes with page limits
    Page<Recipe> findByIsPublishedTrue(Pageable pageable);

    // PAGINATION: Find published recipes by a specific chef
    Page<Recipe> findByChefIdAndIsPublishedTrue(UUID chefId, Pageable pageable);
}