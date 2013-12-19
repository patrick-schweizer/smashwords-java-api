package com.unleashyouradventure.swapi.load;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.retriever.BookListRetriever;
import com.unleashyouradventure.swapi.util.ParseUtils;

public class LoginHelper {
    private Smashwords sw;
    private boolean isLoggedIn = false;
    private String username;
    private String password;

    public LoginHelper(Smashwords sw, String username, String password) {
        this.sw = sw;
        configure(username, password);
    }

    /** @return did the username or passwort change? */
    public boolean configure(String username, String password) {
        boolean hasChanged = !ParseUtils.equals(this.username, username) || !ParseUtils.equals(this.password, password);
        if (hasChanged) {
            this.username = username;
            this.password = password;
            isLoggedIn = false;
            // Invalidate library cache
            sw.getCache().remove(BookListRetriever.URL_LIBRARY);
            sw.getCache().removeAllBookDetails(); // Book details contain info
                                                  // if book was bought by user
        }
        return hasChanged;
    }

    public boolean logIn() throws IOException {
        if (!areCredentialsWellFormed())
            return false;
        String url = Smashwords.BASE_URL + "/auth/index";
        Map<String, String> params = new HashMap<String, String>();

        params.put("email", username);
        params.put("password", password);
        params.put("secToken", "");

        String html = sw.getLoader().postPage(url, params);
        // String html = sw.getLoader().getPage(Smashwords.BASE_URL);
        updateLoginStatus(html);
        return isLoggedIn;
    }

    public void updateLoginStatus(String html) {
        this.isLoggedIn = html.contains("window.angularData.user.loggedIn = true;");
    }

    public void loginIfNecessary() throws IOException {
        if (!this.isLoggedIn) {
            logIn();
        }
    }

    /**
     * Checks if the credentials (user name and password) are formally correct.
     * It does not check however if the user really exists and if his password
     * is correct.
     */
    public boolean areCredentialsWellFormed() {
        return isNotEmpty(this.username) && isNotEmpty(this.password);
    }

    private boolean isNotEmpty(String s) {
        return s != null && s.trim().length() > 0;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void logOut() throws IOException {
        sw.getLoader().getPage(Smashwords.BASE_URL + "/auth/logout");
    }
}
