package com.luff.vnexpress_news.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luff.vnexpress_news.R;

/**
 * Created by Anonymous on 12/21/2015.
 */
public class BussinessFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_bussiness,container,false);
        return viewRoot;
    }
}
