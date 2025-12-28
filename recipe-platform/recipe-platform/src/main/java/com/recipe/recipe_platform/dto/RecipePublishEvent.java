package com.recipe.recipe_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipePublishEvent implements Serializable {
    private Long recipeId;
    private String title;
    private List<String> ingredients;
    private String chefHandle;
}