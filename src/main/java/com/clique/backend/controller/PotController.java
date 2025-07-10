package com.clique.backend.controller;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.response.GetPotListResponse;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.service.PotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pots")
@RequiredArgsConstructor
public class PotController {

    private final PotService potService;

    @GetMapping("/all")
    public ResponseEntity<GetPotListResponse> getAllPots() {
        List<String> contractAddresses = potService.getAllContractAddresses();
        return ResponseEntity.ok(new GetPotListResponse(new PotContractList(contractAddresses)));
    }

    @PostMapping("/create")
    public ResponseEntity<Pot> createPot(@RequestBody CreatePotRequest request) {
        Pot pot = potService.createPot(request.getContractAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(pot);
    }
}
