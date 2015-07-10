package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.blinky.peestash.app.R;

public class LogoutEtbFragment extends Fragment {

    public LogoutEtbFragment(){}

    ImageView logout;
    TextView txtLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_etb_logout, container, false);
        logout = (ImageView)rootView.findViewById(R.id.logout);
        txtLabel = (TextView)rootView.findViewById(R.id.txtLabel);


        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(getActivity())
                        .setMessage("Voulez-vous vraiment vous deconnecter ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getActivity(), SlideActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();


            }
        });
        txtLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(getActivity())
                        .setMessage("Voulez-vous vraiment vous deconnecter ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getActivity(), SlideActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();

            }
        });


        return rootView;
    }
}