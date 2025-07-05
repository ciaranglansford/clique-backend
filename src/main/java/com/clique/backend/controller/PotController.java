package com.clique.backend.controller;

import com.clique.backend.controller.dtoRequest.CreatePotRequest;
import com.clique.backend.controller.dtoRequest.JoinPotRequest;
import com.clique.backend.controller.dtoResponse.JoinPotResponse;
import com.clique.backend.model.Pot;
import com.clique.backend.model.UserPot;
import com.clique.backend.service.PotService;
import com.clique.backend.service.UserPotService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pots")
public class PotController {

    @Autowired
    private PotService potService;
    @Autowired
    private UserPotService userPotService;

    @PostMapping("/create")
    public ResponseEntity<Pot> createPot(@RequestBody CreatePotRequest request) {
        Pot pot = potService.createPot(request.getContractAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(pot);
    }


}
