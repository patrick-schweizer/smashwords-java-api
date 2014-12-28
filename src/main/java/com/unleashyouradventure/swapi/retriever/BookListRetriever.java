package com.unleashyouradventure.swapi.retriever;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.cache.Cache;
import com.unleashyouradventure.swapi.cache.NoCache;
import com.unleashyouradventure.swapi.load.LoginHelper;
import com.unleashyouradventure.swapi.load.PageLoader;
import com.unleashyouradventure.swapi.load.PageLoader.ProgressCallback;
import com.unleashyouradventure.swapi.retriever.json.Author;
import com.unleashyouradventure.swapi.retriever.json.JPrice;
import com.unleashyouradventure.swapi.retriever.json.JsonResult;
import com.unleashyouradventure.swapi.util.ParseUtils;
import com.unleashyouradventure.swapi.util.ParseUtils.Parser;
import com.unleashyouradventure.swapi.util.StringTrimmer;

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
     * @param Instead
     *            of an author you can also use a publisher
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
        JsonResult jsonResult = gson.fromJson(json, JsonResult.class);
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
        book.setAuthors(new ArrayList<Author>());
        book.getAuthors().add(authorParser.parse(element));
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

    private final static Parser<Long> idParser = new Parser<Long>() {
        @Override
        protected Long parseElement(Element element) {
            Element a = element.getElementsByClass("library-title").first();
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

    private final static Parser<Author> authorParser = new Parser<Author>() {

        @Override
        protected Author parseElement(Element element) {
            Element a = element.getElementsByClass("subnote").first().getElementsByTag("a").first();
            String authorName = a.text();
            Author author = new Author();
            author.setDisplay_name(authorName);
            return author;
        }
    };

    private final static Parser<JPrice> priceParser = new Parser<JPrice>() {

        @Override
        protected JPrice parseElement(Element element) {
            JPrice price = new JPrice();
            price.setAmount(0);
            price.setCurrency("USD");
            if (element == null)
                return price;
            Element subnote = element.getElementsByClass("subnote").first();
            String txt = subnote.text();
            if (txt.contains("Price: Free!")) {
                return price;
            } else if (txt.contains("You set the price")) {
                return price;
            } else {
                txt = new StringTrimmer(txt).getAfterNext("Price: $").getBeforeNext("USD").toString();
                price.setAmount(ParseUtils.parsePrice(txt));
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
            t.getAfterNext("<div class=\"subnote\">");
            t.getAfterNext("</div>");
            t.getAfterNext("<div class=\"well well-nb library-well\">");
            t.getBeforeLast("</div>");
            String description = t.toString().trim();
            description = Jsoup.parseBodyFragment(description).text();
            return description;
        }
    };

    private final static Parser<Double> ratingParser = new Parser<Double>() {

        @Override
        protected Double parseElement(Element element) {
            if (element == null)
                return null;

            Elements elements = element.select("span[style=color: #888;]");
            if (elements.size() > 0) {
                String text = new StringTrimmer(elements.first().text()).getAfterNext("(").getBeforeNext(" from").getBeforeNext(")").toString().trim();
                return Double.parseDouble(text);
            } else {
                return getDefaultInCaseOfError(); // no rating
            }
        }

        protected Double getDefaultInCaseOfError() {
            return Double.valueOf(-1);
        }
    };

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
