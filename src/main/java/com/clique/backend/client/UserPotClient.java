package com.clique.backend.client;

import com.clique.backend.model.UserPot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPotClient extends JpaRepository<UserPot, Long> {
    List<UserPot> findByWalletAddress(String walletAddress);
}
