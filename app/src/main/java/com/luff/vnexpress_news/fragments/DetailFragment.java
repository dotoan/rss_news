package com.luff.vnexpress_news.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luff.vnexpress_news.R;
import com.luff.vnexpress_news.models.OverviewItem;
import com.luff.vnexpress_news.ultils.BitmapDownloader;
import com.luff.vnexpress_news.ultils.Variables;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
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
public class DetailFragment extends Fragment {
    TextView tvDetail;
    ImageView imIcon;
    int position;

    private enum RSSXMLTag {
        TITLE, DATE, LINK, DESCRIPTION, IGNORETAG
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        tvDetail = (TextView) rootView.findViewById(R.id.tv_detail);
        imIcon = (ImageView) rootView.findViewById(R.id.im_icon);
        position = this.getArguments().getInt(Variables.KEYPAGE);
        //tvDetail.setText(position+"");
        new RssDownloader().execute(Variables.VNEXPRESS_LINK[position]);
        return rootView;
    }

    private class RssDownloader extends AsyncTask<String, Bitmap, ArrayList<OverviewItem>> {

        private RSSXMLTag currentTag;
        private ArrayList<OverviewItem> rssItemList = new ArrayList<>();

        @Override
        protected ArrayList<OverviewItem> doInBackground(String... params) {
            InputStream is = null;
            String urlStr = params[0];


            try {
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
                OverviewItem rssItem = null;
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
                        } else if (xpp.getName().equals("description")) {
                            currentTag = RSSXMLTag.DESCRIPTION;
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
                                            rssItem.setTitle(rssItem.getTitle() + content);
                                        } else {
                                            rssItem.setTitle(content);
                                        }
                                    }
                                    break;
                                case LINK:
                                    if (content.length() != 0) {
                                        if (rssItem.getLink() != null) {
                                            rssItem.setLink(rssItem.getLink() + content);
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
                                case DESCRIPTION:
                                    int token = xpp.nextToken();
                                    while (token != XmlPullParser.CDSECT) {
                                        token = xpp.nextToken();
                                    }
                                    String cdata = xpp.getText();
                                    Log.i("Info", cdata);
                                    String image = cdata.substring(cdata.indexOf("src=") + 5, cdata.indexOf("jpg") + 3);
                                    Log.i("Info", image);
                                    //rssItem.setIcon(getBitmapFromUrl(image));
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

        public void onPostExecute(ArrayList<OverviewItem> result) {
            String context = "";
            for (int i = 0; i < result.size(); i++) {
                context += result.get(i).getLink() + "\n";
            }
            tvDetail.setText(context);
            tvDetail.setTextSize(20);
            imIcon.setImageBitmap(result.get(0).getIcon());

        }
    }

    public Bitmap getBitmapFromUrl(String image) {
        Bitmap bm = null;
        URL url = null;
        try {

            url = new URL(image);
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
}
