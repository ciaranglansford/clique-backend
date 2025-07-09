package com.clique.backend.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "user_pots")
public class UserPot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_address", nullable = false)
    private String walletAddress;

    @ManyToOne
    @JoinColumn(name = "contract_address", referencedColumnName = "contract_address", nullable = false)
    private Pot pot;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();


}
