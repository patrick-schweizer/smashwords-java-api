package com.unleashyouradventure.swapi.retriever;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unleashyouradventure.swapi.Smashwords;

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

    public enum ImageSize {
        tiny, thumb, full("");
        final String ending;

        ImageSize() {
            this.ending = "-" + name();
        }

        ImageSize(String ending) {
            this.ending = ending;
        }

        public String getEnding() {
            return this.ending;
        }
    }

    public enum FileType {
        MOBI("Kindle"), Epub, PDF, RTF, LRF;
        final String smashwordsName;

        FileType() {
            this.smashwordsName = name();
        }

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
    private String descriptionShort;
    private String coverUrl;
    private byte[] coverImage;
    private int priceInCent;
    private boolean isBookOwned;
    private boolean isBookDetailsAdded;
    private Map<FileType, List<Download>> downloads = new HashMap<FileType, List<Download>>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverUrl(ImageSize size) {
        return coverUrl + size.getEnding();
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

    public void setBookDetailsAdded(boolean isBookDetailsAdded) {
        this.isBookDetailsAdded = isBookDetailsAdded;
    }

    public boolean canBookBeBought() {
        return !isBookOwned && getPriceInCent() > 0;
    }

    public String getUrlForPuttingThisBookInShoppingCart() {
        return Smashwords.BASE_URL + "/cart/add/" + this.id;
    }

    public boolean canBookBeDownloaded() {
        return this.isBookOwned || this.priceInCent == 0;
    }

    public Download getDownloadLinkForNewestVersion(FileType fileType) {
        List<Book.Download> downloads = this.downloads.get(fileType);
        if (downloads != null && !downloads.isEmpty())
            return downloads.get(0);
        return null;
    }

    public Set<FileType> getFileTypes() {
        return this.downloads.keySet();
    }
}
