package com.unleashyouradventure.swapi.retriever;

import java.util.ArrayList;
import java.util.List;

public class BookCategory {
    private final long id;
    private final String title;
    private List<BookCategory> children = new ArrayList<BookCategory>();
    private BookCategory parent;

    public BookCategory(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String toString() {
        return title;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof BookCategory))
            return false;
        return this.getId() == ((BookCategory) o).getId();
    }

    public int hashCode() {
        return (int) this.getId();
    }

    public void addChild(BookCategory childCategory) {
        this.children.add(childCategory);
        childCategory.parent = this;
    }

    public BookCategory getParent() {
        return parent;
    }

    public List<BookCategory> getChildren() {
        return children;
    }
}
