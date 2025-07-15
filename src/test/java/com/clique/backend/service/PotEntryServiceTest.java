package com.clique.backend.service;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.PotEntry;
import com.clique.backend.repo.PotEntryRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.clique.backend.util.TestUtil.*;
import static com.clique.backend.util.TestUtil.createPot;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PotEntryServiceTest {

    private final PotService potService = mock(PotService.class);
    private final PotEntryRepository potEntryRepository = mock(PotEntryRepository.class);
    private final PotEntryService potEntryService = new PotEntryService(potService, potEntryRepository);

    @Test
    void joinPot_shouldSaveUserPot() {
        JoinPotRequest request = new JoinPotRequest();
        request.setWalletAddress(WALLET_ADDRESS);
        request.setContractAddress(CONTRACT_ADDRESS);

        Pot pot = createPot();

        when(potService.getPotByContractAddress(CONTRACT_ADDRESS)).thenReturn(pot);
        when(potEntryRepository.findByWalletAddress(WALLET_ADDRESS)).thenReturn(Collections.emptyList());
        when(potEntryRepository.save(any(PotEntry.class))).thenAnswer(i -> i.getArgument(0));

        PotEntry result = potEntryService.joinPot(request);

        assertEquals(WALLET_ADDRESS, result.getWalletAddress());
        assertEquals(CONTRACT_ADDRESS, result.getContractAddress());
        assertNotNull(result.getJoinedAt());
        verify(potEntryRepository).save(any(PotEntry.class));
    }

    @Test
    void joinPot_shouldThrowIfAlreadyJoined() {
        JoinPotRequest request = new JoinPotRequest();
        request.setWalletAddress(WALLET_ADDRESS);
        request.setContractAddress(CONTRACT_ADDRESS);

        Pot pot = createPot();

        PotEntry existing = PotEntry.builder()
                .walletAddress(WALLET_ADDRESS)
                .contractAddress(CONTRACT_ADDRESS)
                .joinedAt(LocalDateTime.now())
                .build();

        when(potService.getPotByContractAddress(CONTRACT_ADDRESS)).thenReturn(pot);
        when(potEntryRepository.findByWalletAddress(WALLET_ADDRESS)).thenReturn(List.of(existing));

        assertThrows(ApiException.class, () -> potEntryService.joinPot(request));
    }

    @Test
    void getAllPotEntries_shouldReturnPotContractList() {
        PotEntry potEntry = PotEntry.builder()
                .walletAddress(WALLET_ADDRESS)
                .contractAddress(CONTRACT_ADDRESS)
                .joinedAt(LocalDateTime.now())
                .build();

        when(potEntryRepository.findByWalletAddress(WALLET_ADDRESS)).thenReturn(List.of(potEntry));

        PotContractList result = potEntryService.getAllPotEntries(WALLET_ADDRESS);

        assertEquals(List.of(CONTRACT_ADDRESS), result.getContractAddresses());
    }
}