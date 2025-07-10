package com.clique.backend.controller;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.UserPot;
import com.clique.backend.service.UserPotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the UserPotController which manages the relationship between users and pots.
 */
@WebMvcTest(UserPotController.class)
class UserPotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserPotService userPotService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests retrieving users by contract address when users exist in the pot.
     */
    @Test
    void getUsersByContract_shouldReturnUserList() throws Exception {
        // Arrange
        String contractAddress = "0xabc123";
        List<UserPot> userPots = List.of(
                createUserPot("0xwallet1", contractAddress),
                createUserPot("0xwallet2", contractAddress)
        );
        when(userPotService.getUsersByContractAddress(contractAddress)).thenReturn(userPots);

        // Act & Assert
        mockMvc.perform(get("/api/user-pots/by-contract/{contractAddress}", contractAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].walletAddress").value("0xwallet1"))
                .andExpect(jsonPath("$[1].walletAddress").value("0xwallet2"));
    }

    /**
     * Tests retrieving users by contract address when no users are in the pot.
     */
    @Test
    void getUsersByContract_shouldReturnEmptyList() throws Exception {
        // Arrange
        String contractAddress = "0xempty";
        when(userPotService.getUsersByContractAddress(contractAddress)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/user-pots/by-contract/{contractAddress}", contractAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * Tests checking if a user is in a pot when the user is present.
     */
    @Test
    void isUserInPot_whenUserIsInPot_shouldReturnTrue() throws Exception {
        // Arrange
        String contractAddress = "0xabc123";
        String walletAddress = "0xwallet1";
        when(userPotService.isUserInPot(contractAddress, walletAddress)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/user-pots/check")
                        .param("contractAddress", contractAddress)
                        .param("walletAddress", walletAddress))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Tests checking if a user is in a pot when the user is not present.
     */
    @Test
    void isUserInPot_whenUserIsNotInPot_shouldReturnFalse() throws Exception {
        // Arrange
        String contractAddress = "0xabc123";
        String walletAddress = "0xwallet2";
        when(userPotService.isUserInPot(contractAddress, walletAddress)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/user-pots/check")
                        .param("contractAddress", contractAddress)
                        .param("walletAddress", walletAddress))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    /**
     * Tests removing a user from a pot successfully.
     */
    @Test
    void removeUserFromPot_shouldReturn204() throws Exception {
        // Arrange
        JoinPotRequest request = new JoinPotRequest();
        request.setContractAddress("0xabc123");
        request.setWalletAddress("0xwallet1");

        doNothing().when(userPotService).removeUserFromPot(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(delete("/api/user-pots/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(userPotService).removeUserFromPot(eq(request.getContractAddress()), eq(request.getWalletAddress()));
    }

    /**
     * Tests the error case when trying to remove a user who is not in the pot.
     */
    @Test
    void removeUserFromPot_whenUserNotInPot_shouldReturn400() throws Exception {
        // Arrange
        JoinPotRequest request = new JoinPotRequest();
        request.setContractAddress("0xabc123");
        request.setWalletAddress("0xwallet3");

        doThrow(new ApiException("User not found in this pot"))
                .when(userPotService).removeUserFromPot(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(delete("/api/user-pots/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found in this pot"));
    }

    /**
     * Helper method to create a UserPot entity for testing.
     */
    private UserPot createUserPot(String walletAddress, String contractAddress) {
        return UserPot.builder()
                .id("id-" + walletAddress)
                .walletAddress(walletAddress)
                .contractAddress(contractAddress)
                .joinedAt(LocalDateTime.now())
                .build();
    }
}