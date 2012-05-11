package com.unleashyouradventure.swapi.load.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

/** Allows Redirects for all Methods (not only for GET and PUT) */
public class AllowAllRedirectsHandler extends DefaultRedirectHandler {

    @Override
    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
        boolean isRedirect = false;

        isRedirect = super.isRedirectRequested(response, context);

        if (!isRedirect) {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 301 || responseCode == 302) {
                return true;
            }
        }
        return isRedirect;
    }
}
