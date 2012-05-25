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

import com.jsing.common.string.StringTrimmer;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.cache.Cache;
import com.unleashyouradventure.swapi.cache.NoCache;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.load.PageLoader.ProgressCallback;
import com.unleashyouradventure.swapi.retriever.Book.FileType;
import com.unleashyouradventure.swapi.util.ParseUtils;
import com.unleashyouradventure.swapi.util.ParseUtils.Parser;

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

    public Book getBook(ProgressCallback progressCallback, long id) throws IOException {
        Book book = cache.getBook(id);
        if (book == null) {
            return getBookWithDetails(progressCallback, id);
        }
        return book;
    }

    public Book getBookWithDetails(ProgressCallback progressCallback, long id) throws IOException {
        boolean loadBookInformation = false;
        Book book = cache.getBook(id);
        if (book != null && book.isBookDetailsAdded()) {
            progressCallback.setProgress(90);
            progressCallback.setCurrentAction("Book retrieved from cache");
            return book;
        } else if (book == null) {
            // A book we have never heard about before -> We need to load the
            // book information as well
            loadBookInformation = true;
            book = new Book();
            book.setId(id);
            cache.putBook(book);
        } else {
            // We have to load the details but we still keep the same object to
            // not break any object references.
        }
        progressCallback.setCurrentAction("Contacting Smashwords");
        String page = this.loader.getPage(Smashwords.BASE_URL + "/books/view/" + id);
        login.updateLoginStatus(page);
        progressCallback.setProgress(50);
        progressCallback.setCurrentAction("Parsing page");
        Document doc = Jsoup.parse(page);
        if (loadBookInformation) {
            loadBookInformation(page, doc, book);
            progressCallback.setProgress(70);
        }
        loadBookDetails(page, doc, book);
        progressCallback.setProgress(90);
        return book;
    }

    private void loadBookInformation(String rawPage, Document doc, Book book) {
        Element elem = doc.select("div [itemtype=http://data-vocabulary.org/Product]").first();
        book.setAuthor(authorParser.parse(elem));
        book.setCoverUrl(coverUrlParser.parse(elem));
        book.setDescriptionShort(descriptionParser.parse(elem));
        book.setPriceInCent(priceParser.parse(elem));
        book.setTitle(titleParser.parse(doc));
    }

    private void loadBookDetails(String rawPage, Document doc, Book book) throws IOException {
        book.setBookOwned(rawPage.contains("<a href=\"#download\">You own it!</a>"));
        loadDownloadUrls(doc, book);
    }

    private final static Parser<String> authorParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element a = element.select("a[href~=https://www.smashwords.com/profile/view/.*]").first();
            return a.text();
        }
    };

    private final static Parser<String> coverUrlParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element img = element.select("img[itemprop=image]").first();
            String url = new StringTrimmer(img.attr("src")).getBeforeLast("-thumb").toString();
            return url;
        }
    };

    private final static Parser<String> descriptionParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element span = element.select("span[itemprop=description]").first();
            return span.text();
        }
    };

    private final static Parser<Integer> priceParser = new Parser<Integer>() {
        @Override
        protected Integer parseElement(Element element) {
            Element span = element.select("span[itemprop=price]").first();
            String txt = span.text();
            return ParseUtils.parsePrice(txt);
        }
    };

    private final static Parser<String> titleParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element h2 = element.getElementsByClass("bookpage_title_heading").first();
            return h2.text();
        }
    };

    private void loadDownloadUrls(Document doc, Book book) throws IOException {

        Element row;
        Element td;
        String url;
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
                    // Leads to page with all revisions. We generate the link
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
    }

    public Cache getCache() {
        return cache;
    }
}
