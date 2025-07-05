package com.clique.backend.client;

import com.clique.backend.model.Pot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PotClient extends JpaRepository<Pot, Long>{
    Optional<Pot> findByContractAddress(String contractAddress);
}

