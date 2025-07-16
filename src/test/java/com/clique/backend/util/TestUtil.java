package com.clique.backend.util;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.data.response.CreatePotResponse;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotEntry;

import java.time.LocalDateTime;

public class TestUtil {

    public static final String CONTRACT_ADDRESS = "0x123";
    public static final String WALLET_ADDRESS = "0xABC";
    public static final String CURRENCY_TYPE = "ETH";
    public static final Double ENTRY_AMOUNT = 1.5;
    public static final int MAX_PLAYERS = 10;


    public static Pot createPot() {
        return Pot.builder()
                .contractAddress(CONTRACT_ADDRESS)
                .build();
    }

    public static PotEntry createUserPot(String contractAddress, String walletAddress) {
        return PotEntry.builder()
                .contractAddress(contractAddress)
                .walletAddress(walletAddress)
                .joinedAt(LocalDateTime.now())
                .build();
    }

    public static JoinPotRequest createJoinPotRequest(String address) {
        return JoinPotRequest.builder()
                .contractAddress(address)
                .build();
    }

    public static CreatePotRequest getCreatePotRequest() {
        CreatePotRequest request = new CreatePotRequest();
        request.setContractAddress(CONTRACT_ADDRESS);
        request.setEntryAmount(ENTRY_AMOUNT);
        request.setCurrencyType(CURRENCY_TYPE);
        request.setMaxPlayers(MAX_PLAYERS);
        return request;
    }

    public static CreatePotResponse getCreatePotResponse() {
        return CreatePotResponse.builder()
                .contractAddress(CONTRACT_ADDRESS)
                .entryAmount(ENTRY_AMOUNT)
                .currencyType(CURRENCY_TYPE)
                .maxPlayers(MAX_PLAYERS)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
