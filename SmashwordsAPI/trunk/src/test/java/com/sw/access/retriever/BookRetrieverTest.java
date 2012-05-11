package com.sw.access.retriever;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.sw.access.OnOfflineTest;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.retriever.Book;
import com.unleashyouradventure.swapi.retriever.BookRetriever;

public class BookRetrieverTest extends OnOfflineTest {

    private enum SystemProperty {
        swUsername, swPassword
    }

    private BookRetriever lib;

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
        lib = new BookRetriever(this.pageLoader, login);
    }

    @Test
    public void testGetBookLinks() throws IOException {

        // Direkt Download link
        Book book = lib.getBookWithDetails(155976);
        assertNotNull(book);

        // Link to revisions
        book = lib.getBookWithDetails(142475);
        assertNotNull(book);
    }

}
