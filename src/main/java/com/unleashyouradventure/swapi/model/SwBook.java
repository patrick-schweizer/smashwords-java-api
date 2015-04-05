package com.unleashyouradventure.swapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SwBook {
    protected long id;
    protected String title;
    protected SwPrices price;
    protected int word_count;
    protected SwLanguage language;
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
    protected List<SwCategory> categories;
    protected List<String> tags;
    protected List<SwAuthor> authors;
    protected SwAuthor publisher;
    protected Map<String, Boolean> edelivery;
    protected List<String> formats;
    protected Map<String, Boolean> rights;
    protected boolean in_cart;
    protected boolean purchased;
    protected boolean in_library;

    public void addAuthor(SwAuthor author) {
        if (this.authors == null)
            this.authors = new ArrayList<SwAuthor>();
        this.authors.add(author);
    }

    public void addPrice(SwPrice price) {
        if (this.price == null) {
            this.price = new SwPrices();
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
