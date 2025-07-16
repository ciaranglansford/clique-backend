package com.clique.backend.service;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.response.CreatePotResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.repo.PotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.clique.backend.model.Pot.createPotFromRequest;

@Service
@RequiredArgsConstructor
public class PotService {
    private static final Logger logger = LoggerFactory.getLogger(PotService.class);

    private final PotRepository potRepository;

    public CreatePotResponse createPot(CreatePotRequest request) {
        String contractAddress = request.getContractAddress();
        logger.info("Attempting to create pot: {}", contractAddress);

        if (contractAddress == null || contractAddress.isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        if (potRepository.findByContractAddress(contractAddress).isPresent()) {
            throw new ApiException("Pot already exists with address: " + contractAddress);
        }

        logger.info("Attempting to create pot: {}", request);

        Pot savedPot = potRepository.save(createPotFromRequest(request));

        logger.info("Pot created: {}", savedPot);
        return CreatePotResponse.builder()
                .id(savedPot.getContractAddress()) // or savedPot.getId() if you have a separate id field
                .contractAddress(savedPot.getContractAddress())
                .entryAmount(savedPot.getEntryAmount())
                .currencyType(savedPot.getCurrencyType())
                .maxPlayers(savedPot.getMaxPlayers())
                .createdAt(savedPot.getCreatedAt())
                .build();
    }

    public Pot getPotByContractAddress(String contractAddress) {
        logger.info("Fetching pot by contract address: {}", contractAddress);

        return potRepository.findByContractAddress(contractAddress)
                .orElseThrow(() -> new ApiException("Pot not found: " + contractAddress));
    }

    public List<Pot> getAllPots() {
        logger.info("Fetching all pots");
        return new ArrayList<>(potRepository.findAll());
    }

    public List<String> getAllContractAddresses() {
        logger.info("Fetching all contract addresses");

        return potRepository.findAll()
                .stream()
                .map(Pot::getContractAddress)
                .collect(Collectors.toList());
    }
}