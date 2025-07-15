package com.clique.backend.controller;

import com.clique.backend.data.request.CreatePotRequest;
import com.clique.backend.data.response.CreatePotResponse;
import com.clique.backend.exception.ApiException;
import com.clique.backend.model.Pot;
import com.clique.backend.service.PotService;
import com.clique.backend.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.clique.backend.util.TestUtil.*;
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
        List<Pot> pots = List.of(
                Pot.builder().contractAddress("0x123").build(),
                Pot.builder().contractAddress("0x456").build()
        );

        when(potService.getAllPots()).thenReturn(pots);

        mockMvc.perform(get("/api/pots/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.potList[0].contractAddress").value("0x123"))
                .andExpect(jsonPath("$.potList[1].contractAddress").value("0x456"));
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
        CreatePotRequest request = TestUtil.getCreatePotRequest();
        CreatePotResponse response = TestUtil.getCreatePotResponse();
        when(potService.createPot(request)).thenReturn(response);

        mockMvc.perform(post("/api/pots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contractAddress").value(CONTRACT_ADDRESS))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createPot_whenAddressAlreadyExists_shouldReturnBadRequest() throws Exception {
        CreatePotRequest request = getCreatePotRequest();

        doThrow(new ApiException("Pot already exists with address: 0x123"))
                .when(potService).createPot(request);

        mockMvc.perform(post("/api/pots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Pot already exists with address: 0x123"));
    }

    @Test
    void createPot_whenAddressIsEmpty_shouldReturnBadRequest() throws Exception {
        CreatePotRequest request = getCreatePotRequest();

        doThrow(new ApiException("Contract address must not be empty"))
                .when(potService).createPot(request);

        mockMvc.perform(post("/api/pots/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Contract address must not be empty"));
    }
}