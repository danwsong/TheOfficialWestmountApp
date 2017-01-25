package hwdsb.theofficialwestmountapp;

import java.util.Date;

public class FeedItem {

    String title;
    String link;
    Date pubDate;

    // Getters and setters for the titles, descriptions, and publication dates of the feed items
    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

}
