package com.unleashyouradventure.swapi.model;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class SwBookTest {


    @Test
    public void getFirstAuthorDisplayNameShouldNotThrowNullpointer(){
        SwPerson person = new SwPerson();
        SwBook book = new SwBook();
        book.addContributor(person);
        assertNull("Expected null as display name since the author was not set in this test.",
                book.getFirstAuthorDisplayNameOrNull()); // should not throw Nullpointer
    }
}
