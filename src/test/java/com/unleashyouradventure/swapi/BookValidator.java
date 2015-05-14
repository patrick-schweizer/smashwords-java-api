package com.unleashyouradventure.swapi;

import com.unleashyouradventure.swapi.retriever.Book;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
public class BookValidator {
    private final Book book;

    public void validateBookFromListPage() {
        assertTrue("Expected short descriptionh", StringUtils.isNotEmpty(book.getShort_description()));
    }

    public void validateBookFromDetailPage() {

    }
}
