package com.clique.backend.data.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JoinPotResponse {
    private String walletAddress;
    private LocalDateTime joinedAt;
}