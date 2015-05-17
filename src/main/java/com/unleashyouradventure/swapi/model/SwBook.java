package com.unleashyouradventure.swapi.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    protected List<SwPerson> contributors;
    protected SwAuthor publisher;
    protected Map<String, Boolean> edelivery;
    protected List<String> formats;
    protected Map<String, Boolean> rights;
    protected boolean in_cart;
    protected boolean purchased;
    protected boolean in_library;

    public void addContributor(SwPerson person) {
        if (this.contributors == null)
            this.contributors = new ArrayList<SwPerson>();
        this.contributors.add(person);
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

    /** Convienience method for: this.contributors.get(0).getAccount().getDisplay_name(), avoids Nullpointer */
    public String getFirstAuthorDisplayNameOrNull() {
        if(this.contributors == null || this.contributors.isEmpty()) {
            return null;
        }
        if(this.contributors.get(0).getAccount() == null) {
            return null;
        }
        return this.contributors.get(0).getAccount().getDisplay_name();
    }

    public int getPriceInCent() {
        if (this.price.getPrices().isEmpty())
            return 0;
        return (int) (this.price.getPrices().get(0).getAmount() * 100);
    }

    public String getCover_url(ImageSize size) {
        return this.cover_url + size.getEnding();
    }
}
