package com.clique.backend.service;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.PotEntry;
import com.clique.backend.repo.PotEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PotEntryService {
    private static final Logger logger = LoggerFactory.getLogger(PotEntryService.class);

    private final PotService potService;
    private final PotEntryRepository potEntryRepository;

    public PotEntry joinPot(JoinPotRequest request) {
        logger.info("User {} joining pot {}", request.getWalletAddress(), request.getContractAddress());

        checkRequest(request.getWalletAddress(), "Wallet address must not be empty", request.getContractAddress(), "Contract address must not be empty");

        Pot pot = potService.getPotByContractAddress(request.getContractAddress());
        boolean alreadyJoined = potEntryRepository.findByWalletAddress(request.getWalletAddress())
                .stream()
                .anyMatch(userPot -> userPot.getContractAddress().equals(pot.getContractAddress()));

        if (alreadyJoined) {
            throw new ApiException("User already joined this pot");
        }
        PotEntry potEntry = PotEntry.builder()
                .walletAddress(request.getWalletAddress())
                .contractAddress(pot.getContractAddress())
                .joinedAt(LocalDateTime.now())
                .build();

        PotEntry savedPot = potEntryRepository.save(potEntry);
        logger.info("User {} joined pot {}", request.getWalletAddress(), request.getContractAddress());
        return savedPot;
    }

    public PotContractList getAllPotEntries(String walletAddress) {
        logger.info("Fetching all pots for user {}", walletAddress);

        List<PotEntry> potEntries = potEntryRepository.findByWalletAddress(walletAddress);
        List<String> contracts = potEntries.stream()
                .map(PotEntry::getContractAddress)
                .collect(Collectors.toList());

        return new PotContractList(contracts);
    }

    public List<PotEntry> getUsersByContractAddress(String contractAddress) {
        logger.info("Fetching users for pot {}", contractAddress);
        if (contractAddress == null || contractAddress.isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        potService.getPotByContractAddress(contractAddress);
        return potEntryRepository.findByContractAddress(contractAddress);
    }

    public boolean isUserInPot(String contractAddress, String walletAddress) {
        logger.info("Checking if user {} is in pot {}", walletAddress, contractAddress);

        checkRequest(contractAddress, "Contract address must not be empty", walletAddress, "Wallet address must not be empty");

        return potEntryRepository.findByWalletAddress(walletAddress)
                .stream()
                .anyMatch(up -> up.getContractAddress().equals(contractAddress));
    }

    public void removeUserFromPot(String contractAddress, String walletAddress) {
        logger.info("Removing user {} from pot {}", walletAddress, contractAddress);

        checkRequest(contractAddress, "Contract address must not be empty", walletAddress, "Wallet address must not be empty");

        List<PotEntry> potEntries = potEntryRepository.findByContractAddress(contractAddress);
        potEntries.stream()
                .filter(up -> up.getWalletAddress().equals(walletAddress))
                .findFirst()
                .ifPresentOrElse(
                        potEntryRepository::delete,
                        () -> {
                            throw new ApiException("User not found in this pot");
                        }
                );
        logger.info("User {} removed from pot {}", walletAddress, contractAddress);
    }

    private static void checkRequest(String request, String message, String request1, String message1) {
        if (request == null || request.isEmpty()) {
            throw new ApiException(message);
        }
        if (request1 == null || request1.isEmpty()) {
            throw new ApiException(message1);
        }
    }

}