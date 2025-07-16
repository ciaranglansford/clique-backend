package com.clique.backend.service;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.response.CreatePotResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.repo.PotRepository;
import com.clique.backend.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static com.clique.backend.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PotServiceTest {

    private PotRepository potRepository;
    private PotService potService;

    @BeforeEach
    void setUp() {
        potRepository = mock(PotRepository.class);
        potService = new PotService(potRepository);
    }

    @Test
    void createPot_shouldCreatePot() {
        CreatePotRequest request = TestUtil.getCreatePotRequest();
        Pot pot = Pot.createPotFromRequest(request);
        when(potRepository.findByContractAddress(CONTRACT_ADDRESS)).thenReturn(Optional.empty());
        when(potRepository.save(any(Pot.class))).thenReturn(pot);

        CreatePotResponse response = potService.createPot(request);

        assertEquals(CONTRACT_ADDRESS, response.getContractAddress());
        assertEquals(MAX_PLAYERS, response.getMaxPlayers());
        assertEquals(CURRENCY_TYPE, response.getCurrencyType());
        assertEquals(ENTRY_AMOUNT, response.getEntryAmount());
        assertEquals(pot.getCreatedAt(), response.getCreatedAt());
    }

    @Test
    void createPot_shouldThrowIfExists() {
        CreatePotRequest potRequest = TestUtil.getCreatePotRequest();
        Pot pot = createPot();
        when(potRepository.findByContractAddress(CONTRACT_ADDRESS)).thenReturn(Optional.of(pot));

        assertThrows(ApiException.class, () -> potService.createPot(potRequest));
    }

    @Test
    void createPot_shouldThrowIfAddressIsEmpty() {
        CreatePotRequest potRequest = TestUtil.getCreatePotRequest();
        potRequest.setContractAddress("");

        assertThrows(ApiException.class, () -> potService.createPot(potRequest));
    }

    @Test
    void getPotByContractAddress_shouldReturnPot() {
        Pot pot = createPot();
        when(potRepository.findByContractAddress(CONTRACT_ADDRESS)).thenReturn(Optional.of(pot));

        Pot result = potService.getPotByContractAddress(CONTRACT_ADDRESS);
        assertEquals(CONTRACT_ADDRESS, result.getContractAddress());
    }

    @Test
    void getPotByContractAddress_shouldThrowIfNotFound() {
        when(potRepository.findByContractAddress(CONTRACT_ADDRESS)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> potService.getPotByContractAddress(CONTRACT_ADDRESS));
    }

    @Test
    void getAllContractAddresses_shouldReturnList() {
        Pot pot = createPot();
        when(potRepository.findAll()).thenReturn(Collections.singletonList(pot));

        List<String> result = potService.getAllContractAddresses();

        assertEquals(1, result.size());
        assertEquals(CONTRACT_ADDRESS, result.get(0));
    }


}