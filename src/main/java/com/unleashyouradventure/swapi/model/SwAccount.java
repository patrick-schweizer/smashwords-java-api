package com.unleashyouradventure.swapi.model;

import lombok.Data;

@Data
public class SwAccount {

    private long id;
    private String username;
    private boolean is_corp;
    private String display_name;
}
