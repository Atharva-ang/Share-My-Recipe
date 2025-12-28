package com.recipe.recipe_platform.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RECIPE_PUBLISH_QUEUE = "recipe_publish_queue";

    @Bean
    public Queue recipeQueue() {
        return new Queue(RECIPE_PUBLISH_QUEUE, true); // true = durable (survives restart)
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // Send data as JSON, not weird Java bytes
    }
}