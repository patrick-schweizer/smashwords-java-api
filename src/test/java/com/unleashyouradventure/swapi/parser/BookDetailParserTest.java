package com.unleashyouradventure.swapi.parser;

import com.unleashyouradventure.swapi.BookValidator;
import com.unleashyouradventure.swapi.model.SwBook;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class BookDetailParserTest {

    @Test
    public void allDetailsShouldBeFilled() throws IOException {
        SwBook book = new BookDetailParser().parseDetailPage(IOUtils.toString(new URL("https://www.smashwords.com/books/view/305").openStream()));
        new BookValidator(book).validateBookFromDetailPage();
    }
}
