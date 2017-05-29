package com.unleashyouradventure.swapi.retriever;

import com.unleashyouradventure.swapi.BookListValidator;
import com.unleashyouradventure.swapi.OnOfflineTest;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.model.SwAccount;
import com.unleashyouradventure.swapi.model.SwPerson;
import com.unleashyouradventure.swapi.model.SwSeries;
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

    @Test
    public void shouldFindBooksBySeries() throws IOException {
        SwSeries series = new SwSeries();
        series.setId(4289);
        List<Book> books = lib.getBooksBySeries(PageLoader.PROGRESS_CALLBACK_DUMMY, series);
        assertEquals("There should be 5 books by series.", 5, books.size());
        new BookListValidator(books).validateBookFromListPage();
    }

    @Test
    public void shouldFindBooksByAuthor() throws IOException {
        SwAccount account = new SwAccount();
        account.setUsername("mc");
        SwPerson person = new SwPerson();
        person.setAccount(account);
        List<Book> books = lib.getBooksByAuthor(PageLoader.PROGRESS_CALLBACK_DUMMY, person);
        assertEquals("There should be 16 books by author.", 16, books.size());
        new BookListValidator(books).validateBookFromListPage();
    }

}
