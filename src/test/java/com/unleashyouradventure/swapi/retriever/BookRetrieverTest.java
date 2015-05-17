package com.unleashyouradventure.swapi.retriever;

import com.unleashyouradventure.swapi.BookValidator;
import com.unleashyouradventure.swapi.LoggingHelper;
import com.unleashyouradventure.swapi.OnOfflineTest;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.retriever.Book.FileType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookRetrieverTest extends OnOfflineTest {

    private enum SystemProperty {
        swUsername, swPassword
    }

    private BookRetriever lib;
    private Smashwords sw;
    private LoginHelper login;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        LoggingHelper.setLoggingAll();
        String username = System.getProperty(SystemProperty.swUsername.name());
        String password = System.getProperty(SystemProperty.swPassword.name());
        if (this.online) {
            assertTrue("System property " + SystemProperty.swUsername + " is missing!", username != null && !username.isEmpty());
            assertTrue("System property " + SystemProperty.swUsername + " is missing!", password != null && !password.isEmpty());
        }

        sw = new Smashwords(username, password, this.pageLoader);
        login = new LoginHelper(sw, username, password);
        login.loginIfNecessary();
        assertTrue(login.isLoggedIn());
        lib = new BookRetriever(this.pageLoader, login);
    }

    @Test
    public void testGetBook() throws IOException {
        Book book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 208326);
        new BookValidator(book).validateBookFromDetailPage();
        assertTrue("Expected book to be owned", book.isBookOwned());
        // assertTrue(book.getRating() > 0);
    }

    @Test
    public void testGetBookLinks() throws IOException {

        // Direkt Download link
        Book book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 145431);
        assertNotNull(book.getDownloadLinkForNewestVersion(FileType.Epub));

        // Direkt Download link for bought books
        book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 109660);
        assertNotNull(book.getDownloadLinkForNewestVersion(FileType.Epub));

        // Direkt Download link for books with price
        book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 120327);
        assertNotNull(book.getDownloadLinkForNewestVersion(FileType.Epub));

        // Link to revisions
        book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY, 52);
        assertNotNull(book.getDownloadLinkForNewestVersion(FileType.Epub));
    }
}
