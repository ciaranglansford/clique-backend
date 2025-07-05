package com.clique.backend.controller;

import com.clique.backend.controller.dtoRequest.CreatePotRequest;
import com.clique.backend.controller.dtoRequest.JoinPotRequest;
import com.clique.backend.controller.dtoResponse.JoinPotResponse;
import com.clique.backend.model.Pot;
import com.clique.backend.model.UserPot;
import com.clique.backend.service.PotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pots")
public class PotController {

    private final PotService potService;

    public PotController(PotService potService) {
        this.potService = potService;
    }

    @PostMapping("/create")
    public ResponseEntity<Pot> createPot(@RequestBody CreatePotRequest request) {
        Pot pot = potService.createPot(request.getContractAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(pot);
    }

    @PostMapping("/join")
    public ResponseEntity<JoinPotResponse> joinPot(@RequestBody JoinPotRequest request) {
        UserPot userPot = potService.joinPot(request.getContractAddress(), request.getWalletAddress());
        return ResponseEntity.ok(new JoinPotResponse(userPot));
    }
}
