package com.recipe.recipe_worker.service;

import com.recipe.recipe_worker.dto.RecipePublishEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RecipeListener {


    @RabbitListener(queues = "recipe_publish_queue")
    public void handleRecipePublish(RecipePublishEvent event) {
        System.out.println("----------------------------------------");
        System.out.println(">>> WORKER RECEIVED NEW RECIPE EVENT <<<");
        System.out.println("Recipe ID: " + event.getRecipeId());
        System.out.println("Recipe Title: " + event.getTitle());
        System.out.println("Chef Handle: @" + event.getChefHandle());
        System.out.println("Ingredients: " + String.join(", ", event.getIngredients()));
        System.out.println("Status: Successfully processed.");
        System.out.println("----------------------------------------");
    }
}