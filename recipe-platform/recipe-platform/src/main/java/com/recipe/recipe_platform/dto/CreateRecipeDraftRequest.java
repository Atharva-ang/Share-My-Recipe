package com.recipe.recipe_platform.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateRecipeDraftRequest {
    private String title;
    private String summary;
    private List<String> ingredients;
    private String steps;
}
