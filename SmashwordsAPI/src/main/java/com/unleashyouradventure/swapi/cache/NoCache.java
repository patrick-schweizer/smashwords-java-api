package com.unleashyouradventure.swapi.cache;

import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.LazyArrayList;

/** Dummy Cache, doesn't cache at all. */
public class NoCache implements Cache {

    public Book getBook(long id) {
        return null;
    }

    public LazyArrayList<Book> getBooks(String url) {
        return null;
    }

    public void putBook(Book book) {
        // Do nothing, since we don't cache at all
    }

    public void putBooks(String url, LazyArrayList<Book> books) {
        // Do nothing, since we don't cache at all
    }
}
