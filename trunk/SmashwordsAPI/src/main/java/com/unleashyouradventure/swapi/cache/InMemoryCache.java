package com.unleashyouradventure.swapi.cache;

import java.util.HashMap;
import java.util.Map;

import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.LazyArrayList;

public class InMemoryCache implements Cache {

    private Map<Long, Book> books = new HashMap<Long, Book>();
    private Map<String, LazyArrayList<Book>> lists = new HashMap<String, LazyArrayList<Book>>();

    public Book getBook(long id) {
        return books.get(id);
    }

    public LazyArrayList<Book> getBooks(String url) {
        return lists.get(url);
    }

    public void putBook(Book book) {
        books.put(book.getId(), book);
    }

    public void putBooks(String url, LazyArrayList<Book> books) {
        lists.put(url, books);
        for (Book book : books) {
            putBook(book);
        }
    }
}
