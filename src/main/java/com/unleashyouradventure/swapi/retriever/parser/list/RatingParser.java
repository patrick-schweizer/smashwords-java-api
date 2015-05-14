package com.unleashyouradventure.swapi.retriever.parser.list;

import com.unleashyouradventure.swapi.util.ParseUtils;
import com.unleashyouradventure.swapi.util.StringTrimmer;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RatingParser extends ParseUtils.Parser<Double> {

        @Override
        protected Double parseElement(Element element) {
            if (element == null) {
                return null;
            }

            Elements
                    elements = element.select("span[style=color: #888;]");
            if (elements.isEmpty()) {
                return getDefaultInCaseOfError(); // no rating
            } else {
                String text = new StringTrimmer(elements.first().text()).getAfterNext("(").getBeforeNext(" from").getBeforeNext(")").toString().trim();
                return Double.parseDouble(text);
            }
        }

        @Override
        protected Double getDefaultInCaseOfError() {
            return (double) -1;
        }
    };