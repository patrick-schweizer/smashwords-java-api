package com.unleashyouradventure.swapi.cache;

import java.util.HashMap;
import java.util.Map;

import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.BookList;

public class InMemoryCache implements Cache {

    private Map<Long, Book> books = new HashMap<Long, Book>();
    private Map<String, BookList> lists = new HashMap<String, BookList>();
    int maxBooks = 500;
    int maxLists = 20;

    public InMemoryCache() {
        // use default values
    }

    public InMemoryCache(int maxBooks, int maxLists) {
        this.maxBooks = maxBooks;
        this.maxLists = maxLists;
    }

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

    /**
     * {@inheritDoc}
     */
    public void remove(String url) {
        lists.remove(url);
    }

    public void removeAllBookDetails() {
        for (Book book : this.books.values()) {
            book.setBookDetailsAdded(false);
        }
    }

    // private final static class CacheMap<K, V> extends LinkedHashMap<K, V> {
    //
    // public CacheMap(){
    // super(maxEntries + 1, 1, true);
    // }
    // protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    // return size() > MAX_ENTRIES;
    // }
    // }
}
