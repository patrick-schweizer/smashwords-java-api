package com.unleashyouradventure.swapi.retriever.json;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class JPrices {
    private String type = "FIXED_PRICE";
    private List<JPrice> prices = new ArrayList<JPrice>();
}
