package com.unleashyouradventure.swapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SwPrices {
    private final String type = "FIXED_PRICE";
    private List<SwPrice> prices = new ArrayList<SwPrice>();

    public SwPrices() {
    }
}
