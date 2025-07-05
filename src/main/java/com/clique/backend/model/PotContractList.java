package com.clique.backend.model;

import java.util.List;

public class PotContractList {
    private List<String> contractAddresses;

    public PotContractList() {
    }

    public PotContractList(List<String> contractAddresses) {
        this.contractAddresses = contractAddresses;
    }

    public List<String> getContractAddresses() {
        return contractAddresses;
    }

    public void setContractAddresses(List<String> contractAddresses) {
        this.contractAddresses = contractAddresses;
    }
}
