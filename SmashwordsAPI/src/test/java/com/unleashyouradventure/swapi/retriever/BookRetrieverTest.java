package com.unleashyouradventure.swapi.retriever;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.unleashyouradventure.swapi.OnOfflineTest;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.retriever.Book.ImageSize;

public class BookRetrieverTest extends OnOfflineTest {

    private enum SystemProperty {
        swUsername, swPassword
    }

    private BookRetriever lib;

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
        login.loginIfNecessary();
        assertTrue(login.isLoggedIn());
        lib = new BookRetriever(this.pageLoader, login);
    }

    @Test
    public void testGetBook() throws IOException {
        Book book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 208326);
        assertNotNull(book);
        assertNotNull(book.getAuthor());
        assertNotNull(book.getCoverUrl(ImageSize.thumb));
        assertNotNull(book.getDescriptionShort());
        assertNotNull(book.getPriceInCent());
        assertNotNull(book.getTitle());
        assertTrue(book.isBookOwned());
        // assertTrue(book.getRating() > 0);
    }

    @Test
    public void testGetBookLinks() throws IOException {

        // Direkt Download link
        Book book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 145431);
        assertNotNull(book);

        // Link to revisions
        book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 52);
        assertNotNull(book);
    }

}
