package com.sw.access.retriever;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.jsing.common.string.StringTrimmer;
import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.retriever.BookCategory;

public class BookCategoryRetrieverGenerator {

    private StringBuilder b = new StringBuilder();
    private Document doc;
    private BookCategory root;

    private BookCategoryRetrieverGenerator() throws IOException {
        start();
    }

    private void start() throws IOException {
        Smashwords sw = new Smashwords(null, null);
        String page = sw.getLoader().getPage(Smashwords.BASE_URL);
        doc = Jsoup.parse(page);
        root = new BookCategory(1, "All");
        parseMainCategory(new BookCategory(3, "Fiction"));
        parseMainCategory(new BookCategory(4, "Non-Fiction"));
        createCode(root);
        System.out.print(b.toString());

    }

    private void createCode(BookCategory cat) {
        b.append("BookCategory cat").append(cat.getId()).append(" = new BookCategory(").append(cat.getId())
                .append(", \"").append(cat.toString()).append("\");\n");
        if (cat.getParent() != null) {
            b.append("cat").append(cat.getParent().getId()).append(".addChild(cat").append(cat.getId()).append(");\n");
        }
        for (BookCategory child : cat.getChildren()) {
            createCode(child);
        }
    }

    private void parseMainCategory(BookCategory parentCategory) throws IOException {
        root.addChild(parentCategory);
        String selector = "a[href=https://www.smashwords.com/books/category/" + parentCategory.getId() + "]";
        Element ul = doc.select(selector).first().parent().nextElementSibling();
        Element a;
        for (Element li : ul.children()) {
            a = li.child(0);
            String id = new StringTrimmer(a.attr("href")).getAfterLast("/").toString();
            String name = a.text();
            parentCategory.addChild(new BookCategory(Long.parseLong(id), name));
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new BookCategoryRetrieverGenerator();
    }

}
