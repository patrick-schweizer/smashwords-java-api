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
import com.unleashyouradventure.swapi.load.PageLoader.ProgressCallback;
import com.unleashyouradventure.swapi.retriever.Book.FileType;
import com.unleashyouradventure.swapi.retriever.json.Author;
import com.unleashyouradventure.swapi.retriever.json.JPrice;
import com.unleashyouradventure.swapi.util.ParseUtils;
import com.unleashyouradventure.swapi.util.ParseUtils.Parser;
import com.unleashyouradventure.swapi.util.StringTrimmer;

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
        // Element elem = doc.select("div [itemtype=http://data-vocabulary.org/Product]").first();
        book.setAuthors(new ArrayList<Author>());
        book.getAuthors().addAll(authorParser.parse(doc));
        book.setCover_url(coverUrlParser.parse(doc));
        book.setShort_description(descriptionShortParser.parse(doc));
        book.addPrice(priceParser.parse(doc));
        book.setTitle(titleParser.parse(doc));
    }

    private void loadBookDetails(String rawPage, Document doc, Book book) throws IOException {
        book.setBookOwned(rawPage.contains("Another Copy"));
        book.setLong_description(descriptionLongParser.parse(doc));
        loadDownloadUrls(doc, book);
        book.setRating(ratingParser.parse(doc));
    }

    private final static Parser<List<Author>> authorParser = new Parser<List<Author>>() {
        @Override
        protected List<Author> parseElement(Element element) {

            Elements elements = element.select("a[href~=https://www.smashwords.com/profile/view/.*]");
            List<Author> authors = new ArrayList<Author>();
            for (Element it : elements) {
                if (!it.text().contains("Profile")) {
                    Author author = new Author();
                    String name = it.text();
                    StringTrimmer userName = new StringTrimmer(it.attr("href")).getAfterLast("/");
                    author.setDisplay_name(name);
                    author.setUsername(userName.toString());
                    authors.add(author);
                }
            }
            return authors;
        }
    };

    private final static Parser<String> coverUrlParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element img = element.select("img[class=cover-thumbnail]").first();
            String url = new StringTrimmer(img.attr("src")).getBeforeLast("-thumb").toString();
            return url;
        }
    };

    private final static Parser<String> descriptionShortParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element span = element.select("div[class=well]").first();
            return span.text();
        }
    };

    private final static Parser<String> descriptionLongParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element span = element.select("div#longDescription").first();
            String txt = new StringTrimmer(span.text()).toString();
            return txt;
        }
    };

    private final static Parser<JPrice> priceParser = new Parser<JPrice>() {
        @Override
        protected JPrice parseElement(Element element) {
            Element elem = element.select("h3.panel-title").first();
            String txt = elem.text();
            double priceValue;
            if (txt.contains("Price: Free!")) {
                priceValue = 0;
            } else {
                txt = new StringTrimmer(txt).getAfterNext("Price: $").getBeforeNext(" USD").toString();
                priceValue = ParseUtils.parsePrice(txt);
            }
            JPrice price = new JPrice();
            price.setAmount(priceValue);
            price.setCurrency("USD");
            return price;
        }

        protected JPrice getDefaultInCaseOfError() {
            JPrice price = new JPrice();
            price.setAmount(0);
            price.setCurrency("USD");
            return price;
        }
    };

    private final static Parser<String> titleParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element h1 = element.getElementsByTag("h1").first();
            return h1.text();
        }
    };

    private final static Parser<Double> ratingParser = new Parser<Double>() {
        @Override
        protected Double parseElement(Element element) {
            Element span = element.select("span[itemtype=http://data-vocabulary.org/Review-aggregate]").first();
            if (span != null) {
                span = span.select("span[itemprop=rating]").first();
                return Double.valueOf(span.text());
            }
            return getDefaultInCaseOfError();
        }

        protected Double getDefaultInCaseOfError() {
            return Double.valueOf(-1);
        }
    };

    private void loadDownloadUrls(Document doc, Book book) throws IOException {
        String url;
        FileType type;
        Element downloadSection = doc.select("div#download").first();
        Elements elements = downloadSection.select("a");
        for (Element a : elements) {
            url = Smashwords.BASE_URL + a.attr("href");
            type = FileType.getByEnding(new StringTrimmer(url).getAfterLast(".").toString());
            List<Book.Download> downloads = new ArrayList<Book.Download>();
            downloads.add(new Book.Download("Newest", new URL(url)));
            book.setBookDownloads(type, downloads);
        }
        book.setBookDetailsAdded(true);
    }

    public Cache getCache() {
        return cache;
    }
}
