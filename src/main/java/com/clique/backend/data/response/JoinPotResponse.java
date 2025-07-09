package com.clique.backend.data.response;

import com.clique.backend.model.UserPot;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JoinPotResponse {
    private String walletAddress;
    private LocalDateTime joinedAt;

    public JoinPotResponse(UserPot userPot) {
        this.walletAddress = userPot.getWalletAddress();
        this.joinedAt = userPot.getJoinedAt();
    }
}