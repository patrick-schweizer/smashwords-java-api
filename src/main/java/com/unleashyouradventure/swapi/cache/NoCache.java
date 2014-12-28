package com.unleashyouradventure.swapi.cache;

import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.BookList;

/** Dummy Cache, doesn't cache at all. */
public class NoCache implements Cache {

    public Book getBook(long id) {
        return null;
    }

    public BookList getBooks(String url) {
        return null;
    }

    public void putBook(Book book) {
        // Do nothing, since we don't cache at all
    }

    public void putBooks(String url, BookList books) {
        // Do nothing, since we don't cache at all
    }

    public void removeAllBookDetails() {
        // Do nothing, since we don't cache at all
    }

    public void remove(String url) {
        // Do nothing, since we don't cache at all
    }
}
