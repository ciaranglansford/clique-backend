package com.clique.backend.controller;

import com.clique.backend.exception.ApiException;
import com.clique.backend.model.PotEntry;
import com.clique.backend.service.PotEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(PotEntryController.class)
class PotEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PotEntryService potEntryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUsersByContract_shouldReturnUserList() throws Exception {
        String contractAddress = "0xabc123";
        List<PotEntry> potEntries = List.of(
                createUserPot("0xwallet1", contractAddress),
                createUserPot("0xwallet2", contractAddress)
        );
        when(potEntryService.getUsersByContractAddress(contractAddress)).thenReturn(potEntries);

        mockMvc.perform(get("/api/user-pots/by-contract/{contractAddress}", contractAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].walletAddress").value("0xwallet1"))
                .andExpect(jsonPath("$[1].walletAddress").value("0xwallet2"));
    }

    @Test
    void getUsersByContract_shouldReturnEmptyList() throws Exception {
        String contractAddress = "0xempty";
        when(potEntryService.getUsersByContractAddress(contractAddress)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/user-pots/by-contract/{contractAddress}", contractAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void isUserInPot_whenUserIsInPot_shouldReturnTrue() throws Exception {
        String contractAddress = "0xabc123";
        String walletAddress = "0xwallet1";
        when(potEntryService.isUserInPot(contractAddress, walletAddress)).thenReturn(true);

        mockMvc.perform(get("/api/user-pots/check")
                        .param("contractAddress", contractAddress)
                        .param("walletAddress", walletAddress))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isUserInPot_whenUserIsNotInPot_shouldReturnFalse() throws Exception {
        String contractAddress = "0xabc123";
        String walletAddress = "0xwallet2";
        when(potEntryService.isUserInPot(contractAddress, walletAddress)).thenReturn(false);

        mockMvc.perform(get("/api/user-pots/check")
                        .param("contractAddress", contractAddress)
                        .param("walletAddress", walletAddress))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void removeUserFromPot_shouldReturn204() throws Exception {
        String contractAddress = "0xabc123";
        String walletAddress = "0xwallet1";

        doNothing().when(potEntryService).removeUserFromPot(anyString(), anyString());

        mockMvc.perform(delete("/api/user-pots/{contractAddress}/users/{walletAddress}",
                        contractAddress, walletAddress))
                .andExpect(status().isNoContent());

        verify(potEntryService).removeUserFromPot(eq(contractAddress), eq(walletAddress));
    }

    @Test
    void removeUserFromPot_whenUserNotInPot_shouldReturn400() throws Exception {
        String contractAddress = "0xabc123";
        String walletAddress = "0xwallet3";

        doThrow(new ApiException("User not found in this pot"))
                .when(potEntryService).removeUserFromPot(eq(contractAddress), eq(walletAddress));

        mockMvc.perform(delete("/api/user-pots/{contractAddress}/users/{walletAddress}",
                        contractAddress, walletAddress))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found in this pot"));
    }

    private PotEntry createUserPot(String walletAddress, String contractAddress) {
        return PotEntry.builder()
                .id("id-" + walletAddress)
                .walletAddress(walletAddress)
                .contractAddress(contractAddress)
                .joinedAt(LocalDateTime.now())
                .build();
    }
}