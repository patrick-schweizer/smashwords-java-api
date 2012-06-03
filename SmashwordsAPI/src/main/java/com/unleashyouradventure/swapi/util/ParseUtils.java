package com.unleashyouradventure.swapi.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Element;

public class ParseUtils {
    public static abstract class Parser<T> {
        private final static Logger log = Logger.getLogger(Parser.class.getName());

        public T parse(Element element) {
            try {
                return parseElement(element);
            } catch (Exception e) {
                String elem = (element == null) ? "null" : element.toString();
                log.log(Level.WARNING, "Parse error, Element: " + elem, e);
            }
            return null;
        }

        protected abstract T parseElement(Element element);
    }

    public static Integer parsePrice(String txt) {
        txt = txt.replace(".", "");
        txt = txt.replace(" ", "");
        Integer price = Integer.valueOf(txt);
        return price;
    }

    public static boolean equals(String a, String b) {
        if (a == null)
            return b == null;
        else
            return a.equals(b);
    }
}
