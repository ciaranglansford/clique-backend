package com.clique.backend.controller;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.PotEntry;
import com.clique.backend.service.PotEntryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PotEntryService potEntryService;

    @Test
    void getAllUserPots_shouldReturnPotList() throws Exception {
        PotContractList potContractList = new PotContractList(List.of("0xabc", "0xdef"));
        when(potEntryService.getAllPotEntries("wallet1")).thenReturn(potContractList);

        mockMvc.perform(get("/api/user/list")
                        .param("walletAddress", "wallet1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractAddresses[0]").value("0xabc"))
                .andExpect(jsonPath("$.contractAddresses[1]").value("0xdef"));
    }

    @Test
    void joinPot_shouldReturnJoinPotResponse() throws Exception {
        PotEntry potEntry = PotEntry.builder()
                .walletAddress("wallet1")
                .contractAddress("0xabc")
                .joinedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        when(potEntryService.joinPot(any(JoinPotRequest.class))).thenReturn(potEntry);

        String json = "{\"walletAddress\":\"wallet1\",\"contractAddress\":\"0xabc\"}";

        mockMvc.perform(post("/api/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletAddress").value("wallet1"))
                .andExpect(jsonPath("$.joinedAt").exists());
    }
}