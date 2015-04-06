package com.unleashyouradventure.swapi.util;

import org.jsoup.nodes.Element;

import java.util.logging.Level;
import java.util.logging.Logger;

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
            return getDefaultInCaseOfError();
        }

        protected abstract T parseElement(Element element);

        protected T getDefaultInCaseOfError() {
            return null;
        }
    }

    public static Double parsePrice(String txt) {
        String trimmedInput = txt.trim();
        trimmedInput = txt.replace(",", "");
        return Double.valueOf(trimmedInput);
    }

    public static boolean equals(String a, String b) {
        if (a == null)
            return b == null;
        else
            return a.equals(b);
    }
}
