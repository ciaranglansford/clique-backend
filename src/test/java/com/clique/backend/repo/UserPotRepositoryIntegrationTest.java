package com.clique.backend.repo;

import com.clique.backend.config.MongoTestConfiguration;
import com.clique.backend.model.UserPot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
@Import(MongoTestConfiguration.class)
class UserPotRepositoryIntegrationTest {

    @Autowired
    private UserPotRepository userPotRepository;

    @Test
    void saveAndFindById_shouldWork() {
        // Arrange
        UserPot userPot = createUserPot("0xcontract1", "0xwallet1");

        // Act
        userPot = userPotRepository.save(userPot);
        Optional<UserPot> found = userPotRepository.findById(userPot.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("0xcontract1", found.get().getContractAddress());
        assertEquals("0xwallet1", found.get().getWalletAddress());
    }

    @Test
    void findByWalletAddress_shouldWork() {
        // Arrange
        userPotRepository.deleteAll();
        userPotRepository.save(createUserPot("0xcontract1", "0xwallet1"));
        userPotRepository.save(createUserPot("0xcontract2", "0xwallet1"));
        userPotRepository.save(createUserPot("0xcontract3", "0xwallet2"));

        // Act
        List<UserPot> found = userPotRepository.findByWalletAddress("0xwallet1");

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(up -> up.getWalletAddress().equals("0xwallet1")));
    }

    @Test
    void findByContractAddress_shouldWork() {
        // Arrange
        userPotRepository.deleteAll();
        userPotRepository.save(createUserPot("0xcontract1", "0xwallet1"));
        userPotRepository.save(createUserPot("0xcontract1", "0xwallet2"));
        userPotRepository.save(createUserPot("0xcontract2", "0xwallet3"));

        // Act
        List<UserPot> found = userPotRepository.findByContractAddress("0xcontract1");

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(up -> up.getContractAddress().equals("0xcontract1")));
    }

    @Test
    void deleteUserPot_shouldRemoveFromDatabase() {
        // Arrange
        UserPot userPot = createUserPot("0xcontract", "0xwallet");
        userPot = userPotRepository.save(userPot);
        String id = userPot.getId();

        // Act
        userPotRepository.delete(userPot);

        // Assert
        assertFalse(userPotRepository.findById(id).isPresent());
    }

    @Test
    void updateUserPot_shouldUpdateFields() {
        // Arrange
        UserPot userPot = createUserPot("0xcontract", "0xwallet");
        userPot = userPotRepository.save(userPot);
        LocalDateTime newTime = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS);

        // Act
        UserPot updatedPot = UserPot.builder()
                .id(userPot.getId())
                .contractAddress(userPot.getContractAddress())
                .walletAddress(userPot.getWalletAddress())
                .joinedAt(newTime)
                .build();
        userPotRepository.save(updatedPot);

        // Assert
        Optional<UserPot> found = userPotRepository.findById(userPot.getId());
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
        userPotRepository.deleteAll();
        userPotRepository.saveAll(List.of(
                createUserPot("0xcontract1", "0xwallet1"),
                createUserPot("0xcontract2", "0xwallet2"),
                createUserPot("0xcontract3", "0xwallet3")
        ));

        // Act & Assert
        assertEquals(3, userPotRepository.count());
    }

    @Test
    void batchOperations_shouldWorkCorrectly() {
        // Arrange
        userPotRepository.deleteAll();

        List<UserPot> userPots = List.of(
                createUserPot("0xcontract1", "0xwallet1"),
                createUserPot("0xcontract2", "0xwallet2"),
                createUserPot("0xcontract3", "0xwallet3")
        );

        // Act - saveAll
        userPotRepository.saveAll(userPots);

        // Assert
        assertEquals(3, userPotRepository.count());

        // Act - deleteAll
        userPotRepository.deleteAll();

        // Assert
        assertEquals(0, userPotRepository.count());
    }

    private UserPot createUserPot(String contractAddress, String walletAddress) {
        return UserPot.builder()
                .contractAddress(contractAddress)
                .walletAddress(walletAddress)
                .joinedAt(LocalDateTime.now())
                .build();
    }
}