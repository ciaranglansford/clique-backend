package com.clique.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pots")
public class Pot {
    @Id
    @Column(name = "contract_address", nullable = false, unique = true)
    private String contractAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "pot", cascade = CascadeType.ALL)
    private List<UserPot> userPots = new ArrayList<>();
}
