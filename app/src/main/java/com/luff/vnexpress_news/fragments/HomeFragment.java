package com.luff.vnexpress_news.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luff.vnexpress_news.R;
import com.luff.vnexpress_news.ultils.Variables;

/**
 * Created by Anonymous on 12/21/2015.
 */

public class HomeFragment extends Fragment {
    TextView tv;
    int position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home,container,false);
        tv = (TextView) viewRoot.findViewById(R.id.tv);
        position = this.getArguments().getInt(Variables.KEYPAGE);
        tv.setText(position+"");
        tv.setTextSize(30);

        return viewRoot;
    }
}
