package com.unleashyouradventure.swapi.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NullHelperTest {

    @Test
    public void getFirstNoneNullShouldReturnTheRightElement(){
        assertEquals("A", NullHelper.getFirstNoneNull(null, "A", "B"));
        assertEquals(null, NullHelper.getFirstNoneNull(null, null));
        assertEquals(null, NullHelper.getFirstNoneNull());
        assertEquals("A", NullHelper.getFirstNoneNull("A"));
    }

}
