package com.blinky.peestash.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
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

public class ArtistHomeActivity extends Activity {
    int id;
    String id_user = "";
    private TextView Pseudo, Email, Adresse, CP, Nom, Prenom, Ville, Pays, Mobile,
            Fixe, Siteweb, Genre, Dispo, Facebook, Age;

    ImageView img;
    private String pseudo = "", nom = "", prenom = "", age = "", email = "", ville = "", adresse = "", cp = "", pays = "",
            telportable = "", telfixe = "", dispo = "", soundcloud = "", siteweb = "", imgUrl = "", genre_musical = "";
    ProgressDialog progress;
    private WebView wv;
    private Button btn_editProfil;

    String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_home);

        Bundle var = this.getIntent().getExtras();
        id_user=var.getString("id_user");

        Pseudo = (TextView) findViewById(R.id.Pseudo);
        Nom = (TextView) findViewById(R.id.Nom);
        Prenom = (TextView) findViewById(R.id.Prenom);
        Age = (TextView) findViewById(R.id.Age);
        Adresse = (TextView) findViewById(R.id.Adresse);
        CP = (TextView) findViewById(R.id.CP);
        Ville = (TextView) findViewById(R.id.Ville);
        Pays = (TextView) findViewById(R.id.Pays);
        Genre = (TextView) findViewById(R.id.Genre);
        Dispo = (TextView) findViewById(R.id.Dispo);
        Facebook = (TextView) findViewById(R.id.Facebook);
        Siteweb = (TextView) findViewById(R.id.Siteweb);
        Fixe = (TextView) findViewById(R.id.Fixe);
        Mobile = (TextView) findViewById(R.id.Mobile);
        Email = (TextView) findViewById(R.id.Email);
        img = (ImageView) findViewById(R.id.imageView);
        wv = (WebView)findViewById(R.id.webView);

        btn_editProfil = (Button) findViewById(R.id.btneditProfil);

        // On met un Listener sur le bouton Artist
        btn_editProfil.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(ArtistHomeActivity.this, EditArtistProfilActivity.class);
                i.putExtra("id_user", id_user);
                startActivity(i);

            }
        });

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new ReadProfilTask().execute();
                    }
                });
            }
        }).start();

    }
    private class ReadProfilTask extends AsyncTask<Void, Void, InputStream> {
        int i;
        String result = null;
        String tag = "read_ArtistProfil";
        InputStream is = null;
        List<NameValuePair> nameValuePairs;

        protected InputStream doInBackground(Void ... params) {
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
            progress = new ProgressDialog(ArtistHomeActivity.this);
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

                    if(imgUrl.length()!=0)
                    {
                        InputStream in = new java.net.URL(imgUrl).openStream();
                        imgurl = BitmapFactory.decodeStream(in);
                        img.setImageBitmap(imgurl);
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
                    Mobile.setText(telportable);
                    Fixe.setText(telfixe);

                    Siteweb.setText(siteweb);

                }
                if(soundcloud.length()!=0) {
                    html = "<iframe width=\"100%\" height=\"400\" scrolling=\"yes\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/" + soundcloud + "&amp;auto_play=false&amp;hide_related=false&amp;show_comments=false&amp;show_user=false&amp;show_reposts=false&amp;show_artwork=false&amp;buying=false\"></iframe>";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                }else {
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
}