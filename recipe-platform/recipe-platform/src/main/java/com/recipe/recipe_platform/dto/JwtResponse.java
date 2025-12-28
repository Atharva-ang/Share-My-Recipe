package com.recipe.recipe_platform.dto;

public record JwtResponse(String token, String email, String role) {}