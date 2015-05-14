package com.unleashyouradventure.swapi.retriever.parser.list;

import com.unleashyouradventure.swapi.model.SwPrice;
import com.unleashyouradventure.swapi.util.ParseUtils;
import com.unleashyouradventure.swapi.util.StringTrimmer;
import org.jsoup.nodes.Element;

public class PriceParser extends ParseUtils.Parser<SwPrice> {

    @Override
    protected SwPrice parseElement(Element element) {
        SwPrice price = new SwPrice();
        price.setAmount(0);
        price.setCurrency("USD");
        if (element == null) {
            return price;
        }
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
}