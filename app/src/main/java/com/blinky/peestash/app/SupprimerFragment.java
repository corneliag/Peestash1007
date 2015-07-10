package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blinky.peestash.app.R;

public class SupprimerFragment extends Fragment {

    public SupprimerFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_supprimer, container, false);

        return rootView;
    }
}