package com.unleashyouradventure.swapi.retriever;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unleashyouradventure.swapi.model.SwBook;
import lombok.Getter;
import lombok.Setter;

import com.unleashyouradventure.swapi.Smashwords;

@Getter
@Setter
public class Book extends SwBook {

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

        public static FileType getByEnding(String ending) {
            for (FileType type : FileType.values()) {
                if (type.name().equalsIgnoreCase(ending))
                    return type;
            }
            return null;
        }
    }

    private byte[] coverImage;
    private boolean isBookOwned;
    private boolean isBookDetailsAdded;
    private Map<FileType, List<Download>> downloads = new HashMap<FileType, List<Download>>();

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getCover_url(ImageSize size) {
        return this.cover_url + size.getEnding();
    }

    public int getPriceInCent() {
        if (this.price.getPrices().isEmpty())
            return 0;
        return (int) (this.price.getPrices().get(0).getAmount() * 100);
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

    public void setBookDownloads(Map<FileType, List<Download>> downloads) {
        this.downloads = downloads;
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

    public String getUrlForBookDetails() {
        return Smashwords.BASE_URL + "/books/view/" + this.id;
    }

    public boolean canBookBeDownloaded() {
        return this.isBookOwned || this.getPriceInCent() == 0;
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

    public boolean isBookOwned() {
        return isBookOwned;
    }
}
