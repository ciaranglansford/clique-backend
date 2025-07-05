package com.clique.backend.controller.dtoResponse;

import com.clique.backend.model.PotContractList;

import java.util.List;

public class GetPotListResponse {
    private List<String> potList;

    public GetPotListResponse(PotContractList potContractList){
        this.potList = potContractList.getContractAddresses();
    }

    public List<String> getPotList() {
        return potList;
    }
}
