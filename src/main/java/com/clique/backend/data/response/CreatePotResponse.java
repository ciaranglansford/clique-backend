package com.clique.backend.data.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class CreatePotResponse {
    @Id
    private String id;
    private String contractAddress;
    private Double entryAmount;
    private String currencyType;
    private int maxPlayers;
    private LocalDateTime createdAt;
}
