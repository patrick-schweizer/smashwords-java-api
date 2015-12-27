package com.unleashyouradventure.swapi.retriever;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.cache.Cache;
import com.unleashyouradventure.swapi.cache.NoCache;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.load.PageLoader.ProgressCallback;
import com.unleashyouradventure.swapi.model.*;
import com.unleashyouradventure.swapi.retriever.parser.list.PriceParser;
import com.unleashyouradventure.swapi.retriever.parser.list.RatingParser;
import com.unleashyouradventure.swapi.util.ParseUtils.Parser;
import com.unleashyouradventure.swapi.util.StringTrimmer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookListRetriever {

    public enum Sortby {
        any("Any"), newest("Newest"), bestsellers("Best Sellers"), unitssold("Units Sold"), downloads("Most Downloads"), highlyrated("Highest Rated");
        private String displayName;

        Sortby(String displayName) {
            this.displayName = displayName;
        }

        public String toString() {
            return this.displayName;
        }

        public Object getUrlName() {
            return this.name();
        }
    }

    public enum Price {
        anyPrice("Any price"), free("Free"), $0_99orless("$0.99 or less", ".99"), $2_99orless("$2.99 or less", "2.99"), $5_99orless("$5.99 or less", "5.99"), $9_99orless("$9.99 or less", "9.99");
        private final String urlName;
        private final String displayName;

        Price(String displayName, String urlName) {
            this.urlName = urlName;
            this.displayName = displayName;
        }

        Price(String displayName) {
            this.displayName = displayName;
            this.urlName = name();
        }

        public String getUrlName() {
            return urlName;
        }

        public String toString() {
            return this.displayName;
        }
    }

    public enum Length {

        any("Any length"), Short("Short", "short"), medium("Medium"), full("Full"), epic("Epic");
        private final String urlName;
        private final String displayName;

        Length(String displayName, String urlName) {
            this.displayName = displayName;
            this.urlName = urlName;
        }

        Length(String displayName) {
            this.displayName = displayName;
            this.urlName = name();
        }

        public String getUrlName() {
            return urlName;
        }

        public String toString() {
            return this.displayName;
        }
    }

    public enum AdultContent {
        /**
         * Use no parameter 'adult' but rely on the Smashwords mechanism: For
         * not logged in users its off, for logged in users it depends on the
         * user settings.
         */
        swdefault, on, off;
    }

    private final static Logger log = Logger.getLogger(BookListRetriever.class.getName());
    public final static String URL_LIBRARY = Smashwords.BASE_URL + "/library";
    private PageLoader loader;
    private LoginHelper login;
    private Cache cache = new NoCache();

    public BookListRetriever(PageLoader loader, LoginHelper login) {
        this.loader = loader;
        this.login = login;
    }

    /**
     * Instead
     * of an author you can also use a publisher
     */
    public BookList getBooksFromAuthor(ProgressCallback progressCallback, String author) throws IOException {
        return getBooks(progressCallback, Smashwords.BASE_URL + "/profile/view/" + author);
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category, Sortby sortby, Price price) throws IOException {
        return getBooksByCategory(progressCallback, category, sortby, price, Length.any);
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category, Sortby sortby) throws IOException {
        return getBooksByCategory(progressCallback, category, sortby, Price.anyPrice, Length.any);
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category) throws IOException {
        return getBooksByCategory(progressCallback, category, Sortby.any, Price.anyPrice, Length.any);
    }

    public BookList getBooksBySeries(ProgressCallback progressCallback, SwSeries series) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(Smashwords.BASE_URL).append("/books/byseries/").append(series.getId());
        return getBooks(progressCallback, url.toString());
    }

    public BookList getBooksBySearch(ProgressCallback progressCallback, String searchterm) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(Smashwords.BASE_URL).append("/books/search?query=");
        url.append(URLEncoder.encode(searchterm, "UTF-8"));
        return getBooks(progressCallback, url.toString());
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category, Sortby sortby, Price price, Length length) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(Smashwords.BASE_URL).append("/books/category/");
        url.append(category.getId()).append("/");
        url.append(sortby.getUrlName()).append("/0/");
        url.append(price.getUrlName()).append('/');
        url.append(length.getUrlName());
        return getBooks(progressCallback, url.toString());
    }

    public BookList getBooksFromLibary(ProgressCallback progress) throws IOException {
        login.loginIfNecessary();
        String url = BookListRetriever.URL_LIBRARY;
        BookList books = cache.getBooks(url);
        if (books == null) {
            books = new BookList();
            try {
                progress.setCurrentAction("Connecting to Smashwords");
                String page = this.loader.getPage(url);
                login.updateLoginStatus(page);
                progress.setCurrentAction("Reading page");
                List<Book> bookList = parseJsonBooklist(page);
                books.addAll(bookList);
            } catch (Exception e) {
                log.log(Level.SEVERE, "Cannot load library from: " + url);
            }
        }
        return books;
    }

    private List<Book> parseJsonBooklist(String page) {
        String json = new StringTrimmer(page).getAfterNext("window.angularData[\"library\"] =").getBeforeNext("</script>").getBeforeLast(";").toString();
        Gson gson = new GsonBuilder().create();
        SwResult jsonResult = gson.fromJson(json, SwResult.class);
        ArrayList<Book> result = new ArrayList<Book>();
        result.addAll(jsonResult.getBooks().getAddedBooks());
        result.addAll(jsonResult.getBooks().getPurchasedBooks());
        result.addAll(jsonResult.getBooks().getGiftedBooks());
        return result;
    }

    public BookList getBooks(ProgressCallback progressCallback, String url) throws IOException {
        BookList books = cache.getBooks(url);
        if (books == null) {
            books = new BookList();
            loadBooksIntoList(progressCallback, url, books);
        }

        return books;
    }

    private void loadBooksIntoList(ProgressCallback progress, String url, BookList books) throws IOException {
        progress.setCurrentAction("Connecting to Smashwords");
        String page = this.loader.getPage(url);
        login.updateLoginStatus(page);
        progress.setCurrentAction("Reading page");

        int prog = 30;
        progress.setProgress(prog);
        Document doc = Jsoup.parse(page);
        Elements elements = doc.getElementsByClass("library-book");
        int divisor = elements.size() == 0 ? 1 : elements.size();
        int progStep = (100 - prog) / divisor;
        for (Element element : elements) {
            Book book = parseBook(element);
            try {
                books.add(book);
            } catch (NullPointerException e) {
                handleException(e, element);
                progress.setCurrentAction("Adding book: " + book.getTitle());
            }
            progress.setProgress(prog += progStep);

        }
        String urlForNextSet = getUrlForNextSet(doc);
        books.setUrlForNextSet(urlForNextSet);
        cache.putBooks(url, books);
    }

    private void handleException(NullPointerException e, Element element) {
        throw new IllegalArgumentException("Nullpointer while parsing :"+ element, e);
    }

    public void getMoreBooks(ProgressCallback progressCallback, BookList books) throws IOException {
        if (books.hasMoreElementsToLoad()) {
            loadBooksIntoList(progressCallback, books.getUrlForNextSet(), books);
        }
    }

    private String getUrlForNextSet(Document doc) {
        Elements elements = doc.select("i.icon-hand-right");
        if (elements.isEmpty()) {
            return null;
        }
        Element elem = elements.first();
        String url = elem.parent().attr("href");
        return url;
    }

    private Book parseBook(Element element) {
        Book book = new Book();
        book.setId(idParser.parse(element));
        book.setTitle(titleParser.parse(element));
        book.setContributors(new ArrayList<SwPerson>());
        book.getContributors().add(authorParser.parse(element));
        book.setCover_url(imgParser.parse(element));
        book.addPrice(priceParser.parse(element));
        book.setShort_description(shortDescriptionParser.parse(element));
        book.setRating(ratingParser.parse(element));
        return book;
    }

    private final static Parser<String> imgParser = new Parser<String>() {

        @Override
        protected String parseElement(Element element) {
            String url = element.select("img[class=book-list-image]").attr("src");
            return new StringTrimmer(url).getBeforeNext("-thumb").toString();
        }
    };

    private static final Parser<Long> idParser = new Parser<Long>() {
        @Override
        protected Long parseElement(Element element) {
            Element a = element.getElementsByClass("library-title").first().getElementsByTag("a").first();
            String idString = new StringTrimmer(a.attr("href")).getAfterLast("/").toString();
            return Long.parseLong(idString);
        }
    };

    private final static Parser<String> titleParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element a = element.getElementsByClass("library-title").first();
            return a.text();
        }
    };

    private final static Parser<SwPerson> authorParser = new Parser<SwPerson>() {

        @Override
        protected SwPerson parseElement(Element element) {
            Element a = element.getElementsByClass("subnote").first().getElementsByTag("a").first();
            String authorName = a.text();
            SwPerson person = new SwPerson();
            SwAccount account = new SwAccount();
            account.setDisplay_name(authorName);
            person.setAccount(account);
            return person;
        }
    };

    private static final Parser<SwPrice> priceParser = new PriceParser();

    private static final Parser<String> shortDescriptionParser = new Parser<String>() {

        @Override
        protected String parseElement(Element element) {
            if (element == null) {
                return null;
            }
            String text = element.getElementsByClass("library-well").first().ownText();
            return text;
        }
    };

    private static final Parser<Double> ratingParser = new RatingParser();

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
