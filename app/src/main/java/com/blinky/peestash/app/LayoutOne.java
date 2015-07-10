package com.blinky.peestash.app;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nelly on 26/04/2015.
 */
public class LayoutOne extends Fragment {

    public static Fragment newInstance(Context context) {
        LayoutOne f = new LayoutOne();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.layout_one, null);
        return root;
    }

}