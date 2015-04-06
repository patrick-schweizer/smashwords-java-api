package com.unleashyouradventure.swapi.util;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseUtilsTest {

    @Test
    public void priceParserShouldWork (){
        assertEquals("Comma is not interpreted correctly",  Double.valueOf(10000.23), ParseUtils.parsePrice("10,000.23"));
    }
}
