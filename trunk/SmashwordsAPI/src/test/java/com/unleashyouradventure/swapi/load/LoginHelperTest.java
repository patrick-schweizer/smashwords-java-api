package com.unleashyouradventure.swapi.load;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import com.unleashyouradventure.swapi.Smashwords;

public class LoginHelperTest {

    enum SystemProperty {
        swUser, swPassword
    };

    @Test
    public void testLogin() throws IOException {
        setLogging();
        Smashwords sw = new Smashwords(getSystemProperty(SystemProperty.swUser),
                getSystemProperty(SystemProperty.swPassword));
        LoginHelper login = sw.getLogin();
        assertTrue(login.logIn());
    }

    private String getSystemProperty(SystemProperty propertyName) {
        String value = System.getProperty(propertyName.name());
        assertNotNull("System Property " + propertyName + " must be set for this test.", value);
        return value;
    }

    private void setLogging() {
        Logger.getLogger("httpclient.wire.header").setLevel(java.util.logging.Level.FINEST);
        Logger.getLogger("httpclient.wire.content").setLevel(java.util.logging.Level.FINEST);

        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");

    }
}
