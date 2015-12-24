package com.luff.vnexpress_news.ultils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anonymous on 12/23/2015.
 */
public class BitmapDownloader implements Runnable {
    private String link;

    public BitmapDownloader(String link) {
        this.link = link;
    }

    public  Bitmap getBitmapFromUrl()
    {
        Bitmap bm = null;
        URL url = null;
        try {

            url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != 200) {
                return bm;
            }
            conn.connect();
            InputStream is = conn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            try {
                bm = BitmapFactory.decodeStream(bis);
            } catch (OutOfMemoryError ex) {
                bm = null;
            }
            bis.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    @Override
    public void run() {

    }
}
