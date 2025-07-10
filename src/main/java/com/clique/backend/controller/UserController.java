package com.clique.backend.controller;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.data.response.GetPotListResponse;
import com.clique.backend.data.response.JoinPotResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import com.clique.backend.service.UserPotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user operations related to pots.
 * Provides endpoints for retrieving a user's pots and joining pots.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserPotService userPotService;

    /**
     * Retrieves all pots that a user has joined.
     *
     * @param walletAddress The wallet address of the user
     * @return A list of contract addresses for pots the user has joined
     * @throws ApiException if the wallet address is invalid
     */
    @GetMapping("/list")
    public ResponseEntity<GetPotListResponse> getAllUserPots(@RequestParam String walletAddress) {
        PotContractList potContractList = userPotService.getAllUserPots(walletAddress);
        return ResponseEntity.ok(new GetPotListResponse(potContractList));
    }

    /**
     * Adds a user to a pot.
     *
     * @param request The request containing contract address and wallet address
     * @return Details of the newly created user-pot relationship
     * @throws ApiException if the user is already in the pot or if addresses are invalid
     */
    @PostMapping("/join")
    public ResponseEntity<JoinPotResponse> joinPot(@RequestBody JoinPotRequest request) {
        UserPot userPot = userPotService.joinPot(request);
        return ResponseEntity.ok(new JoinPotResponse(userPot));
    }
}