package com.clique.backend.service;

import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.repo.PotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PotService {

    private final PotRepository potRepository;

    public Pot createPot(String contractAddress) {
        if (contractAddress == null || contractAddress.isEmpty()) {
            throw new ApiException("Contract address must not be empty");
        }
        if (potRepository.findByContractAddress(contractAddress).isPresent()) {
            throw new ApiException("Pot already exists with address: " + contractAddress);
        }
        Pot pot = new Pot();
        pot.setContractAddress(contractAddress);
        return potRepository.save(pot);
    }

    public Pot getPotByContractAddress(String contractAddress) {
        return potRepository.findByContractAddress(contractAddress)
                .orElseThrow(() -> new ApiException("Pot not found: " + contractAddress));
    }

    public List<String> getAllContractAddresses() {
        return potRepository.findAll()
                .stream()
                .map(Pot::getContractAddress)
                .collect(Collectors.toList());
    }
}