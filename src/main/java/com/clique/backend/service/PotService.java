package com.clique.backend.service;

import com.clique.backend.Repository.PotRepository;
import com.clique.backend.Repository.UserPotRepository;
import com.clique.backend.model.Pot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PotService {
    @Autowired
    private PotRepository potRepository;
    @Autowired
    private UserPotRepository userPotRepository;

    public Pot createPot(String contractAddress) {
        if (potRepository.findByContractAddress(contractAddress).isPresent()) {
            throw new IllegalArgumentException("Pot already exists with address: " + contractAddress);
        }
        Pot pot = new Pot();
        pot.setContractAddress(contractAddress);
        return potRepository.save(pot);
    }

    public Pot getPotByContractAddress(String contractAddress){
        return potRepository.findByContractAddress(contractAddress)
                .orElseThrow(() -> new IllegalArgumentException("Pot not found: " + contractAddress));
    }


}
