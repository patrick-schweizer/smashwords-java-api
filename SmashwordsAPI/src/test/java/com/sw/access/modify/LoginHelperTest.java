package com.sw.access.modify;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;

public class LoginHelperTest {

    @Test
    public void testLogin() throws IOException {
        setLogging();
        LoginHelper login = new LoginHelper(new PageLoader(), "patrick@schweizer-ing.com", "StmFoPnf");
        assertTrue(login.logIn());
    }

    private void setLogging() {
        Logger.getLogger("httpclient.wire.header").setLevel(java.util.logging.Level.FINEST);
        Logger.getLogger("httpclient.wire.content").setLevel(java.util.logging.Level.FINEST);

        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
        // System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers",
        // "debug");

    }
}
