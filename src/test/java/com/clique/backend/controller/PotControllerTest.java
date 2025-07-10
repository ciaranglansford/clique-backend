package com.clique.backend.controller;

import com.clique.backend.service.PotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PotController.class)
class PotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PotService potService;

    @Test
    void getAllPots_shouldReturnPotList() throws Exception {
        when(potService.getAllContractAddresses()).thenReturn(List.of("0x123", "0x456"));

        mockMvc.perform(get("/api/pots/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.potList[0]").value("0x123"))
                .andExpect(jsonPath("$.potList[1]").value("0x456"));
    }
}