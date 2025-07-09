package com.clique.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class PotContractList {

    private List<String> contractAddresses;

    public PotContractList(List<String> contractAddresses) {
        this.contractAddresses = contractAddresses;
    }
}
