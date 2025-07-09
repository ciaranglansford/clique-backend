package com.clique.backend.service;

import com.clique.backend.model.Pot;
import com.clique.backend.repo.PotRepository;
import com.clique.backend.repo.UserPotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PotService {

    @Autowired
    private PotRepository potRepository;

    @Autowired
    private UserPotRepository userPotRepository;

    public Pot createPot(String contractAddress) {
        if (this.potRepository.findByContractAddress(contractAddress).isPresent()) {
            throw new IllegalArgumentException("Pot already exists with address: " + contractAddress);
        }
        Pot pot = new Pot();
        pot.setContractAddress(contractAddress);
        return potRepository.save(pot);
    }

    public Pot getPotByContractAddress(String contractAddress) {
        return potRepository.findByContractAddress(contractAddress)
                .orElseThrow(() -> new IllegalArgumentException("Pot not found: " + contractAddress));
    }


}
