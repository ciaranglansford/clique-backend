package com.clique.backend.Repository;

import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPotRepository extends JpaRepository<UserPot, Long> {
    List<UserPot> findByWalletAddress(String walletAddress);

    @Query("SELECT u.pot.contractAddress FROM UserPot u WHERE u.walletAddress = :walletAddress")
    List<String> findContractAddressesByWalletAddress(String walletAddress);
}
