package hwdsb.theofficialwestmountapp;

import java.util.Date;

/**
 * Created by danielsong on 2017-01-18.
 */

public class FeedItem {

    String title;
    String link;
    Date pubDate;

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
