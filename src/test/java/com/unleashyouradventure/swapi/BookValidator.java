package com.unleashyouradventure.swapi;

import com.unleashyouradventure.swapi.model.ImageSize;
import com.unleashyouradventure.swapi.model.SwBook;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
public class BookValidator {
    private final SwBook book;

    public void validateBookFromListPage() {
        assertNotNull("Expected a non null Book", book);
        assertTrue("Expected short description", StringUtils.isNotEmpty(book.getShort_description()));
        assertTrue("Expected author name", StringUtils.isNotEmpty(book.getFirstAuthorDisplayNameOrNull()));
        assertNotNull(book.getPriceInCent());
        assertNotNull(book.getTitle());
        assertNotNull(book.getCover_url(ImageSize.thumb));
    }

    public void validateBookFromDetailPage() {
        validateBookFromListPage(); // we expect everything that that was parsed from list page as well

    }
}
