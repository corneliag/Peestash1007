package com.blinky.peestash.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nelly on 26/04/2015.
 */
public class LayoutFour extends Fragment {

    public static Fragment newInstance(Context context) {
        LayoutFour f = new LayoutFour();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.layout_four, null);
        return root;
    }

}