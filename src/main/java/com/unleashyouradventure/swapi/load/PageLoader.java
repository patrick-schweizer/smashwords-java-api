package com.unleashyouradventure.swapi.load;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.httpclient.AllowAllRedirectsHandler;
import com.unleashyouradventure.swapi.retriever.BookListRetriever.AdultContent;
import com.unleashyouradventure.swapi.util.IOUtil;
import com.unleashyouradventure.swapi.util.StringTrimmer;

public class PageLoader {

    public final static ProgressCallback PROGRESS_CALLBACK_DUMMY = new ProgressCallbackDummy();

    private DefaultHttpClient client;

    public PageLoader() {
        BasicHttpParams params = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        client = new DefaultHttpClient(cm, params);

        // TODO GZipHandler gzip = new GZipHandler();
        // TODO client.addRequestInterceptor(gzip);
        // TODO client.addResponseInterceptor(gzip);
        // HttpHost proxy = new HttpHost("127.0.0.1", 8900);
        // client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
        // proxy);
        client.setRedirectHandler(new AllowAllRedirectsHandler());
    }

    public void setAdultContent(AdultContent adultContent) {

        if (adultContent == AdultContent.swdefault) {
            // Remove cookie
            List<Cookie> list = client.getCookieStore().getCookies();
            client.getCookieStore().clear();
            for (Cookie cookie : list) {
                if (cookie.getName().contains("adultOff")) {
                    continue;
                }
                client.getCookieStore().addCookie(cookie);
            }
        } else {
            String value = (adultContent == AdultContent.on) ? "no" : "yes";
            BasicClientCookie cookie = new BasicClientCookie("adultOff", value);
            cookie.setDomain(".smashwords.com");
            cookie.setPath("/");
            client.getCookieStore().addCookie(cookie);
        }
    }

    public String getPage(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        addDefaultHeaders(get);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        HttpResponse response = client.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return responseHandler.handleResponse(response);
        }
        throw new IOException("Status " + statusCode + "received");
    }

    public File saveURLToFile(File parentFolder, String url) throws IOException {
        return saveURLToFile(parentFolder, url, new ProgressCallbackDummy());

    }

    public File saveURLToFile(File parentFolder, String url, ProgressCallback callback) throws IOException {
        HttpGet get = new HttpGet(url);
        addDefaultHeaders(get);
        HttpResponse response = client.execute(get);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            long contentLength = response.getEntity().getContentLength();
            InputStream input = response.getEntity().getContent();
            File file = createFileName(parentFolder, url);
            OutputStream out = new FileOutputStream(file);
            IOUtil.copy(input, out, callback, contentLength);
            return file;
        }
        throw new IOException("Status " + statusCode + "received");
    }

    protected File createFileName(File parentFolder, String url) {
        String fileName = new StringTrimmer(url).getAfterLast("/").getBeforeNext("?").toString();
        File file = new File(parentFolder, fileName);
        return file;
    }

    public String postPage(String url, Map<String, String> params) throws IOException {
        HttpPost post = new HttpPost(url);
        addDefaultHeaders(post);
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        post.setHeader("Accept-Encoding", "deflate");
        post.setHeader("Accept-Language", "de-de,de;q=0.8,en-us;q=0.5,en;q=0.3");
        post.setHeader("DNT", "1");
        post.setHeader("Host", "www.smashwords.com");
        post.setHeader("Referer", Smashwords.BASE_URL);
        post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (Map.Entry<String, String> param : params.entrySet()) {
            nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }

        post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        HttpResponse response = client.execute(post);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String html = responseHandler.handleResponse(response);
            Logger.getLogger(this.getClass().getName()).fine("Response of login: " + html);
            return html;
        }
        throw new IOException("Status " + statusCode + "received");
    }

    private void addDefaultHeaders(HttpRequestBase request) {

        request.setHeader("User-Agent", "Mozilla/5.0 (Ubuntu; X11; Linux i686; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
        // request.setHeader("Accept-Encoding", "gzip, deflate");
        // we need to simulate the webkit user agent in order to get the mobile
        // version :(
        // request.setHeader(
        // "User-Agent",
        // "Mozilla/5.0 (Linux; U; Android 1.0; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2");
        // request.setHeader("Referer",
        // "https://www.smashwords.com/auth/index");
    }

    public byte[] getUrlAsBytes(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        addDefaultHeaders(get);
        HttpResponse response = client.execute(get);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            InputStream input = response.getEntity().getContent();
            byte[] bytes = IOUtil.toByteArray(input, null, 1l);
            return bytes;
        }
        throw new IOException("Status " + statusCode + "received");
    }

    public static interface ProgressCallback {
        public void setProgress(int progressInPercent);

        public void setCurrentAction(String action);
    }

    public static class ProgressCallbackDummy implements ProgressCallback {

        public void setProgress(int progressInPercent) {
            // do nothing, we are a dummy
        }

        public void setCurrentAction(String action) {
            // do nothing, we are a dummy
        }
    }
}
