package com.unleashyouradventure.swapi.retriever;

import java.io.IOException;
import java.util.List;
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

public class BookListRetriever {

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
    public List<Book> getBooksFromAuthor(String author) throws IOException {
        return getBooks(Smashwords.BASE_URL + "/profile/view/" + author);
    }

    public List<Book> getBooksNewest() throws IOException {
        return getBooks(Smashwords.BASE_URL + "/");
    }

    public List<Book> getBooksTop100() throws IOException {
        return getBooks(Smashwords.BASE_URL + "/100");
    }

    public List<Book> getBooksFromLibary() throws IOException {
        login.loginIfNecessary();
        return getBooks(Smashwords.BASE_URL + "/library");
    }

    public List<Book> getBooks(String url) throws IOException {

        LazyArrayList<Book> books = cache.getBooks(url);
        if (books == null) {
            books = new LazyArrayList<Book>();
            loadBooksIntoList(url, books);
        }

        return books;
    }

    private void loadBooksIntoList(String url, LazyArrayList<Book> books) throws IOException {
        String page = this.loader.getPage(url);
        login.updateLoginStatus(page);
        Document doc = Jsoup.parse(page);
        for (Element element : doc.getElementsByClass("bookCoverImg")) {
            Book book = parseBook(element);
            books.add(book);
        }
        String urlForNextSet = getUrlForNextSet(doc);
        books.setUrlForNextSet(urlForNextSet);
    }

    public void getMoreBooks(List<Book> books) throws IOException {
        if (books instanceof LazyArrayList && ((LazyArrayList<Book>) books).hasMoreElementsToLoad()) {
            LazyArrayList<Book> booksLAL = (LazyArrayList<Book>) books;
            loadBooksIntoList(booksLAL.getUrlForNextSet(), booksLAL);
        }
    }

    private String getUrlForNextSet(Document doc) {
        Elements elements = doc.getElementsContainingOwnText("Next &gt;");
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
        // book.setCoverImage(getCoverImage(book.getCoverUrl()));
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
            return new StringTrimmer(style).getAfterNext("background:url('").getBeforeNext("'").toString();
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
            Element subnote = element.getElementsByClass("subnote").first();
            String txt = subnote.text();
            if (txt.contains("Price: Free!")) {
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
