package com.clique.backend.repo;

import com.clique.backend.model.Pot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PotRepository extends JpaRepository<Pot, Long>{
    Optional<Pot> findByContractAddress(String contractAddress);
}

