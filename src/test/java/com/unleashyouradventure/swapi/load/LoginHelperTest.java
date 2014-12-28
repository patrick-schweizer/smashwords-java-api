package com.unleashyouradventure.swapi.load;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.unleashyouradventure.swapi.LoggingHelper;
import com.unleashyouradventure.swapi.Smashwords;

public class LoginHelperTest {

    enum SystemProperty {
        swUsername, swPassword
    };

    @Test
    public void testLogin() throws IOException {
        LoggingHelper.setLoggingAll();
        Smashwords sw = new Smashwords(getSystemProperty(SystemProperty.swUsername), getSystemProperty(SystemProperty.swPassword));
        LoginHelper login = sw.getLogin();
        assertTrue(login.logIn());
    }

    private String getSystemProperty(SystemProperty propertyName) {
        String value = System.getProperty(propertyName.name());
        assertNotNull("System Property " + propertyName + " must be set for this test.", value);
        return value;
    }

}
