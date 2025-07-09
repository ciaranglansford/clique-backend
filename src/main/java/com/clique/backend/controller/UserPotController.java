package com.clique.backend.controller;

import com.clique.backend.controller.dtoRequest.GetUserPotRequest;
import com.clique.backend.controller.dtoRequest.JoinPotRequest;
import com.clique.backend.controller.dtoResponse.GetPotListResponse;
import com.clique.backend.controller.dtoResponse.JoinPotResponse;
import com.clique.backend.model.PotContractList;
import com.clique.backend.model.UserPot;
import com.clique.backend.service.UserPotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserPotController {

    @Autowired
    UserPotService userPotService;

    @GetMapping("/list")
    public ResponseEntity<GetPotListResponse> getAllUserPots(@RequestParam String walletAddress) {
        PotContractList potContractList = userPotService.getAllUserPots(walletAddress);
        return ResponseEntity.ok(new GetPotListResponse(potContractList));
    }

    @PostMapping("/join")
    public ResponseEntity<JoinPotResponse> joinPot(@RequestBody JoinPotRequest request) {
        UserPot userPot = userPotService.joinPot(request.getContractAddress(), request.getWalletAddress());
        return ResponseEntity.ok(new JoinPotResponse(userPot));
    }
}
