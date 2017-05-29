package com.unleashyouradventure.swapi.load;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.retriever.BookListRetriever.AdultContent;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PageLoaderTest {
    private File downloadFile;

    private final static String ADULT_HIDING_MESSAGE = "Currently hiding all adult content in book lists and search results.";
    private final static String ADULT_SHOW_MESSAGE = "Currently showing adult content, but not erotica, in book lists and search results.";

    @Test
    public void testGetFile() throws IOException {
        // TODO
        downloadFile = new PageLoader().saveURLToFile(new File(System.getProperty("java.io.tmpdir")),
                "http://www.smashwords.com/books/download/305/8/latest/0/0/smashwords-book-marketing-guide.epub");
        assertNotNull(downloadFile);
        assertTrue(downloadFile.length() > 0);
    }

    @Test
    public void testAdultContent() throws IOException {
        PageLoader loader = new PageLoader();

        // default
        String page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains(ADULT_HIDING_MESSAGE));

        // on
        loader.setAdultContent(AdultContent.on);
        page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains(ADULT_SHOW_MESSAGE));

        // off
        loader.setAdultContent(AdultContent.off);
        page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains(ADULT_HIDING_MESSAGE));

        // reset to default
        loader.setAdultContent(AdultContent.on);
        page = loader.getPage(Smashwords.BASE_URL); // Setting it on to be able to reset it in the next step
        assertTrue(page.contains(ADULT_SHOW_MESSAGE));
        loader.setAdultContent(AdultContent.swdefault);
        page = loader.getPage(Smashwords.BASE_URL);
        assertTrue(page.contains(ADULT_HIDING_MESSAGE));

    }

    @After
    public void tearDown() {
        if (downloadFile != null) {
            downloadFile.deleteOnExit();
            downloadFile.delete();
        }
    }
}
