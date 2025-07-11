package com.clique.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.bson.Document;

@Configuration
public class MongoHealthConfig {

    @Bean
    public HealthIndicator mongoHealthIndicator(final MongoTemplate mongoTemplate) {
        return () -> {
            try {
                mongoTemplate.getDb().runCommand(new Document("ping", 1));
                return Health.up().build();
            } catch (Exception e) {
                return Health.down().withException(e).build();
            }
        };
    }
}