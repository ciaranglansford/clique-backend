package com.clique.backend.service;

import com.clique.backend.client.PotClient;
import com.clique.backend.client.UserPotClient;
import com.clique.backend.model.Pot;
import com.clique.backend.model.UserPot;
import org.springframework.stereotype.Service;

@Service
public class PotService {

    private final PotClient potClient;
    private final UserPotClient userPotClient;

    public PotService(PotClient potClient, UserPotClient userPotClient) {
        this.potClient = potClient;
        this.userPotClient = userPotClient;
    }

    public Pot createPot(String contractAddress) {
        if (potClient.findByContractAddress(contractAddress).isPresent()) {
            throw new IllegalArgumentException("Pot already exists with address: " + contractAddress);
        }
        Pot pot = new Pot();
        pot.setContractAddress(contractAddress);
        return potClient.save(pot);
    }

    public UserPot joinPot(String contractAddress, String walletAddress) {
        Pot pot = potClient.findByContractAddress(contractAddress)
                .orElseThrow(() -> new IllegalArgumentException("Pot not found: " + contractAddress));

        UserPot userPot = new UserPot();
        userPot.setWalletAddress(walletAddress);
        userPot.setPot(pot);

        return userPotClient.save(userPot);
    }
}
