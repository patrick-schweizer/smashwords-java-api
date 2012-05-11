package com.unleashyouradventure.swapi.retriever;

import java.io.Serializable;
import java.util.ArrayList;

public class LazyArrayList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String urlForNextSet = null;

    public boolean hasMoreElementsToLoad() {
        return urlForNextSet != null;
    }

    public String getUrlForNextSet() {
        return urlForNextSet;
    }

    public void setUrlForNextSet(String urlForNextSet) {
        this.urlForNextSet = urlForNextSet;
    }

}
