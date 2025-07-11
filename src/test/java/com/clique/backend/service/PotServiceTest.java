package com.clique.backend.service;

import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.repo.PotRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PotServiceTest {

    private final PotRepository potRepository = mock(PotRepository.class);
    private final PotService potService = new PotService(potRepository);

    @Test
    void createPot_shouldSavePot() {
        String contractAddress = "0x123";
        when(potRepository.findByContractAddress(contractAddress)).thenReturn(Optional.empty());
        when(potRepository.save(any(Pot.class))).thenAnswer(i -> i.getArgument(0));

        Pot pot = potService.createPot(contractAddress);

        assertEquals(contractAddress, pot.getContractAddress());
        verify(potRepository).save(any(Pot.class));
    }

    @Test
    void createPot_shouldThrowIfExists() {
        String contractAddress = "0x123";
        when(potRepository.findByContractAddress(contractAddress)).thenReturn(Optional.of(new Pot()));

        assertThrows(ApiException.class, () -> potService.createPot(contractAddress));
    }

    @Test
    void getAllContractAddresses_shouldReturnList() {
        Pot pot = new Pot();
        pot.setContractAddress("0x123");
        when(potRepository.findAll()).thenReturn(Collections.singletonList(pot));

        List<String> result = potService.getAllContractAddresses();

        assertEquals(1, result.size());
        assertEquals("0x123", result.get(0));
    }
}