package com.luff.vnexpress_news.models;

import android.graphics.Bitmap;

/**
 * Created by Anonymous on 12/22/2015.
 */
public class OverviewItem {
    private String title;
    private String pubDate;
    private String link;
    private Bitmap icon;
    private String src;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "OverviewItem{" +
                "src='" + src + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}
