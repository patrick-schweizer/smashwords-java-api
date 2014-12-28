package com.unleashyouradventure.swapi;

import java.util.logging.Logger;

public class LoggingHelper {
    public static void setLoggingAll() {
        Logger.getLogger("httpclient.wire.header").setLevel(java.util.logging.Level.FINEST);
        Logger.getLogger("httpclient.wire.content").setLevel(java.util.logging.Level.FINEST);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
    }
}
