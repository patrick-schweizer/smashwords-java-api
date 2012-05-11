package com.unleashyouradventure.swapi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unleashyouradventure.swapi.cache.Cache;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.retriever.BookListRetriever;
import com.unleashyouradventure.swapi.retriever.BookRetriever;

public class Smashwords {

    public final static String BASE_HOST = "smashwords.com";
    public final static String BASE_URL = "https://www." + BASE_HOST;

    private final static Logger log = Logger.getLogger(Smashwords.class.getName());
    private PageLoader loader;
    private LoginHelper login;
    private BookListRetriever bookList;
    private BookRetriever bookRetriever;

    public Smashwords(String username, String password) {
        this(username, password, new PageLoader());
    }

    public Smashwords(String username, String password, PageLoader loader) {
        this.loader = loader;
        login = new LoginHelper(loader, username, password);
        bookList = new BookListRetriever(loader, login);
        this.bookRetriever = new BookRetriever(loader, login);
    }

    /** Downloads and saves the file as a temporary file. */
    public File getFile(File parentFolder, String url) throws IOException {
        log.log(Level.FINE, "Looking for URL=" + url);
        login.loginIfNecessary();
        File file = loader.saveURLToFile(parentFolder, url);
        log.log(Level.FINE, "Saved file as " + file);
        return file;
    }

    public LoginHelper getLogin() {
        return login;
    }

    public BookListRetriever getBookListRetriever() {
        return bookList;
    }

    public BookRetriever getBookRetriever() {
        return bookRetriever;
    }

    public void setCache(Cache cache) {
        this.bookList.setCache(cache);
        this.bookRetriever.setCache(cache);
    }
}
