package com.clique.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MongoConnectionVerifier {

    @Bean
    public CommandLineRunner checkMongoConnection(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                log.info("MongoDB connection check - database name: {}",
                        mongoTemplate.getDb().getName());
                log.info("MongoDB connection successful");
            } catch (Exception e) {
                log.error("Failed to connect to MongoDB", e);
            }
        };
    }
}