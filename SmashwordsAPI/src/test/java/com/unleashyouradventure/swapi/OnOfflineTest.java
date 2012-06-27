package com.unleashyouradventure.swapi;

import com.unleashyouradventure.swapi.load.PageLoader;

public class OnOfflineTest {

    enum SystemProperty {
        testOnline
    }

    protected PageLoader pageLoader;
    protected boolean online = false;

    public void setUp() {
        // online or offline test?
        if ("true".equals(System.getProperty(SystemProperty.testOnline.name()))) {
            this.pageLoader = new PageLoader();
            this.online = true;
        } else {
            this.pageLoader = new PageLoaderFromFile();
        }
    }
}
