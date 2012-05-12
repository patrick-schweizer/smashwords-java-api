package com.unleashyouradventure.swapi.cache;

import java.util.HashMap;
import java.util.Map;

import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.BookList;

public class InMemoryCache implements Cache {

    private Map<Long, Book> books = new HashMap<Long, Book>();
    private Map<String, BookList> lists = new HashMap<String, BookList>();

    public Book getBook(long id) {
        return books.get(id);
    }

    public BookList getBooks(String url) {
        return lists.get(url);
    }

    public void putBook(Book book) {
        books.put(book.getId(), book);
    }

    public void putBooks(String url, BookList books) {
        lists.put(url, books);
        for (Book book : books) {
            putBook(book);
        }
    }
}
