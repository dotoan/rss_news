package com.luff.vnexpress_news.ultils;

import android.os.AsyncTask;
import android.util.Log;

import com.luff.vnexpress_news.models.OverviewItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Anonymous on 12/22/2015.
 */
abstract class RssDownloader extends AsyncTask<String,Void,ArrayList<OverviewItem>> {
    private enum RSSXMLTag {
        TITLE, DATE, LINK, IGNORETAG
    }
    private RSSXMLTag currentTag;
    private ArrayList<OverviewItem> rssItemList = new ArrayList<>();
    @Override
    protected ArrayList<OverviewItem> doInBackground(String... params) {
        InputStream is = null;
        String urlStr = params[0];


        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int response = connection.getResponseCode();
            Log.d("debug", "The response is: " + response);
            is = connection.getInputStream();

            // parse xml
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(is, null);

            int eventType = xpp.getEventType();
            OverviewItem rssItem =null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {

                } else if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("item")) {
                        rssItem = new OverviewItem();
                        currentTag = RSSXMLTag.IGNORETAG;
                    } else if (xpp.getName().equals("title")) {
                        currentTag = RSSXMLTag.TITLE;
                    } else if (xpp.getName().equals("link")) {
                        currentTag = RSSXMLTag.LINK;
                    } else if (xpp.getName().equals("pubDate")) {
                        currentTag = RSSXMLTag.DATE;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equals("item")) {
                        rssItemList.add(rssItem);
                    } else {
                        currentTag = RSSXMLTag.IGNORETAG;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    String content = xpp.getText();
                    content = content.trim();
                    Log.d("debug", content);
                    if (rssItem != null) {
                        switch (currentTag) {
                            case TITLE:
                                if (content.length() != 0) {
                                    if (rssItem.getTitle() != null) {
                                        rssItem.setTitle(rssItem.getTitle()+ content);
                                    } else {
                                        rssItem.setTitle(content);
                                    }
                                }
                                break;
                            case LINK:
                                if (content.length() != 0) {
                                    if (rssItem.getLink() != null) {
                                        rssItem.setLink(rssItem.getLink() +content);
                                    } else {
                                        rssItem.setLink(content);
                                    }
                                }
                                break;
                            case DATE:
                                if (content.length() != 0) {
                                    if (rssItem.getPubDate() != null) {
                                        rssItem.setPubDate(rssItem.getPubDate() + content);
                                    } else {
                                        rssItem.setPubDate(rssItem + content);
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                eventType = xpp.next();
            }
            Log.v("tst", String.valueOf((rssItemList.size())));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return rssItemList;
    }
    public abstract void onPostExecute(ArrayList<OverviewItem> result);
}
