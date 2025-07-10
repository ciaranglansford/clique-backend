package com.clique.backend.service;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import com.clique.backend.repo.UserPotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPotService {

    private final PotService potService;
    private final UserPotRepository userPotRepository;

    public UserPot joinPot(JoinPotRequest request) {
        if (request.getWalletAddress() == null || request.getWalletAddress().isEmpty()) {
            throw new ApiException("Wallet address must not be empty");
        }
        if (request.getContractAddress() == null || request.getContractAddress().isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        Pot pot = potService.getPotByContractAddress(request.getContractAddress());
        boolean alreadyJoined = userPotRepository.findByWalletAddress(request.getWalletAddress())
                .stream()
                .anyMatch(up -> up.getContractAddress().equals(pot.getContractAddress()));
        if (alreadyJoined) {
            throw new ApiException("User already joined this pot");
        }
        UserPot userPot = UserPot.builder()
                .walletAddress(request.getWalletAddress())
                .contractAddress(pot.getContractAddress())
                .joinedAt(LocalDateTime.now())
                .build();
        return userPotRepository.save(userPot);
    }

    public PotContractList getAllUserPots(String walletAddress) {
        List<UserPot> userPots = userPotRepository.findByWalletAddress(walletAddress);
        List<String> contracts = userPots.stream()
                .map(UserPot::getContractAddress)
                .collect(Collectors.toList());
        return new PotContractList(contracts);
    }

    public List<UserPot> getUsersByContractAddress(String contractAddress) {
        if (contractAddress == null || contractAddress.isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        potService.getPotByContractAddress(contractAddress);
        return userPotRepository.findByContractAddress(contractAddress);
    }

    public void removeUserFromPot(String contractAddress, String walletAddress) {
        if (contractAddress == null || contractAddress.isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new ApiException("Wallet address must not be empty");
        }

        List<UserPot> userPots = userPotRepository.findByContractAddress(contractAddress);
        userPots.stream()
                .filter(up -> up.getWalletAddress().equals(walletAddress))
                .findFirst()
                .ifPresentOrElse(
                        userPotRepository::delete,
                        () -> {
                            throw new ApiException("User not found in this pot");
                        }
                );
    }

    public boolean isUserInPot(String contractAddress, String walletAddress) {
        if (contractAddress == null || contractAddress.isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new ApiException("Wallet address must not be empty");
        }

        return userPotRepository.findByWalletAddress(walletAddress)
                .stream()
                .anyMatch(up -> up.getContractAddress().equals(contractAddress));
    }
}