package com.recipe.recipe_platform.dto;

public record SignupRequest(String email, String password, String handle, String role) {}