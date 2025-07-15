package com.clique.backend.controller;

import com.clique.backend.exception.ErrorResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.PotEntry;
import com.clique.backend.service.PotEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user-pot relationships.
 * Provides endpoints for retrieving users in a pot, removing users from pots,
 * and checking if a user is in a specific pot.
 */
@RestController
@RequestMapping("/api/user-pots")
@RequiredArgsConstructor
public class PotEntryController {

    private final PotEntryService potEntryService;

    /**
     * Retrieves all users who have joined a specific pot.
     *
     * @param contractAddress The address of the pot contract
     * @return List of UserPot objects representing users in the pot
     * @throws ApiException if the contract address is invalid or the pot doesn't exist
     */
    @GetMapping("/by-contract/{contractAddress}")
    public ResponseEntity<?> getUsersByContract(@PathVariable String contractAddress) {
        try {
            List<PotEntry> potEntries = potEntryService.getUsersByContractAddress(contractAddress);
            return ResponseEntity.ok(potEntries);
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
        }
    }

    /**
     * Checks if a user is currently in a specific pot.
     *
     * @param contractAddress The address of the pot contract
     * @param walletAddress   The user's wallet address
     * @return Boolean indicating whether the user is in the pot
     * @throws ApiException if either address is invalid
     */
    @GetMapping("/check")
    public ResponseEntity<?> isUserInPot(@RequestParam String contractAddress,
                                         @RequestParam String walletAddress) {
        try {
            boolean isInPot = potEntryService.isUserInPot(contractAddress, walletAddress);
            return ResponseEntity.ok(isInPot);
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
        }
    }

    /**
     * Removes a user from a pot.
     *
     * @param contractAddress The address of the pot contract
     * @param walletAddress   The user's wallet address
     * @return Empty response with HTTP 204 No Content status
     * @throws ApiException if the user is not in the pot or addresses are invalid
     */
    @DeleteMapping("/{contractAddress}/users/{walletAddress}")
    public ResponseEntity<?> removeUserFromPot(@PathVariable String contractAddress,
                                               @PathVariable String walletAddress) {
        try {
            potEntryService.removeUserFromPot(contractAddress, walletAddress);
            return ResponseEntity.noContent().build();
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
        }
    }
}
