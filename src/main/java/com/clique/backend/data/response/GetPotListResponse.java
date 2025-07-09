package com.clique.backend.data.response;

import com.clique.backend.model.PotContractList;
import lombok.Data;

import java.util.List;

@Data
public class GetPotListResponse {
    private List<String> potList;

    public GetPotListResponse(PotContractList potContractList){
        this.potList = potContractList.getContractAddresses();
    }
}
