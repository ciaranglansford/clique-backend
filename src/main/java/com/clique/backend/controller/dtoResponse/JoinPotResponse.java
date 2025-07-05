package com.clique.backend.controller.dtoResponse;

import com.clique.backend.model.UserPot;

import java.time.LocalDateTime;

public class JoinPotResponse {
    private String walletAddress;
    private LocalDateTime joinedAt;

    public JoinPotResponse(UserPot userPot) {
        this.walletAddress = userPot.getWalletAddress();
        this.joinedAt = userPot.getJoinedAt();
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}