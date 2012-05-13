package com.unleashyouradventure.swapi.retriever;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.cache.Cache;
import com.unleashyouradventure.swapi.cache.NoCache;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.retriever.Book.FileType;

public class BookRetriever {
    private final static Logger log = Logger.getLogger(BookRetriever.class.getName());

    private PageLoader loader;
    private LoginHelper login;
    private Cache cache = new NoCache();

    public BookRetriever(PageLoader loader, LoginHelper login) {
        this.loader = loader;
        this.login = login;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Book getBook(long id) throws IOException {
        Book book = cache.getBook(id);
        if (book == null) {
            return getBookWithDetails(id);
        }
        return book;
    }

    public Book getBookWithDetails(long id) throws IOException {
        Book book = cache.getBook(id);
        if (book != null && book.isBookDetailsAdded()) {
            return book;
        } else if (book == null) {
            book = new Book();
            book.setId(id);
            cache.putBook(book);
            // A book we have never heard about before
        } else {
            // We have to load the detail page and we write all attributes new
            // but we still keep the same object to not break any object
            // references.
        }

        String page = this.loader.getPage(Smashwords.BASE_URL + "/books/view/" + id);
        login.updateLoginStatus(page);

        book.setBookOwned(page.contains("<a href=\"#download\">You own it!</a>"));

        Element row;
        Element td;
        String url;
        Document doc = Jsoup.parse(page);

        // TODO add other attributes

        for (FileType type : FileType.values()) {
            Elements elements = doc.select("table tr td b:contains(" + type.getSmashwordsName() + ")");
            if (elements.size() > 0) {
                Element element = elements.get(0);
                row = element.parent().parent();
                td = row.children().last();
                if (td.children().size() == 0)
                    continue; // Format not available
                url = td.child(0).attr("href");
                String title;
                if (url.endsWith(type.name().toLowerCase())) {
                    // Direct download link
                    title = "Newest";
                } else {
                    // Leads to page with all revisions.We generate the link
                    // ourself
                    url = url.replace("null", "newest");
                    url = url + "." + type.name().toLowerCase();
                    title = "Revision Overview";
                }
                List<Book.Download> downloads = new ArrayList<Book.Download>();
                downloads.add(new Book.Download(title, new URL(url)));
                book.setBookDownloads(type, downloads);
            }
        }
        book.setBookDetailsAdded(true);
        return book;
    }
}
