package com.luff.vnexpress_news.models;


/**
 * Created by Anonymous on 12/21/2015.
 */
public class SlidingMenuItem {
    private String title;
    private int icon;

    public SlidingMenuItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
