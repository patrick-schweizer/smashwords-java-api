package com.unleashyouradventure.swapi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;

import com.jsing.common.string.StringTrimmer;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.PageLoader;

/**
 * Simulates the Smashwords server by reading from the file system. Useful for
 * offline testing.
 */
public class PageLoaderFromFile extends PageLoader {

    public String getPage(String url) throws IOException {
        InputStream stream = getLocalFile(url);
        if (stream == null)
            throw new IOException("404 file not found");
        int c;
        StringBuilder b = new StringBuilder();
        while ((c = stream.read()) != -1) {
            b.append((char) c);
        }
        stream.close();
        String page = b.toString();
        return page;
    }

    private InputStream getLocalFile(String url) {
        url = new StringTrimmer(url).getAfterNext(Smashwords.BASE_HOST + "/").getBeforeNext("?").toString();
        if ("".equals(url)) {
            url = "index";
        }
        url = url.replace('/', '_');
        if (!url.endsWith(".mobi"))
            url = url + ".html";

        return PageLoaderFromFile.class.getResourceAsStream(url);
    }

    public File saveURLToFile(File parentFolder, String url) throws IOException {

        File file = createFileName(parentFolder, url);
        FileOutputStream out = new FileOutputStream(file);
        writeToStream(url, out);
        return file;
    }

    public byte[] getUrlAsBytes(String url) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return out.toByteArray();
    }

    private void writeToStream(String url, OutputStream out) throws IOException {
        String content = getPage(url);
        Reader reader = new StringReader(content);
        int c;
        while ((c = reader.read()) > -1) {
            out.write(c);
        }
        out.close();
    }
}
