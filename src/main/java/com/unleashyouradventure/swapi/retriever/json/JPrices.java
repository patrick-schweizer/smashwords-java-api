package com.unleashyouradventure.swapi.retriever.json;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JPrices {
    private final String type = "FIXED_PRICE";
    private List<JPrice> prices = new ArrayList<JPrice>();

    public JPrices() {
    }
}
