package com.clique.backend.service;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import com.clique.backend.repo.UserPotRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPotService {

    private final PotService potService;
    private final UserPotRepository userPotRepository;

    public UserPot joinPot(JoinPotRequest request) {
        Pot pot = potService.getPotByContractAddress(request.getContractAddress());

        UserPot userPot = UserPot.builder()
                .walletAddress(request.getWalletAddress())
                .pot(pot)
                .build();

        return userPotRepository.save(userPot);
    }

    public PotContractList getAllUserPots(String walletAddress) {
        List<String> contracts = userPotRepository.findContractAddressesByWalletAddress(walletAddress);

        return new PotContractList(contracts);
    }
}
