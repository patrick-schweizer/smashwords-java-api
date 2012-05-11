package com.sw.access.retriever;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sw.access.OnOfflineTest;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.BookListRetriever;

public class BookListRetrieverTest extends OnOfflineTest {

    private enum SystemProperty {
        swUsername, swPassword
    }

    private BookListRetriever lib;

    @Before
    public void setUp() {
        super.setUp();
        String username = System.getProperty(SystemProperty.swUsername.name());
        String password = System.getProperty(SystemProperty.swPassword.name());
        if (this.online) {
            assertTrue("System property " + SystemProperty.swUsername + " is missing!",
                    username != null && !username.isEmpty());
            assertTrue("System property " + SystemProperty.swUsername + " is missing!",
                    password != null && !password.isEmpty());
        }
        LoginHelper login = new LoginHelper(pageLoader, username, password);
        lib = new BookListRetriever(this.pageLoader, login);
    }

    @Test
    public void testGetBooksFromLibary() throws IOException {
        List<Book> books = lib.getBooksFromLibary();
        assertTrue(books.size() > 0);
        Book book = books.get(0);
        assertEquals(0, book.getPriceInCent());

        book = books.get(1);
        assertEquals(99, book.getPriceInCent());
    }

    @Test
    public void testGetBooksFromAuthor() throws IOException {
        List<Book> books = lib.getBooksFromAuthor("UnleashYourAdventure");
        assertTrue(books.size() > 0);
    }

    @Test
    public void testGetBooksNewest() throws IOException {
        List<Book> books = lib.getBooksNewest();
        assertTrue(books.size() > 0);
    }
}
