package com.clique.backend.controller;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.model.Pot;
import com.clique.backend.service.PotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pots")
@RequiredArgsConstructor
public class PotController {

    private final PotService potService;

    @PostMapping("/create")
    public ResponseEntity<Pot> createPot(@RequestBody CreatePotRequest request) {
        Pot pot = potService.createPot(request.getContractAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(pot);
    }
}
