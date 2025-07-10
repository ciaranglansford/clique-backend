package com.clique.backend.service;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import com.clique.backend.repo.UserPotRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPotServiceTest {

    private final PotService potService = mock(PotService.class);
    private final UserPotRepository userPotRepository = mock(UserPotRepository.class);
    private final UserPotService userPotService = new UserPotService(potService, userPotRepository);

    @Test
    void joinPot_shouldSaveUserPot() {
        JoinPotRequest request = new JoinPotRequest();
        request.setWalletAddress("wallet1");
        request.setContractAddress("pot1");

        Pot pot = new Pot();
        pot.setContractAddress("pot1");

        when(potService.getPotByContractAddress("pot1")).thenReturn(pot);
        when(userPotRepository.findByWalletAddress("wallet1")).thenReturn(Collections.emptyList());
        when(userPotRepository.save(any(UserPot.class))).thenAnswer(i -> i.getArgument(0));

        UserPot result = userPotService.joinPot(request);

        assertEquals("wallet1", result.getWalletAddress());
        assertEquals("pot1", result.getContractAddress());
        assertNotNull(result.getJoinedAt());
        verify(userPotRepository).save(any(UserPot.class));
    }

    @Test
    void joinPot_shouldThrowIfAlreadyJoined() {
        JoinPotRequest request = new JoinPotRequest();
        request.setWalletAddress("wallet1");
        request.setContractAddress("pot1");

        Pot pot = new Pot();
        pot.setContractAddress("pot1");

        UserPot existing = UserPot.builder()
                .walletAddress("wallet1")
                .contractAddress("pot1")
                .joinedAt(LocalDateTime.now())
                .build();

        when(potService.getPotByContractAddress("pot1")).thenReturn(pot);
        when(userPotRepository.findByWalletAddress("wallet1")).thenReturn(List.of(existing));

        assertThrows(ApiException.class, () -> userPotService.joinPot(request));
    }

    @Test
    void getAllUserPots_shouldReturnPotContractList() {
        UserPot userPot = UserPot.builder()
                .walletAddress("wallet1")
                .contractAddress("pot1")
                .joinedAt(LocalDateTime.now())
                .build();

        when(userPotRepository.findByWalletAddress("wallet1")).thenReturn(List.of(userPot));

        PotContractList result = userPotService.getAllUserPots("wallet1");

        assertEquals(List.of("pot1"), result.getContractAddresses());
    }
}