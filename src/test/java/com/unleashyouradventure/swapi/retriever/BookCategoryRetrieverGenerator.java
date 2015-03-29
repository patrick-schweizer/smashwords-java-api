package com.unleashyouradventure.swapi.retriever;

import com.unleashyouradventure.swapi.Smashwords;
import com.unleashyouradventure.swapi.util.NullHelper;
import com.unleashyouradventure.swapi.util.StringTrimmer;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

@Log
public class BookCategoryRetrieverGenerator {

    private StringBuilder b = new StringBuilder();
    private Document doc;
    private BookCategory root;
    private Smashwords sw = new Smashwords(null, null);

    private BookCategoryRetrieverGenerator() throws IOException {
        start();
    }

    private void start() throws IOException {
        sw = new Smashwords(null, null);
        String page = sw.getLoader().getPage(Smashwords.BASE_URL);
        doc = Jsoup.parse(page);
        root = new BookCategory(1, "All");
        parseMainCategory(new BookCategory(3, "Fiction"));
        parseMainCategory(new BookCategory(4, "Non-Fiction"));
        parseMainCategory(new BookCategory(898, "Essay"));
        parseMainCategory(new BookCategory(2044, "Plays"));
        parseMainCategory(new BookCategory(2, "Screenplays"));
        parseMainCategory(new BookCategory(56, "Poetry"));

        createCode(root);
        System.out.print(b.toString());

    }

    private void createCode(BookCategory cat) {
        b.append("BookCategory cat").append(cat.getId()).append(" = new BookCategory(").append(cat.getId()).append(", \"").append(cat.toString()).append("\");\n");
        if (cat.getParent() != null) {
            b.append("cat").append(cat.getParent().getId()).append(".addChild(cat").append(cat.getId()).append(");\n");
        }
        for (BookCategory child : cat.getChildren()) {
            createCode(child);
        }
    }

    private void parseMainCategory(BookCategory parentCategory) throws IOException {
        log.info("Parsing " + parentCategory.getId());
        root.addChild(parentCategory);

        String page = sw.getLoader().getPage(Smashwords.BASE_URL + "/books/category/" + parentCategory.getId());
        doc = Jsoup.parse(page);
        Element span = NullHelper.getFirstNoneNull(doc.getElementById("allSubCats"),
                doc.getElementById("topSubCats"));

        for (Element a : span.getElementsByTag("a")) {
            String id = new StringTrimmer(a.attr("href")).getAfterLast("category/").getBeforeNext("/").toString();
            String name = a.text();
            if (StringUtils.isNumeric(id)) {
                parentCategory.addChild(new BookCategory(Long.parseLong(id), name));
            }
        }
        log.info("Added " + parentCategory.getChildren().size()+" categories.");
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new BookCategoryRetrieverGenerator();
    }

}
