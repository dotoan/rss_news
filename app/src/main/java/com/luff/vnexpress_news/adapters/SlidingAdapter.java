package com.luff.vnexpress_news.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luff.vnexpress_news.R;
import com.luff.vnexpress_news.models.SlidingMenuItem;

import java.util.ArrayList;

/**
 * Created by Anonymous on 12/21/2015.
 */
public class SlidingAdapter extends BaseAdapter {
    Context context;
    ArrayList<SlidingMenuItem> listItem;

    public SlidingAdapter(Context context, ArrayList<SlidingMenuItem> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int i) {
        return listItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null ){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sliding_menu_item,null);
        }
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        TextView title = (TextView)view.findViewById(R.id.title);
        icon.setImageResource(listItem.get(i).getIcon());
        title.setText(listItem.get(i).getTitle());
        return view;
    }
}
