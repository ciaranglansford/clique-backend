package com.clique.backend.data.response;

import com.clique.backend.model.Pot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetPotListResponse {
    private List<Pot> potList;
}
