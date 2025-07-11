package com.clique.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "user_pots")
public class UserPot {
    @Id
    private String id;
    private String walletAddress;
    private String contractAddress;
    private LocalDateTime joinedAt;
}