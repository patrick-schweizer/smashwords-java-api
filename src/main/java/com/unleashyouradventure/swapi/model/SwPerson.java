package com.unleashyouradventure.swapi.model;

import lombok.Data;

@Data
public class SwPerson {
    private SwAccount account;
    private String role;
}
