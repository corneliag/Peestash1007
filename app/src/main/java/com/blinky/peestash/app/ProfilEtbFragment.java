package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.blinky.peestash.app.EditArtistProfilActivity;
import com.blinky.peestash.app.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.blinky.peestash.app.ProfilFragment.getCircularBitmapWithBorder;

public class ProfilEtbFragment extends Fragment {

    public ProfilEtbFragment(){}

    private ImageView editProfil;
    ImageView btnEdit;
    String id_user="",type="";
    private TextView Nom, Adresse, CP, Ville, Pays, Mobile,
            Fixe, Email, Siteweb, Facebook, Twitter, Genre_musical, Type_etab;
    ImageView img, editImage;
    private String nom = "", email = "", ville = "", adresse = "", cp = "", pays = "",
            telportable = "", telfixe = "", siteweb = "", imgUrl = "", facebook = "", twitter="", description = "", genre="", type_etab;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_etb_profil, container, false);

        Bundle var = getActivity().getIntent().getExtras();
        id_user=var.getString("id_user");

        Nom = (TextView) rootView.findViewById(R.id.Nom);
        Adresse = (TextView) rootView.findViewById(R.id.Adresse);
        CP = (TextView) rootView.findViewById(R.id.CP);
        Ville = (TextView) rootView.findViewById(R.id.Ville);
        Pays = (TextView) rootView.findViewById(R.id.Pays);
        Facebook = (TextView) rootView.findViewById(R.id.Facebook);
        Twitter =(TextView) rootView.findViewById(R.id.Twitter);
        Siteweb = (TextView) rootView.findViewById(R.id.Siteweb);
        Fixe = (TextView) rootView.findViewById(R.id.Fixe);
        Mobile = (TextView) rootView.findViewById(R.id.Mobile);
        Email = (TextView) rootView.findViewById(R.id.Email);
        Genre_musical = (TextView) rootView.findViewById(R.id.Genre);
        Type_etab =(TextView) rootView.findViewById(R.id.typeetab);
        img = (ImageView) rootView.findViewById(R.id.imageView);
        editImage = (ImageView) rootView.findViewById(R.id.imageView2);

        editProfil = (ImageView) rootView.findViewById(R.id.editprofil);

        editProfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Fragment newFragment = new EditEtbProfilFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_user", id_user);
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.frame_container, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });
        new Thread(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new ReadProfilTask().execute();
                    }
                });
            }
        }).start();

        //gestion des onglets
        final Button testtab1 = (Button) rootView.findViewById(R.id.testtab1);
        final Button testtab2 = (Button) rootView.findViewById(R.id.testtab2);
        final Button testtab3 = (Button) rootView.findViewById(R.id.testtab3);

        final LinearLayout propLayout1 = (LinearLayout) rootView.findViewById(R.id.properLayout1);
        final LinearLayout propLayout2 = (LinearLayout) rootView.findViewById(R.id.properLayout2);
        final LinearLayout propLayout3 = (LinearLayout) rootView.findViewById(R.id.properLayout3);
        testtab1.setPressed(true);
        testtab2.setPressed(false);
        testtab3.setPressed(false);

        testtab1.setOnTouchListener
                (
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                testtab1.setPressed(true);
                                testtab2.setPressed(false);
                                testtab3.setPressed(false);

                                propLayout1.setVisibility(View.VISIBLE);
                                propLayout2.setVisibility(View.INVISIBLE);
                                propLayout3.setVisibility(View.INVISIBLE);

                                return true;
                            }
                        }
                );
        testtab2.setOnTouchListener
                (
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                testtab2.setPressed(true);
                                testtab1.setPressed(false);
                                testtab3.setPressed(false);

                                propLayout2.setVisibility(View.VISIBLE);
                                propLayout1.setVisibility(View.INVISIBLE);
                                propLayout3.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        }
                );
        testtab3.setOnTouchListener
                (
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                testtab3.setPressed(true);
                                testtab2.setPressed(false);
                                testtab1.setPressed(false);

                                propLayout3.setVisibility(View.VISIBLE);
                                propLayout1.setVisibility(View.INVISIBLE);
                                propLayout2.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        }
                );

        View.OnClickListener listnr = new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        type="etablissement";

                        // Create new fragment and transaction
                        Fragment newFragment = new UploadFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id_user", id_user);
                        bundle.putString("type", type);
                        newFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.frame_container, newFragment);
                        transaction.addToBackStack(null);
                        // Commit the transaction
                        transaction.commit();

                    }
                }).start();
            }

        };

        editImage.setOnClickListener(listnr);

        return rootView;
    }

    private class ReadProfilTask extends AsyncTask<Void, Void, InputStream> {

        int i;
        String result = null;
        protected InputStream doInBackground(Void ... params) {

            String tag = "read_EtablissementProfil";
            InputStream is = null;
            List<NameValuePair> nameValuePairs;

            //setting the connection to the database
            try {
                //setting nameValuePairs
                nameValuePairs = new ArrayList<NameValuePair>(1);
                //adding string variables into the NameValuePairs
                nameValuePairs.add(new BasicNameValuePair("tag", tag));
                nameValuePairs.add(new BasicNameValuePair("id_user", id_user));

                //Setting up the default http client
                HttpClient httpClient = new DefaultHttpClient();

                //setting up the http post method
                HttpPost httpPost = new HttpPost("http://peestash.peestash.fr/index.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //getting the response
                HttpResponse response = httpClient.execute(httpPost);

                //setting up the entity
                HttpEntity entity = response.getEntity();

                //setting up the content inside the input stream reader
                is = entity.getContent();


            } catch (ClientProtocolException e) {

                Log.e("ClientProtocole", "Log_tag");
                String msg = "Erreur client protocole";

            } catch (IOException e) {
                Log.e("Log_tag", "IOException");
                e.printStackTrace();
                String msg = "Erreur IOException";
            }
            return is;
        }
        protected void onProgressUpdate(Void params) {

        }
        protected void onPreExecute() {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Chargement de vos informations de profil...");
            progress.show();
        }

        protected void onPostExecute(InputStream is) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                Bitmap imgurl;
                for (i = 0; i < finalResult.length(); i++) {

                    JSONObject element = finalResult.getJSONObject(0);
                    nom = element.getString("nom");
                    email = element.getString("email");
                    adresse = element.getString("adresse");
                    cp = element.getString("code_postal");

                    ville = element.getString("ville");
                    pays = element.getString("pays");
                    telportable = element.getString("tel_portable");
                    telfixe = element.getString("tel_fixe");
                    siteweb = element.getString("siteweb");
                    facebook = element.getString("facebook");
                    twitter = element.getString("twitter");
                    genre = element.getString("genre_musical");
                    type_etab = element.getString("type_etablissement");

                    imgUrl = element.getString("image_url");

                    if(imgUrl.length()!=0)
                    {
                        InputStream in = new java.net.URL(imgUrl).openStream();
                        imgurl = BitmapFactory.decodeStream(in);
                        img.setImageBitmap(getCircularBitmapWithBorder(imgurl, 1, Color.rgb(232,126,4)));

                    }
                    Nom.setText(nom);
                    Email.setText(email);
                    Adresse.setText(adresse);
                    CP.setText(cp);
                    Ville.setText(ville);
                    Pays.setText(pays);
                    Mobile.setText("0"+telportable);
                    Fixe.setText("0"+telfixe);
                    Siteweb.setText(siteweb);
                    Facebook.setText(facebook);
                    Twitter.setText(twitter);
                    Genre_musical.setText(genre.toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), ""));
                    Type_etab.setText(type_etab);

                }
                if(telfixe.toString().equals("0")){
                    Fixe.setText("Inconnu");
                }
                if(telportable.toString().equals("0")){
                    Mobile.setText("Inconnu");
                }

                is.close();

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }
}