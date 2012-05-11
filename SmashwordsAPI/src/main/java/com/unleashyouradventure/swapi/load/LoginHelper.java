package com.unleashyouradventure.swapi.load;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.unleashyouradventure.swapi.Smashwords;

public class LoginHelper {
    private PageLoader loader;
    private boolean isLoggedIn = false;
    private String username;
    private String password;

    public LoginHelper(PageLoader loader, String username, String password) {
        this.loader = loader;
        configure(username, password);
    }

    public void configure(String username, String password) {
        this.username = username;
        this.password = password;
        isLoggedIn = false;
    }

    public boolean logIn() throws IOException {
        if (!areCredentialsWellFormed())
            return false;
        String url = Smashwords.BASE_URL + "/auth/index";
        Map<String, String> params = new HashMap<String, String>();

        params.put("email", username);
        params.put("password", password);
        params.put("secToken", "");

        loader.postPage(url, params);
        String html = loader.getPage(Smashwords.BASE_URL);
        updateLoginStatus(html);
        return isLoggedIn;
    }

    public void updateLoginStatus(String html) {
        this.isLoggedIn = html.contains("Log Out");
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
}
