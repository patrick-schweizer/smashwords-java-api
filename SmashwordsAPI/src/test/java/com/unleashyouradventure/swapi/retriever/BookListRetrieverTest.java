package com.unleashyouradventure.swapi.retriever;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.unleashyouradventure.swapi.OnOfflineTest;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.retriever.BookListRetriever.Length;
import com.unleashyouradventure.swapi.retriever.BookListRetriever.Price;
import com.unleashyouradventure.swapi.retriever.BookListRetriever.Sortby;

public class BookListRetrieverTest extends OnOfflineTest {

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
    public void testGetBooksFromLibary() throws IOException {
        List<Book> books = lib.getBooksFromLibary(PageLoader.PROGRESS_CALLBACK_DUMMY);
        assertTrue(books.size() > 0);
        Book book = books.get(0);
        assertEquals(299, book.getPriceInCent());

        book = books.get(1);
        assertEquals(0, book.getPriceInCent());
    }

    @Test
    public void testGetBooksFromAuthor() throws IOException {
        List<Book> books = lib.getBooksFromAuthor(PageLoader.PROGRESS_CALLBACK_DUMMY, "UnleashYourAdventure");
        assertTrue(books.size() > 0);
        assertTrue(books.get(0).getDescriptionShort().length() > 0);
    }

    @Test
    public void testGetBooksByParameters() throws IOException {
        for (Sortby sortby : Sortby.values()) {
            for (Price price : Price.values()) {
                for (Length length : Length.values()) {
                    List<Book> books = lib.getBooksByCategory(PageLoader.PROGRESS_CALLBACK_DUMMY, rootCategory, sortby, price, length);
                    assertTrue(books.size() > 0 || isException(sortby, price, length));
                }
            }
        }
    }

    private boolean isException(Sortby sortby, Price price, Length length) {
        return Sortby.unitssold == sortby && price == Price.free; // Smashwords doesn't give a result for this
    }

    @Test
    public void testGetNext() throws IOException {
        BookList books = lib.getBooksByCategory(PageLoader.PROGRESS_CALLBACK_DUMMY, rootCategory, Sortby.newest, Price.anyPrice, Length.any);
        assertTrue(books.hasMoreElementsToLoad());
    }
}
