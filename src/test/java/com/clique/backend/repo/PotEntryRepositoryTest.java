package com.clique.backend.repo;

import com.clique.backend.model.PotEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PotEntryRepositoryTest {

    private final PotEntryRepository potEntryRepository = mock(PotEntryRepository.class);

    @Test
    void findByWalletAddress_shouldReturnUserPots() {
        PotEntry potEntry = PotEntry.builder()
                .walletAddress("wallet1")
                .contractAddress("0xabc")
                .joinedAt(LocalDateTime.now())
                .build();
        when(potEntryRepository.findByWalletAddress("wallet1")).thenReturn(List.of(potEntry));

        List<PotEntry> found = potEntryRepository.findByWalletAddress("wallet1");

        assertFalse(found.isEmpty());
        assertEquals("wallet1", found.get(0).getWalletAddress());
    }

    @Test
    void findByContractAddress_shouldReturnUserPots() {
        PotEntry potEntry = PotEntry.builder()
                .walletAddress("wallet2")
                .contractAddress("0xdef")
                .joinedAt(LocalDateTime.now())
                .build();
        when(potEntryRepository.findByContractAddress("0xdef")).thenReturn(List.of(potEntry));

        List<PotEntry> found = potEntryRepository.findByContractAddress("0xdef");

        assertFalse(found.isEmpty());
        assertEquals("0xdef", found.get(0).getContractAddress());
    }

    @Test
    void save_shouldReturnSavedUserPot() {
        PotEntry potEntry = PotEntry.builder()
                .walletAddress("wallet3")
                .contractAddress("0xghi")
                .joinedAt(LocalDateTime.now())
                .build();
        when(potEntryRepository.save(potEntry)).thenReturn(potEntry);

        PotEntry saved = potEntryRepository.save(potEntry);

        assertEquals("wallet3", saved.getWalletAddress());
        verify(potEntryRepository).save(potEntry);
    }

    @Test
    void findAll_shouldReturnListOfUserPots() {
        PotEntry potEntry = PotEntry.builder()
                .walletAddress("wallet4")
                .contractAddress("0xjkl")
                .joinedAt(LocalDateTime.now())
                .build();
        when(potEntryRepository.findAll()).thenReturn(List.of(potEntry));

        List<PotEntry> potEntries = potEntryRepository.findAll();

        assertEquals(1, potEntries.size());
        assertEquals("wallet4", potEntries.get(0).getWalletAddress());
    }

    @Test
    void deleteById_shouldInvokeDelete() {
        doNothing().when(potEntryRepository).deleteById("id123");

        potEntryRepository.deleteById("id123");

        verify(potEntryRepository).deleteById("id123");
    }
}