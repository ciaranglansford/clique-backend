package com.clique.backend.controller;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.response.GetPotListResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.model.PotContractList;
import com.clique.backend.service.PotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing pots.
 * Provides endpoints for retrieving all pots and creating new pots.
 */
@RestController
@RequestMapping("/api/pots")
@RequiredArgsConstructor
public class PotController {

    private final PotService potService;

    /**
     * Retrieves a list of all pot contract addresses.
     *
     * @return A list of all pot contract addresses
     */
    @GetMapping("/all")
    public ResponseEntity<GetPotListResponse> getAllPots() {
        List<String> contractAddresses = potService.getAllContractAddresses();
        return ResponseEntity.ok(new GetPotListResponse(new PotContractList(contractAddresses)));
    }

    /**
     * Creates a new pot with the given contract address.
     *
     * @param request The request containing the pot contract address
     * @return The newly created pot
     * @throws ApiException if a pot with the given address already exists or if the address is invalid
     */
    @PostMapping("/create")
    public ResponseEntity<Pot> createPot(@RequestBody CreatePotRequest request) {
        Pot pot = potService.createPot(request.getContractAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(pot);
    }
}
