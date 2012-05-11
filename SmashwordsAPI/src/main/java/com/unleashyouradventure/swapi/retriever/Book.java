package com.unleashyouradventure.swapi.retriever;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

    public static class Download {
        private final String title;
        private final URL url;

        public Download(String title, URL url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public URL getUrl() {
            return url;
        }

    }

    public enum FileType {
        ;
        final String smashwordsName;

        FileType(String smashwordsName) {
            this.smashwordsName = smashwordsName;
        }

        public String getSmashwordsName() {
            return this.smashwordsName;
        }

    }

    private long id;
    private String title;
    private String author;
    private String description;
    private String coverUrl;
    private byte[] coverImage;
    private int priceInCent;
    private boolean isBookOwned;
    private boolean isBookDetailsAdded;
    private Map<FileType, List<Download>> downloads = new HashMap<FileType, List<Download>>();

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String toString() {
        return "Book[" + this.title + "]";
    }

    public int getPriceInCent() {
        return priceInCent;
    }

    public void setPriceInCent(int priceInCent) {
        this.priceInCent = priceInCent;
    }

    public boolean isBookDetailsAdded() {
        return this.isBookDetailsAdded;
    }

    public void setBookOwned(boolean isBookOwned) {
        this.isBookOwned = isBookOwned;

    }

    public void setBookDownloads(FileType type, List<Download> downloads) {
        this.downloads.put(type, downloads);
    }
}
