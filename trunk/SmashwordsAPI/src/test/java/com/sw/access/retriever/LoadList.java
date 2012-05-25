package com.sw.access.retriever;

import java.io.IOException;
import java.util.List;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.retriever.Book;

public class LoadList {

    private Smashwords sw = new Smashwords(null, null);

    private void loadAndPrintList(String url) throws IOException {
        List<Book> books = sw.getBookListRetriever().getBooks(PageLoader.PROGRESS_CALLBACK_DUMMY, url);
        for (Book book : books) {
            System.out.println(book);
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new LoadList().loadAndPrintList(args[0]);
    }

}
