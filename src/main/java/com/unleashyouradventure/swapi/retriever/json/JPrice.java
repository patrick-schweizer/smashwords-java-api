package com.unleashyouradventure.swapi.retriever.json;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JPrice {
    private double amount;
    private String currency;

    public JPrice() {
    }
}
