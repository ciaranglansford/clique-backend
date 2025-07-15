package com.clique.backend.controller;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.response.CreatePotResponse;
import com.clique.backend.exception.ErrorResponse;
import com.clique.backend.data.response.GetPotListResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.service.PotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing pots.
 * Provides endpoints for retrieving all pots and creating new pots.
 */
@RestController
@RequestMapping("/api/pots")
@RequiredArgsConstructor
@Validated
public class PotController {

    private static final Logger logger = LoggerFactory.getLogger(PotController.class);

    private final PotService potService;

    /**
     * Retrieves a list of all pot contract addresses.
     *
     * @return A list of all pot contract addresses
     */
    @Operation(summary = "Get all pot contract addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<GetPotListResponse> getAllPots() {
        logger.info("Fetching all pot contract addresses");
        try {
            List<Pot> potList = potService.getAllPots();
            GetPotListResponse response = new GetPotListResponse(potList);

            logger.info("Successfully retrieved {} pot contract addresses", potList.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching pot contract addresses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new pot with the given pot details
     *
     * @param request The request containing the pot details
     * @return The newly created pot
     * @throws ApiException if a pot with the given address already exists or if the address is invalid
     */
    @Operation(summary = "Create a new pot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pot created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Pot already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createPot(@Valid @RequestBody CreatePotRequest request) {
        logger.info("Creating pot with contract address: {}", request.getContractAddress());
        try {
            CreatePotResponse pot = potService.createPot(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(pot);
        } catch (ApiException e) {
            logger.warn("API exception: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("CONFLICT", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating pot", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "Internal server error"));
        }
    }
}
