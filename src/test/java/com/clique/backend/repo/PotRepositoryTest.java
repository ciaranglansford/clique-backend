package com.clique.backend.repo;

import com.clique.backend.model.Pot;
import com.clique.backend.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.clique.backend.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PotRepositoryTest {

    private final PotRepository potRepository = mock(PotRepository.class);

    @Test
    void findByContractAddress_shouldReturnPot() {
        Pot pot = TestUtil.createPot();

        when(potRepository.findByContractAddress(CONTRACT_ADDRESS)).thenReturn(Optional.of(pot));

        Optional<Pot> found = potRepository.findByContractAddress(CONTRACT_ADDRESS);

        assertTrue(found.isPresent());
        assertEquals(CONTRACT_ADDRESS, found.get().getContractAddress());
    }

    @Test
    void findByContractAddress_shouldReturnEmpty() {
        when(potRepository.findByContractAddress(CONTRACT_ADDRESS)).thenReturn(Optional.empty());

        Optional<Pot> found = potRepository.findByContractAddress(CONTRACT_ADDRESS);

        assertFalse(found.isPresent());
    }

    @Test
    void save_shouldReturnSavedPot() {
        Pot pot = TestUtil.createPot();
        when(potRepository.save(pot)).thenReturn(pot);

        Pot saved = potRepository.save(pot);

        assertEquals(CONTRACT_ADDRESS, saved.getContractAddress());
        verify(potRepository).save(pot);
    }

    @Test
    void findAll_shouldReturnListOfPots() {
        Pot pot = TestUtil.createPot();
        when(potRepository.findAll()).thenReturn(List.of(pot));

        List<Pot> pots = potRepository.findAll();

        assertEquals(1, pots.size());
        assertEquals(CONTRACT_ADDRESS, pots.get(0).getContractAddress());
    }

    @Test
    void deleteById_shouldInvokeDelete() {
        doNothing().when(potRepository).deleteById(CONTRACT_ADDRESS);

        potRepository.deleteById(CONTRACT_ADDRESS);

        verify(potRepository).deleteById(CONTRACT_ADDRESS);
    }
}