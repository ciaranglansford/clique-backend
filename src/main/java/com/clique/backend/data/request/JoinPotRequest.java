package com.clique.backend.data.request;

import lombok.Data;

@Data
public class JoinPotRequest {
    private String contractAddress;
    private String walletAddress;
}
