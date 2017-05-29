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
    public void shouldHaveDirektDownloadLink() throws IOException{
        testGetBookLink(145431);
    }

    @Test
    public void shouldHaveDirektDownloadLinkForBoughtBook() throws IOException{
        testGetBookLink(326886);
    }

    @Test
    public void shouldHaveDirektDownloadLinkForBookWithPrice() throws IOException{
        testGetBookLink(120327);
    }

    @Test
    public void shouldHaveDirektDownloadLinkWithRevisions() throws IOException{
        testGetBookLink(52);
    }

    private void testGetBookLink(long bookId) throws IOException {
        Book book = lib.getBookWithDetails(PageLoader.PROGRESS_CALLBACK_DUMMY,bookId);
        assertNotNull(book.getDownloadLinkForNewestVersion(FileType.Epub));

    }
}
