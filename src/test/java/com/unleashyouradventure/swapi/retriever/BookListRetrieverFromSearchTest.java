package com.unleashyouradventure.swapi.retriever;

import com.unleashyouradventure.swapi.BookListValidator;
import com.unleashyouradventure.swapi.OnOfflineTest;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Log
public class BookListRetrieverFromSearchTest extends OnOfflineTest {

    private enum SystemProperty {
        swUsername, swPassword
    }

    private BookListRetriever lib;
    private BookCategory rootCategory = new BookCategory(1, "All");

    @Before
    public void setUp() throws Exception {
        super.setUp();
        String username = System.getProperty(SystemProperty.swUsername.name());
        String password = System.getProperty(SystemProperty.swPassword.name());
        if (this.online) {
            assertTrue("System property " + SystemProperty.swUsername + " is missing!", username != null && !username.isEmpty());
            assertTrue("System property " + SystemProperty.swUsername + " is missing!", password != null && !password.isEmpty());
        }
        Smashwords sw = new Smashwords(username, password, this.pageLoader);
        LoginHelper login = new LoginHelper(sw, username, password);
        lib = new BookListRetriever(this.pageLoader, login);
    }

    @Test
    public void shouldFindNoBooksFromEmptySearchPhrase() throws IOException {
        List<Book> books = lib.getBooksBySearch(PageLoader.PROGRESS_CALLBACK_DUMMY, "");
        assertTrue("There should be no books.", books.isEmpty());
    }

    @Test
    public void shouldFindBooksFromSearchPhrase() throws IOException {
        List<Book> books = lib.getBooksBySearch(PageLoader.PROGRESS_CALLBACK_DUMMY, "dog");
        assertEquals("There should be 10 books.", 10, books.size());
        new BookListValidator(books).validateBookFromListPage();
    }


}
