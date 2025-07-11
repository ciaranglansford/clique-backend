package com.clique.backend.repo;

import com.clique.backend.model.UserPot;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface UserPotRepository extends MongoRepository<UserPot, String> {
    // Find all UserPot documents by wallet address
    List<UserPot> findByWalletAddress(String walletAddress);

    // Find all UserPot documents by contract address
    List<UserPot> findByContractAddress(String contractAddress);
}