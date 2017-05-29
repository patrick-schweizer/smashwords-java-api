package com.unleashyouradventure.swapi.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SwSeries implements Serializable {
    private long id;
    private String name;
    private long number;
}
