package com.unleashyouradventure.swapi.cache;

import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.BookList;

/**
 * Caches books and book lists for performance and offline purposes. The cache
 * has the following characteristics:
 * <ul>
 * <li>It doesn't guarantee thread safety</li>
 * <li>a new added object overrides an older one if present, it does not merge
 * the objects.</li>
 * <ul>
 */
public interface Cache {
    Book getBook(long id);

    BookList getBooks(String url);

    void putBook(Book book);

    void putBooks(String url, BookList books);
}
