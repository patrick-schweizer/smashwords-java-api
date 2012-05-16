package com.unleashyouradventure.swapi.retriever;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
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

public class BookListRetriever {

    public enum Sortby {
        any("Any"), newest("Newest"), bestsellers("Best Sellers"), unitssold("Units Sold"), downloads("Most Downloads"), highlyrated(
                "Highest Rated");
        private String displayName;

        Sortby(String displayName) {
            this.displayName = displayName;
        }

        public String toString() {
            return this.displayName;
        }
    }

    public enum Price {
        anyPrice("Any price"), free("Free"), $0_99orless("$0.99 or less", ".99"), $2_99orless("$2.99 or less", "2.99"), $5_99orless(
                "$5.99 or less", "5.99"), $9_99orless("$9.99 or less", "9.99");
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

    private final static Logger log = Logger.getLogger(BookListRetriever.class.getName());

    private PageLoader loader;
    private LoginHelper login;
    private Cache cache = new NoCache();

    public BookListRetriever(PageLoader loader, LoginHelper login) {
        this.loader = loader;
        this.login = login;
    }

    /**
     * @param Instead
     *            of an author you can also use a publisher
     */
    public BookList getBooksFromAuthor(ProgressCallback progressCallback, String author) throws IOException {
        return getBooks(progressCallback, Smashwords.BASE_URL + "/profile/view/" + author);
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category, Sortby sortby,
            Price price) throws IOException {
        return getBooksByCategory(progressCallback, category, sortby, price, Length.any);
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category, Sortby sortby)
            throws IOException {
        return getBooksByCategory(progressCallback, category, sortby, Price.anyPrice, Length.any);
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category) throws IOException {
        return getBooksByCategory(progressCallback, category, Sortby.any, Price.anyPrice, Length.any);
    }

    public BookList getBooksBySearch(ProgressCallback progressCallback, String searchterm) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(Smashwords.BASE_URL).append("/books/search?query=");
        url.append(URLEncoder.encode(searchterm, "UTF-8"));
        return getBooks(progressCallback, url.toString());
    }

    public BookList getBooksByCategory(ProgressCallback progressCallback, BookCategory category, Sortby sortby,
            Price price, Length length) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(Smashwords.BASE_URL).append("/books/category/");
        url.append(category.getId()).append("/");
        url.append(sortby).append("/0/");
        url.append(price.getUrlName()).append('/');
        url.append(length.getUrlName());
        return getBooks(progressCallback, url.toString());
    }

    public BookList getBooksFromLibary(ProgressCallback progressCallback) throws IOException {
        login.loginIfNecessary();
        return getBooks(progressCallback, Smashwords.BASE_URL + "/library");
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
        Elements elements = doc.getElementsByClass("bookCoverImg");
        int progStep = (100 - prog) / elements.size();
        for (Element element : elements) {
            Book book = parseBook(element);
            books.add(book);
            progress.setCurrentAction("Adding book: " + book.getTitle());
            progress.setProgress(prog += progStep);
        }
        String urlForNextSet = getUrlForNextSet(doc);
        books.setUrlForNextSet(urlForNextSet);
        cache.putBooks(url, books);
    }

    public void getMoreBooks(ProgressCallback progressCallback, BookList books) throws IOException {
        if (books.hasMoreElementsToLoad()) {
            loadBooksIntoList(progressCallback, books.getUrlForNextSet(), books);
        }
    }

    private String getUrlForNextSet(Document doc) {
        Elements elements = doc.getElementsContainingOwnText("Next >");
        if (elements.isEmpty()) {
            return null;
        }
        Element element = elements.first();
        String url = element.attr("href");
        return url;
    }

    private Book parseBook(Element element) {
        Book book = new Book();
        book.setId(idParser.parse(element));
        book.setTitle(titleParser.parse(element));
        book.setAuthor(authorParser.parse(element));
        book.setCoverUrl(imgParser.parse(element));
        book.setPriceInCent(priceParser.parse(element));
        book.setDescriptionShort(shortDescriptionParser.parse(element));
        return book;
    }

    private final static Parser<String> imgParser = new Parser<String>() {

        @Override
        protected String parseElement(Element element) {
            String style = element.attr("style");
            return new StringTrimmer(style).getAfterNext("background:url('").getBeforeNext("-tiny'").toString();
        }
    };

    private final static Parser<Long> idParser = new Parser<Long>() {
        @Override
        protected Long parseElement(Element element) {
            Element a = element.getElementsByClass("bookTitle").first();
            String idString = new StringTrimmer(a.attr("href")).getAfterLast("/").toString();
            return Long.parseLong(idString);
        }
    };

    private final static Parser<String> titleParser = new Parser<String>() {
        @Override
        protected String parseElement(Element element) {
            Element a = element.getElementsByClass("bookTitle").first();
            return a.text();
        }
    };

    private final static Parser<String> authorParser = new Parser<String>() {

        @Override
        protected String parseElement(Element element) {
            Element a = element.getElementsByClass("subnote").first().getElementsByTag("a").first();
            return a.text();
        }
    };

    private final static Parser<Integer> priceParser = new Parser<Integer>() {

        @Override
        protected Integer parseElement(Element element) {
            if (element == null)
                return 0;
            Element subnote = element.getElementsByClass("subnote").first();
            String txt = subnote.text();
            if (txt.contains("Price: Free!")) {
                return 0;
            } else if (txt.contains("You set the price")) {
                return 0;
            } else {
                txt = new StringTrimmer(txt).getAfterNext("Price: $").getBeforeNext("USD").toString();
                txt = txt.replace(".", "");
                txt = txt.replace(" ", "");
                Integer price = Integer.valueOf(txt);
                return price;
            }
        }
    };

    private final static Parser<String> shortDescriptionParser = new Parser<String>() {

        @Override
        protected String parseElement(Element element) {
            if (element == null)
                return null;
            String text = element.getElementsByClass("text").first().html();
            StringTrimmer t = new StringTrimmer(text);
            t.getAfterNext("<span class=\"subnote\">");
            t.getAfterNext("</span>");
            t.getAfterLast("<br />");
            String description = t.toString().trim();
            description = Jsoup.parseBodyFragment(description).text();
            return description;
        }
    };

    private static abstract class Parser<T> {

        public T parse(Element element) {
            try {
                return parseElement(element);
            } catch (Exception e) {
                log.log(Level.WARNING, "Parse error, Element: " + element.toString(), e);
            }
            return null;
        }

        protected abstract T parseElement(Element element);
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
