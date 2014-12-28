package com.unleashyouradventure.swapi.retriever.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class JBook {
    protected long id;
    protected String title;
    protected JPrices price;
    protected int word_count;
    protected JLanguage language;
    protected long preorder_date;
    protected long publication_date;
    /** -1 indicates no rating yet */
    protected double rating = -1;
    protected boolean published;
    protected int sampling;
    protected String short_description;
    protected String long_description;
    protected String cover_url;
    protected String cover_thumbnail_url;
    protected boolean adult;
    protected List<JCategory> categories;
    protected List<String> tags;
    protected List<Author> authors;
    protected Author publisher;
    protected Map<String, Boolean> edelivery;
    protected List<String> formats;
    protected Map<String, Boolean> rights;
    protected boolean in_cart;
    protected boolean purchased;
    protected boolean in_library;

    public void addAuthor(Author author) {
        if (this.authors == null)
            this.authors = new ArrayList<Author>();
        this.authors.add(author);
    }

    public void addPrice(JPrice price) {
        if (this.price == null) {
            this.price = new JPrices();
        }
        this.price.getPrices().add(price);
    }

    public String toString() {
        return "Book[" + this.title + "]";
    }

    /** Convienience method for: this.authors.get(0).getDisplay_name(), avoids Nullpointer */
    public String getFirstAuthorDisplayName() {
        return (this.authors == null || this.authors.isEmpty()) ? null : this.authors.get(0).getDisplay_name();
    }
}
