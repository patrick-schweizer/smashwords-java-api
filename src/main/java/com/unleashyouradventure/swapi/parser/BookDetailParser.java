package com.unleashyouradventure.swapi.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unleashyouradventure.swapi.model.SwBook;
import org.apache.commons.lang3.StringUtils;


public class BookDetailParser {

    public SwBook parseDetailPage(String html){
        String json = getJson(html);
        Gson gson = new GsonBuilder().create();
        SwBook swBook = gson.fromJson(json, SwBook.class);
        return swBook;
    }

    private String getJson(String html) {
        String json = StringUtils.substringAfter(html, "window.angularData.book = ");
        json = StringUtils.substringBefore(json, "};")+"}";
        return json;
    }
}
