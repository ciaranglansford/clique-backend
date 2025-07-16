package com.clique.backend.repo;

import com.clique.backend.config.MongoTestConfiguration;
import com.clique.backend.model.PotEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.clique.backend.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
@Import(MongoTestConfiguration.class)
class PotEntryRepositoryIntegrationTest {

    @Autowired
    private PotEntryRepository potEntryRepository;

    @Test
    void saveAndFindById_shouldWork() {
        // Arrange
        PotEntry potEntry = createUserPot(CONTRACT_ADDRESS, WALLET_ADDRESS);

        // Act
        PotEntry savedPotEntry = potEntryRepository.save(potEntry);
        Optional<PotEntry> found = potEntryRepository.findById(savedPotEntry.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(CONTRACT_ADDRESS, found.get().getContractAddress());
        assertEquals(WALLET_ADDRESS, found.get().getWalletAddress());
    }

    @Test
    void findByWalletAddress_shouldWork() {
        // Arrange
        potEntryRepository.deleteAll();
        potEntryRepository.save(createUserPot("0xcontract1", "0xwallet1"));
        potEntryRepository.save(createUserPot("0xcontract2", "0xwallet1"));
        potEntryRepository.save(createUserPot("0xcontract3", "0xwallet2"));

        // Act
        List<PotEntry> found = potEntryRepository.findByWalletAddress("0xwallet1");

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(up -> up.getWalletAddress().equals("0xwallet1")));
    }

    @Test
    void findByContractAddress_shouldWork() {
        // Arrange
        potEntryRepository.deleteAll();
        potEntryRepository.save(createUserPot("0xcontract1", "0xwallet1"));
        potEntryRepository.save(createUserPot("0xcontract1", "0xwallet2"));
        potEntryRepository.save(createUserPot("0xcontract2", "0xwallet3"));

        // Act
        List<PotEntry> found = potEntryRepository.findByContractAddress("0xcontract1");

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(up -> up.getContractAddress().equals("0xcontract1")));
    }

    @Test
    void deleteUserPot_shouldRemoveFromDatabase() {
        // Arrange
        PotEntry potEntry = potEntryRepository.save(createUserPot("0xcontract", "0xwallet"));
        String id = potEntry.getId();

        // Act
        potEntryRepository.delete(potEntry);

        // Assert
        assertFalse(potEntryRepository.findById(id).isPresent());
    }

    @Test
    void updateUserPot_shouldUpdateFields() {
        // Arrange
        PotEntry potEntry = createUserPot("0xcontract", "0xwallet");
        potEntry = potEntryRepository.save(potEntry);
        LocalDateTime newTime = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS);

        // Act
        PotEntry updatedPot = PotEntry.builder()
                .id(potEntry.getId())
                .contractAddress(potEntry.getContractAddress())
                .walletAddress(potEntry.getWalletAddress())
                .joinedAt(newTime)
                .build();
        potEntryRepository.save(updatedPot);

        // Assert
        Optional<PotEntry> found = potEntryRepository.findById(potEntry.getId());
        assertTrue(found.isPresent());

        // Compare truncated timestamps to avoid precision issues
        assertEquals(
                newTime.truncatedTo(ChronoUnit.MILLIS),
                found.get().getJoinedAt().truncatedTo(ChronoUnit.MILLIS)
        );
    }

    @Test
    void countUserPots_shouldReturnCorrectNumber() {
        // Arrange
        potEntryRepository.deleteAll();
        potEntryRepository.saveAll(List.of(
                createUserPot("0xcontract1", "0xwallet1"),
                createUserPot("0xcontract2", "0xwallet2"),
                createUserPot("0xcontract3", "0xwallet3")
        ));

        // Act & Assert
        assertEquals(3, potEntryRepository.count());
    }

    @Test
    void batchOperations_shouldWorkCorrectly() {
        // Arrange
        potEntryRepository.deleteAll();

        List<PotEntry> potEntries = List.of(
                createUserPot("0xcontract1", "0xwallet1"),
                createUserPot("0xcontract2", "0xwallet2"),
                createUserPot("0xcontract3", "0xwallet3")
        );

        // Act - saveAll
        potEntryRepository.saveAll(potEntries);

        // Assert
        assertEquals(3, potEntryRepository.count());

        // Act - deleteAll
        potEntryRepository.deleteAll();

        // Assert
        assertEquals(0, potEntryRepository.count());
    }
}