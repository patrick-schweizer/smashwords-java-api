package com.unleashyouradventure.swapi.retriever;

import java.io.IOException;
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
import com.unleashyouradventure.swapi.load.PageLoader.ProgressCallbackDummy;

public class BookListRetriever {

    public enum Sortby {
        any, newest, bestsellers, unitssold, downloads, highlyrated;
    }

    public enum Price {
        anyPrice("any"), free, $0_99orless(".99"), $2_99orless("2.99"), $5_99orless("5.99"), $9_99orless("9.99");
        private final String urlName;

        Price(String urlName) {
            this.urlName = urlName;
        }

        Price() {
            this.urlName = name();
        }

        public String getUrlName() {
            return urlName;
        }
    }

    public enum Length {

        any, Short("short"), medium, full, epic;
        private final String urlName;

        Length(String urlName) {
            this.urlName = urlName;
        }

        Length() {
            this.urlName = name();
        }

        public String getUrlName() {
            return urlName;
        }
    }

    private final static Logger log = Logger.getLogger(BookListRetriever.class.getName());

    private PageLoader loader;
    private LoginHelper login;
    private Cache cache = new NoCache();
    private ProgressCallback processCallbackDummy = new ProgressCallbackDummy();

    public BookListRetriever(PageLoader loader, LoginHelper login) {
        this.loader = loader;
        this.login = login;
    }

    public BookList getBooksFromAuthor(String author) throws IOException {
        return getBooksFromAuthor(author, processCallbackDummy);
    }

    /**
     * @param Instead
     *            of an author you can also use a publisher
     */
    public BookList getBooksFromAuthor(String author, ProgressCallback progress) throws IOException {
        return getBooks(Smashwords.BASE_URL + "/profile/view/" + author, progress);
    }

    public BookList getBooksByCategory(Sortby sortby, Price price, ProgressCallback progress) throws IOException {
        return getBooksByCategory(sortby, price, Length.any, progress);
    }

    public BookList getBooksByCategory(Sortby sortby, ProgressCallback progress) throws IOException {
        return getBooksByCategory(sortby, Price.anyPrice, Length.any, progress);
    }

    public BookList getBooksByCategory(Sortby sortby, Price price, Length length) throws IOException {
        return getBooksByCategory(sortby, price, length, processCallbackDummy);
    }

    public BookList getBooksByCategory(Sortby sortby, Price price, Length length, ProgressCallback progress)
            throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(Smashwords.BASE_URL).append("/books/category/1/");
        url.append(sortby).append("/0/");
        url.append(price.getUrlName()).append('/');
        url.append(length.getUrlName());
        return getBooks(url.toString(), progress);
    }

    public BookList getBooksFromLibary() throws IOException {
        return getBooksFromLibary(processCallbackDummy);
    }

    public BookList getBooksFromLibary(ProgressCallback progress) throws IOException {
        login.loginIfNecessary();
        return getBooks(Smashwords.BASE_URL + "/library", progress);
    }

    public BookList getBooks(String url, ProgressCallback progress) throws IOException {

        BookList books = cache.getBooks(url);
        if (books == null) {
            books = new BookList();
            loadBooksIntoList(url, books, progress);
        }

        return books;
    }

    private void loadBooksIntoList(String url, BookList books, ProgressCallback progress) throws IOException {
        progress.setCurrentAction("Connecting to Smashwords");
        String page = this.loader.getPage(url);
        login.updateLoginStatus(page);
        progress.setCurrentAction("Reading page");
        progress.setProgress(30);
        int prog = 30;
        Document doc = Jsoup.parse(page);
        for (Element element : doc.getElementsByClass("bookCoverImg")) {
            Book book = parseBook(element);
            books.add(book);
            progress.setCurrentAction("Adding book: " + book.getTitle());
            progress.setProgress(prog += 5);
        }
        String urlForNextSet = getUrlForNextSet(doc);
        books.setUrlForNextSet(urlForNextSet);
        cache.putBooks(url, books);
    }

    public void getMoreBooks(BookList books) throws IOException {
        ProgressCallback progress = new ProgressCallbackDummy();
        getMoreBooks(books, progress);
    }

    public void getMoreBooks(BookList books, ProgressCallback progress) throws IOException {
        if (books.hasMoreElementsToLoad()) {
            loadBooksIntoList(books.getUrlForNextSet(), books, progress);
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

    private byte[] getCoverImage(String imgUrl) {
        try {
            return this.loader.getUrlAsBytes(imgUrl);
        } catch (IOException e) {
            log.log(Level.WARNING, "Loading error, url=" + imgUrl, e);
        }
        return null;
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
