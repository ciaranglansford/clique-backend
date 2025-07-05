package com.clique.backend.service;

import com.clique.backend.Repository.UserPotRepository;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPotService {

    @Autowired
    private PotService potService;
    @Autowired
    private UserPotRepository userPotRepository;


    public UserPot joinPot(String contractAddress, String walletAddress) {
        Pot pot = potService.getPotByContractAddress(contractAddress);

        UserPot userPot = new UserPot();
        userPot.setWalletAddress(walletAddress);
        userPot.setPot(pot);

        return userPotRepository.save(userPot);
    }

    public PotContractList getAllUserPots(String walletAddress){
        List<String> contracts = userPotRepository.findContractAddressesByWalletAddress(walletAddress);
        return new PotContractList(contracts);
    }
}
