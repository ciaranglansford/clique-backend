package com.clique.backend.repo;

import com.clique.backend.model.PotEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PotEntryRepository extends MongoRepository<PotEntry, String> {
    // Find all UserPot documents by wallet address
    List<PotEntry> findByWalletAddress(String walletAddress);

    // Find all UserPot documents by contract address
    List<PotEntry> findByContractAddress(String contractAddress);
}