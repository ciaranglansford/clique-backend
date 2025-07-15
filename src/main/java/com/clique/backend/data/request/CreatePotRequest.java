package com.clique.backend.data.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePotRequest {
    @NotBlank(message = "Contract address must not be blank")
    private String contractAddress;

    @NotNull(message = "Entry amount must not be null")
    @Positive(message = "Entry amount must be a positive number")
    private Double entryAmount;

    @NotBlank(message = "Currency type must not be blank")
    private String currencyType;

    @NotNull(message = "Max players must not be null")
    @Min(value = 2, message = "There must be at least 2 players in the pot")
    private Integer maxPlayers;
}