package com.unleashyouradventure.swapi.parser;

import com.unleashyouradventure.swapi.model.SwBook;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Created by patrick on 29.12.14.
 */

public class BookDetailParserTest {

    @Test
    public void allDetailsShouldBeFilled() throws IOException {
        SwBook book = new BookDetailParser().parseDetailPage(IOUtils.toString(new URL("https://www.smashwords.com/books/view/305").openStream()));
        assertNotNull(book);
    }
}
