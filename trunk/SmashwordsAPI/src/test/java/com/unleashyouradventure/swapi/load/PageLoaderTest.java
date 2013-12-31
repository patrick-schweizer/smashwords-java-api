package com.unleashyouradventure.swapi.load;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.retriever.BookListRetriever.AdultContent;

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

    @Test
    public void testAdultContent() throws IOException {
        PageLoader loader = new PageLoader();

        // default
        String page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains("Currently hiding adult content in book lists and search results."));

        // on
        loader.setAdultContent(AdultContent.on);
        page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains("Currently showing adult content in book lists and search results."));

        // off
        loader.setAdultContent(AdultContent.off);
        page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains("Currently hiding adult content in book lists and search results."));

        // reset to default
        loader.setAdultContent(AdultContent.on);
        page = loader.getPage(Smashwords.BASE_URL); // Setting it on to be able to reset it in the next step
        assertTrue(page.contains("Currently showing adult content in book lists and search results."));
        loader.setAdultContent(AdultContent.swdefault);
        page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains("Currently hiding adult content in book lists and search results."));

    }

    @After
    public void tearDown() {
        if (downloadFile != null) {
            downloadFile.deleteOnExit();
            downloadFile.delete();
        }
    }
}
