package com.clique.backend.repo;

import com.clique.backend.model.UserPot;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPotRepositoryTest {

    private final UserPotRepository userPotRepository = mock(UserPotRepository.class);

    @Test
    void findByWalletAddress_shouldReturnUserPots() {
        UserPot userPot = UserPot.builder()
                .walletAddress("wallet1")
                .contractAddress("0xabc")
                .joinedAt(LocalDateTime.now())
                .build();
        when(userPotRepository.findByWalletAddress("wallet1")).thenReturn(List.of(userPot));

        List<UserPot> found = userPotRepository.findByWalletAddress("wallet1");

        assertFalse(found.isEmpty());
        assertEquals("wallet1", found.get(0).getWalletAddress());
    }

    @Test
    void findByContractAddress_shouldReturnUserPots() {
        UserPot userPot = UserPot.builder()
                .walletAddress("wallet2")
                .contractAddress("0xdef")
                .joinedAt(LocalDateTime.now())
                .build();
        when(userPotRepository.findByContractAddress("0xdef")).thenReturn(List.of(userPot));

        List<UserPot> found = userPotRepository.findByContractAddress("0xdef");

        assertFalse(found.isEmpty());
        assertEquals("0xdef", found.get(0).getContractAddress());
    }

    @Test
    void save_shouldReturnSavedUserPot() {
        UserPot userPot = UserPot.builder()
                .walletAddress("wallet3")
                .contractAddress("0xghi")
                .joinedAt(LocalDateTime.now())
                .build();
        when(userPotRepository.save(userPot)).thenReturn(userPot);

        UserPot saved = userPotRepository.save(userPot);

        assertEquals("wallet3", saved.getWalletAddress());
        verify(userPotRepository).save(userPot);
    }

    @Test
    void findAll_shouldReturnListOfUserPots() {
        UserPot userPot = UserPot.builder()
                .walletAddress("wallet4")
                .contractAddress("0xjkl")
                .joinedAt(LocalDateTime.now())
                .build();
        when(userPotRepository.findAll()).thenReturn(List.of(userPot));

        List<UserPot> userPots = userPotRepository.findAll();

        assertEquals(1, userPots.size());
        assertEquals("wallet4", userPots.get(0).getWalletAddress());
    }

    @Test
    void deleteById_shouldInvokeDelete() {
        doNothing().when(userPotRepository).deleteById("id123");

        userPotRepository.deleteById("id123");

        verify(userPotRepository).deleteById("id123");
    }
}