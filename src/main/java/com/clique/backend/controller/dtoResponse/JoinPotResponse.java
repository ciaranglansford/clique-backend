package com.clique.backend.controller.dtoResponse;

import com.clique.backend.model.UserPot;

import java.time.LocalDateTime;

public class JoinPotResponse {
    private Long potId;
    private String walletAddress;
    private LocalDateTime joinedAt;

    public JoinPotResponse(UserPot userPot) {
        this.potId = userPot.getPot().getId();
        this.walletAddress = userPot.getWalletAddress();
        this.joinedAt = userPot.getJoinedAt();
    }

    public Long getPotId() {
        return potId;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}