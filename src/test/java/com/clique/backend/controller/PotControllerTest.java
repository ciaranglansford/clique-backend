package com.clique.backend.controller;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.service.PotService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PotController.class)
class PotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PotService potService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPots_shouldReturnPotList() throws Exception {
        when(potService.getAllContractAddresses()).thenReturn(List.of("0x123", "0x456"));

        mockMvc.perform(get("/api/pots/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.potList[0]").value("0x123"))
                .andExpect(jsonPath("$.potList[1]").value("0x456"));
    }

    @Test
    void getAllPots_whenEmpty_shouldReturnEmptyList() throws Exception {
        when(potService.getAllContractAddresses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pots/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.potList").isArray())
                .andExpect(jsonPath("$.potList").isEmpty());
    }

    @Test
    void createPot_shouldReturnCreatedPot() throws Exception {
        Pot pot = new Pot();
        pot.setContractAddress("0x789");
        pot.setCreatedAt(LocalDateTime.now());

        when(potService.createPot(anyString())).thenReturn(pot);

        CreatePotRequest request = new CreatePotRequest();
        request.setContractAddress("0x789");

        mockMvc.perform(post("/api/pots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contractAddress").value("0x789"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createPot_whenAddressAlreadyExists_shouldReturnBadRequest() throws Exception {
        doThrow(new ApiException("Pot already exists with address: 0x789"))
                .when(potService).createPot(anyString());

        CreatePotRequest request = new CreatePotRequest();
        request.setContractAddress("0x789");

        mockMvc.perform(post("/api/pots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pot already exists with address: 0x789"));
    }

    @Test
    void createPot_whenAddressIsEmpty_shouldReturnBadRequest() throws Exception {
        doThrow(new ApiException("Contract address must not be empty"))
                .when(potService).createPot(anyString());

        CreatePotRequest request = new CreatePotRequest();
        request.setContractAddress("");

        mockMvc.perform(post("/api/pots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Contract address must not be empty"));
    }
}