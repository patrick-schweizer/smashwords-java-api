package com.unleashyouradventure.swapi.load;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import com.unleashyouradventure.swapi.load.PageLoader;

public class PageLoaderTest {
    private File downloadFile;

    @Test
    public void testGetFile() throws IOException {
        // TODO
        downloadFile = new PageLoader().saveURLToFile(new File(System.getProperty("java.io.tmpdir")),
                "http://www.smashwords.com/books/download/131185/8/latest/0/1/happily-ever-after_10pct_sample.epub");
        assertNotNull(downloadFile);
        assertTrue(downloadFile.length() > 0);
    }

    @After
    public void tearDown() {
        downloadFile.deleteOnExit();
        downloadFile.delete();
    }
}
