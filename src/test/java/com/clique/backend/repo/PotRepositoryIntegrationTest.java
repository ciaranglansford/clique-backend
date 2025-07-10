package com.clique.backend.repo;

import com.clique.backend.config.MongoTestConfiguration;
import com.clique.backend.model.Pot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
@Import(MongoTestConfiguration.class)
class PotRepositoryIntegrationTest {

    @Autowired
    private PotRepository potRepository;

    @Test
    void saveAndFindByContractAddress_shouldWork() {
        // Arrange
        Pot pot = new Pot();
        pot.setContractAddress("0xabc");

        // Act
        potRepository.save(pot);
        Optional<Pot> found = potRepository.findByContractAddress("0xabc");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("0xabc", found.get().getContractAddress());
    }

    @Test
    void findByContractAddress_shouldReturnEmptyIfNotFound() {
        // Act
        Optional<Pot> found = potRepository.findByContractAddress("non-existent");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_shouldReturnAllSavedPots() {
        // Arrange
        potRepository.deleteAll(); // Clear any existing data

        Pot pot1 = new Pot();
        pot1.setContractAddress("0xabc1");

        Pot pot2 = new Pot();
        pot2.setContractAddress("0xabc2");

        potRepository.saveAll(List.of(pot1, pot2));

        // Act
        List<Pot> pots = potRepository.findAll();

        // Assert
        assertEquals(2, pots.size());
        assertTrue(pots.stream().anyMatch(p -> p.getContractAddress().equals("0xabc1")));
        assertTrue(pots.stream().anyMatch(p -> p.getContractAddress().equals("0xabc2")));
    }

    @Test
    void deletePot_shouldRemoveFromDatabase() {
        // Arrange
        Pot pot = new Pot();
        pot.setContractAddress("0xabc");
        pot = potRepository.save(pot);

        // Act
        potRepository.delete(pot);

        // Assert
        assertFalse(potRepository.findById(pot.getContractAddress()).isPresent());
    }

    @Test
    void updatePot_shouldUpdateFields() {
        // Arrange
        Pot pot = new Pot();
        pot.setContractAddress("0xabc");
        pot = potRepository.save(pot);

        // Act
        pot.setContractAddress("0xdef");
        potRepository.save(pot);

        // Assert
        Optional<Pot> found = potRepository.findById(pot.getContractAddress());
        assertTrue(found.isPresent());
        assertEquals("0xdef", found.get().getContractAddress());
    }

    @Test
    void countPots_shouldReturnCorrectNumber() {
        // Arrange
        potRepository.deleteAll();
        potRepository.saveAll(List.of(
                createPot("0x111"),
                createPot("0x222"),
                createPot("0x333")
        ));

        // Act & Assert
        assertEquals(3, potRepository.count());
    }

    @Test
    void batchOperations_shouldWorkCorrectly() {
        // Arrange
        potRepository.deleteAll();

        List<Pot> pots = List.of(
                createPot("0xbatch1"),
                createPot("0xbatch2"),
                createPot("0xbatch3")
        );

        // Act - saveAll
        potRepository.saveAll(pots);

        // Assert
        assertEquals(3, potRepository.count());

        // Act - deleteAll
        potRepository.deleteAll();

        // Assert
        assertEquals(0, potRepository.count());
    }

    private Pot createPot(String address) {
        Pot pot = new Pot();
        pot.setContractAddress(address);
        return pot;
    }
}