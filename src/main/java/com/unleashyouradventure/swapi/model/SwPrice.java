package com.unleashyouradventure.swapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SwPrice {
    private double amount;
    private String currency;

    public SwPrice() {
    }
}
