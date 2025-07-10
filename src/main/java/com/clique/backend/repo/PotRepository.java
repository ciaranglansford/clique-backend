package com.clique.backend.repo;

import com.clique.backend.model.Pot;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PotRepository extends MongoRepository<Pot, String> {
    // Custom query to find a Pot by its contract address
    Optional<Pot> findByContractAddress(String contractAddress);
}