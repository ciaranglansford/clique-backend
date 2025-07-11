package com.clique.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "pots")
public class Pot {
    @Id
    private String contractAddress;
    private LocalDateTime createdAt = LocalDateTime.now();
}