package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */

import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

public class ProfilFragment extends Fragment{

    public ProfilFragment(){}


    ImageView btnEdit;
    ImageView btnEditProfil;

    int id;
    String id_user = "", type="";
    private TextView Pseudo, Email, Adresse, CP, Nom, Prenom, Ville, Pays, Mobile,
            Fixe, Siteweb, Genre, Dispo, Facebook, Twitter, Age, Type_artiste, idWebview;

    ImageView img, editImg;
    private String pseudo = "", nom = "", prenom = "", age = "", email = "", ville = "", adresse = "", cp = "", pays = "",
            telportable = "", telfixe = "", dispo = "", soundcloud = "", siteweb = "", imgUrl = "", genre_musical = "", facebook="", twitter="", type_artiste="";
    ProgressDialog progress;
    private WebView wv;

    String html;

    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Top Rated", "Games", "Movies"};

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        btnEditProfil = (ImageView) rootView.findViewById(R.id.btnEditProfil);


        Bundle var = getActivity().getIntent().getExtras();
        id_user = var.getString("id_user");

        Pseudo = (TextView) rootView.findViewById(R.id.Pseudo);
        Nom = (TextView) rootView.findViewById(R.id.Nom);
        Prenom = (TextView) rootView.findViewById(R.id.Prenom);
        Age = (TextView) rootView.findViewById(R.id.Age);
        Adresse = (TextView) rootView.findViewById(R.id.Adresse);
        CP = (TextView) rootView.findViewById(R.id.CP);
        Ville = (TextView) rootView.findViewById(R.id.Ville);
        Pays = (TextView) rootView.findViewById(R.id.Pays);
        Genre = (TextView) rootView.findViewById(R.id.Genre);
        Dispo = (TextView) rootView.findViewById(R.id.Dispo);
        Facebook = (TextView) rootView.findViewById(R.id.Facebook);
        Twitter = (TextView) rootView.findViewById(R.id.Twitter);
        Siteweb = (TextView) rootView.findViewById(R.id.Siteweb);
        Type_artiste = (TextView) rootView.findViewById(R.id.Type_artiste);
        Fixe = (TextView) rootView.findViewById(R.id.Fixe);
        Mobile = (TextView) rootView.findViewById(R.id.Mobile);
        Email = (TextView) rootView.findViewById(R.id.Email);
        img = (ImageView) rootView.findViewById(R.id.imageView);
        editImg = (ImageView) rootView.findViewById(R.id.imageView2);
        wv = (WebView) rootView.findViewById(R.id.webView);
        idWebview = (TextView) rootView.findViewById(R.id.idWebview);

        View.OnClickListener listnr = new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        type="artiste";

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

        editImg.setOnClickListener(listnr);


        btnEditProfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // Create new fragment and transaction
                Fragment newFragment = new EditArtistProfilFragment();
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


        return rootView;
    }

    private class ReadProfilTask extends AsyncTask<Void, Void, InputStream> {
        int i;
        String result = null;
        String tag = "read_ArtistProfil";
        InputStream is = null;
        List<NameValuePair> nameValuePairs;

        protected InputStream doInBackground(Void... params) {
            //setting nameValuePairs
            nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("id_user", id_user));

            //setting the connection to the database
            try {
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
                StringBuilder total = new StringBuilder();
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                Bitmap imgurl;
                // Access by key : value
                for (i = 0; i < finalResult.length(); i++) {
                    JSONObject element = finalResult.getJSONObject(0);

                    pseudo = element.getString("pseudo");
                    soundcloud = element.getString("soundcloud");
                    email = element.getString("email");
                    adresse = element.getString("adresse");
                    cp = element.getString("code_postal");
                    nom = element.getString("nom");
                    prenom = element.getString("prenom");
                    ville = element.getString("ville");
                    pays = element.getString("pays");
                    telportable = element.getString("tel_portable");
                    telfixe = element.getString("tel_fixe");
                    dispo = element.getString("disponibilites");
                    siteweb = element.getString("siteweb");
                    imgUrl = element.getString("image_url");
                    genre_musical = element.getString("genre_musical");
                    age = element.getString("age");
                    facebook=element.getString("facebook");
                    twitter=element.getString("twitter");
                    type_artiste=element.getString("type_artiste");

                    if (imgUrl.length() != 0) {
                        InputStream in = new java.net.URL(imgUrl).openStream();
                        imgurl = BitmapFactory.decodeStream(in);

                        img.setImageBitmap(getCircularBitmapWithBorder(imgurl, 1, Color.rgb(232,126,4)));


                    }

                    Nom.setText(nom);
                    Prenom.setText(prenom);
                    Pseudo.setText(pseudo);
                    Age.setText(age);
                    genre_musical = genre_musical.replace(String.valueOf("["), "");
                    genre_musical = genre_musical.replace(String.valueOf("]"), "");
                    Genre.setText(genre_musical);
                    Email.setText(email);
                    Adresse.setText(adresse);
                    CP.setText(cp);
                    Ville.setText(ville);
                    Pays.setText(pays);
                    dispo = dispo.replace(String.valueOf("["), "");
                    dispo = dispo.replace(String.valueOf("]"), "");
                    Dispo.setText(dispo);
                    Mobile.setText("0"+telportable);
                    Fixe.setText("0"+telfixe);
                    Siteweb.setText(siteweb);
                    Facebook.setText(facebook);
                    Twitter.setText(twitter);
                    Type_artiste.setText(type_artiste);
                    idWebview.setText(soundcloud);


                }
                if(telfixe.toString().equals("0")){
                    Fixe.setText("Inconnu");
                }
                if(telportable.toString().equals("0")){
                    Mobile.setText("Inconnu");
                }
                if (soundcloud.length() != 0) {
                    html = "<iframe width=\"100%\" height=\"400\" scrolling=\"yes\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/" + soundcloud + "&amp;auto_play=false&amp;hide_related=false&amp;show_comments=false&amp;show_user=false&amp;show_reposts=false&amp;show_artwork=false&amp;buying=false\"></iframe>";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                } else {
                    html = "Vous n'avez pas renseigne l\'ID de votre playlist Soundcloud";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

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
    public static Bitmap getCircularBitmapWithBorder(Bitmap bitmap,
                                                     int borderWidth, int bordercolor) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f
                : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(bordercolor);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2,
                paint);
        return canvasBitmap;
    }


}