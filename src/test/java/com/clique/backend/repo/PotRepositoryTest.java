package com.clique.backend.repo;

import com.clique.backend.model.Pot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PotRepositoryTest {

    private final PotRepository potRepository = mock(PotRepository.class);

    @Test
    void findByContractAddress_shouldReturnPot() {
        Pot pot = new Pot();
        pot.setContractAddress("0xabc");
        when(potRepository.findByContractAddress("0xabc")).thenReturn(Optional.of(pot));

        Optional<Pot> found = potRepository.findByContractAddress("0xabc");

        assertTrue(found.isPresent());
        assertEquals("0xabc", found.get().getContractAddress());
    }

    @Test
    void findByContractAddress_shouldReturnEmpty() {
        when(potRepository.findByContractAddress("0xdef")).thenReturn(Optional.empty());

        Optional<Pot> found = potRepository.findByContractAddress("0xdef");

        assertFalse(found.isPresent());
    }

    @Test
    void save_shouldReturnSavedPot() {
        Pot pot = new Pot();
        pot.setContractAddress("0xsave");
        when(potRepository.save(pot)).thenReturn(pot);

        Pot saved = potRepository.save(pot);

        assertEquals("0xsave", saved.getContractAddress());
        verify(potRepository).save(pot);
    }

    @Test
    void findAll_shouldReturnListOfPots() {
        Pot pot = new Pot();
        pot.setContractAddress("0xlist");
        when(potRepository.findAll()).thenReturn(List.of(pot));

        List<Pot> pots = potRepository.findAll();

        assertEquals(1, pots.size());
        assertEquals("0xlist", pots.get(0).getContractAddress());
    }

    @Test
    void deleteById_shouldInvokeDelete() {
        doNothing().when(potRepository).deleteById("0xabc");

        potRepository.deleteById("0xabc");

        verify(potRepository).deleteById("0xabc");
    }
}