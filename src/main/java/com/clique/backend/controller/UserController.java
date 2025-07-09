package com.clique.backend.controller;

import com.clique.backend.data.request.JoinPotRequest;
import com.clique.backend.data.response.GetPotListResponse;
import com.clique.backend.data.response.JoinPotResponse;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import com.clique.backend.service.UserPotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserPotService userPotService;

    @GetMapping("/list")
    public ResponseEntity<GetPotListResponse> getAllUserPots(@RequestParam String walletAddress) {
        PotContractList potContractList = userPotService.getAllUserPots(walletAddress);
        return ResponseEntity.ok(new GetPotListResponse(potContractList));
    }

    @PostMapping("/join")
    public ResponseEntity<JoinPotResponse> joinPot(@RequestBody JoinPotRequest request) {
        UserPot userPot = userPotService.joinPot(request);
        return ResponseEntity.ok(new JoinPotResponse(userPot));
    }
}
