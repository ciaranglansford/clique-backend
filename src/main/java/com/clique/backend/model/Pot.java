package com.clique.backend.model;

import com.clique.backend.data.request.CreatePotRequest;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@Document(collection = "pots")
public class Pot {
    @Id
    private String id;
    @NotBlank(message = "Contract address must not be blank")
    private String contractAddress;
    private Double entryAmount;
    private String currencyType;
    private int maxPlayers;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static Pot createPotFromRequest(CreatePotRequest request) {
        return Pot.builder()
                .contractAddress(request.getContractAddress())
                .entryAmount(request.getEntryAmount())
                .currencyType(request.getCurrencyType())
                .maxPlayers(request.getMaxPlayers())
                .build();
    }
}