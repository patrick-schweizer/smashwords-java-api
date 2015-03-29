package com.unleashyouradventure.swapi.load;

import com.unleashyouradventure.swapi.LoggingHelper;
import com.unleashyouradventure.swapi.Smashwords;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginHelperTest {

    enum SystemProperty {
        swUsername, swPassword
    };

    @Test
    public void loginShouldWork() throws IOException {
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
