package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
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

public class HomeFragment extends Fragment {

    public HomeFragment(){}

    String id_user = "",msg, tag;
    private TextView Email, Adresse, CP, Nom, Ville, Pays, Mobile,
            Fixe, Siteweb, Genre, Facebook, Twitter, Type_etab;
    List<String> etablissement_id, idEtb, nom, adresse, ville, pays, cp, email, telportable, telfixe, facebook, twitter, siteweb, imgUrl, genre_musical, type_etablissement;
    ImageView img;
    private TextView Demande_inscription, Nb_events, Titre, Date_debut, Date_fin, Heure_Debut, Heure_fin, Statut;
    int nbreponse;
    private List<String> titre, date_debut, statut_recrutement, id_etablissement;
    ProgressDialog progress;
    ProgressDialog progress2;
    Bitmap imgurl;
    ImageView btnAddContact;
    ImageView fixeVisuel;
    ImageView imgFacebook;
    ImageView imgTwitter;
    ImageView imgSite;
    ImageView btnFavoris;


    private LinearLayout listeEvents;
    private int layoutHeight, i=0;
    private LinearLayout.LayoutParams listeEventsParams;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    String id_artist;

    String idEtablissement;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        btnFavoris = (ImageView) rootView.findViewById(R.id.addFavoris);

        list = (ListView) rootView.findViewById(android.R.id.list);

        Bundle var = getActivity().getIntent().getExtras();
        id_user = var.getString("id_user");
        id_artist = id_user;

        Nom = (TextView) rootView.findViewById(R.id.Nom);
        Adresse = (TextView) rootView.findViewById(R.id.Adresse);
        CP = (TextView) rootView.findViewById(R.id.CP);
        Ville = (TextView) rootView.findViewById(R.id.Ville);
        Pays = (TextView) rootView.findViewById(R.id.Pays);
        Genre = (TextView) rootView.findViewById(R.id.Genre);
        Facebook = (TextView) rootView.findViewById(R.id.Facebook);
        Twitter = (TextView) rootView.findViewById(R.id.Twitter);
        Siteweb = (TextView) rootView.findViewById(R.id.Siteweb);
        Fixe = (TextView) rootView.findViewById(R.id.Fixe);
        Mobile = (TextView) rootView.findViewById(R.id.Mobile);
        Email = (TextView) rootView.findViewById(R.id.Email);
        Type_etab = (TextView) rootView.findViewById(R.id.typeetab);
        img = (ImageView) rootView.findViewById(R.id.imageView);


        new Thread(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new ReadProfilTask().execute();
                    }
                });
            }
        }).start();

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                                //Log.i("right to left", "Right to Left");


                                if (i == (nbreponse - 1)) {
                                    i = 0;

                                    afficheProfilContent(i);

                                }else {
                                    i++;

                                    afficheProfilContent(i);

                                }

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                //Log.i("left to rigth", "Left to Right");

                                if(i==0)
                                {
                                    i=nbreponse-1;

                                    afficheProfilContent(i);

                                } else {
                                    i--;

                                    afficheProfilContent(i);

                                }

                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View rootView, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

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

        fixeVisuel = (ImageView) rootView.findViewById(R.id.FixeVisuel);
        ImageView mobileVisuel = (ImageView) rootView.findViewById(R.id.MobileVisuel);
        fixeVisuel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Intent.ACTION_CALL);
                String phNum = "tel:" + Fixe.getText().toString();
                myIntent.setData(Uri.parse(phNum));
                startActivity(myIntent);

            }
        });

        mobileVisuel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Intent.ACTION_CALL);
                String phNum = "tel:" + Mobile.getText().toString();
                myIntent.setData(Uri.parse(phNum));
                startActivity(myIntent);

            }
        });
        ImageView imgMail = (ImageView) rootView.findViewById(R.id.imgEmail);
        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String strEmail = Email.getText().toString();
                i.setData(Uri.fromParts("mailto", strEmail, null));
                startActivity(i);
            }
        });

        imgSite = (ImageView) rootView.findViewById(R.id.imgSite);
        imgTwitter = (ImageView) rootView.findViewById(R.id.imgTwitter);
        imgFacebook = (ImageView) rootView.findViewById(R.id.imgFacebook);
        imgSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSite = Siteweb.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(strSite));
                startActivity(intent);
            }
        });
        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTwitter = "https://twitter.com/"+Twitter.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(strTwitter));
                startActivity(intent);
            }
        });
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strFacebook = "https://www.facebook.com/"+Facebook.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(strFacebook));
                startActivity(intent);
            }
        });
        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strFacebook = "https://www.facebook.com/"+Facebook.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(strFacebook));
                startActivity(intent);
            }
        });
        Twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strFacebook = "https://twitter.com/" + Twitter.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(strFacebook));
                startActivity(intent);
            }
        });

        btnAddContact = (ImageView) rootView.findViewById(R.id.addContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertContact(Nom.getText().toString(), Email.getText().toString(), Mobile.getText().toString());
            }
        });

        return rootView;
    }
    private class ReadProfilTask extends AsyncTask<Void, Void, InputStream> {
        int i;
        String result = null;
        String tag = "read_AllProfil";
        String type = "etablissement";
        InputStream is = null;
        List<NameValuePair> nameValuePairs;

        protected InputStream doInBackground(Void... params) {
            //setting nameValuePairs
            nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("type", type));
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
           // progress.setMessage("Chargement de la liste des etablissements...");
          //  progress.show();
        }

        protected void onPostExecute(InputStream is) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);

                int i=0;
                // Access by key : value
                nbreponse = finalResult.length();
                String nb = String.valueOf(nbreponse);
                //System.out.println("nb reponse "+ nb);

                email = new ArrayList<String>(nbreponse);
                nom = new ArrayList<String>(nbreponse);
                adresse = new ArrayList<String>(nbreponse);
                cp = new ArrayList<String>(nbreponse);
                ville = new ArrayList<String>(nbreponse);
                pays = new ArrayList<String>(nbreponse);
                telportable = new ArrayList<String>(nbreponse);
                telfixe = new ArrayList<String>(nbreponse);
                siteweb = new ArrayList<String>(nbreponse);
                imgUrl = new ArrayList<String>(nbreponse);
                genre_musical = new ArrayList<String>(nbreponse);
                type_etablissement = new ArrayList<String>(nbreponse);
                facebook = new ArrayList<String>(nbreponse);
                twitter = new ArrayList<String>(nbreponse);
                idEtb = new ArrayList<String>(nbreponse);
                etablissement_id = new ArrayList<String>(nbreponse);

                for (i = 0; i < finalResult.length(); i++) {

                    JSONObject element = finalResult.getJSONObject(i);
                    email.add(element.getString("email"));
                    nom.add(element.getString("nom"));
                    adresse.add(element.getString("adresse"));
                    cp.add(element.getString("code_postal"));
                    ville.add(element.getString("ville"));
                    pays.add(element.getString("pays"));
                    telportable.add(element.getString("tel_portable"));
                    telfixe.add(element.getString("tel_fixe"));
                    siteweb.add(element.getString("siteweb"));
                    imgUrl.add(element.getString("image_url"));
                    facebook.add(element.getString("facebook"));
                    twitter.add(element.getString("twitter"));
                    type_etablissement.add(element.getString("type_etablissement"));
                    genre_musical.add(element.getString("genre_musical"));
                    idEtb.add(element.getString("id"));
                    etablissement_id.add(element.getString("id"));


                }
                i=0;
                afficheProfilContent(i);


                is.close();

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }

        }
    }
    private class ShowAllEventsTask extends AsyncTask<Void, Void, InputStream> {

        String result = null;
        String tag = "read_AllEtablissementEvents";
        String type="etablissement";
        InputStream is = null;
        List<NameValuePair> nameValuePairs;

        protected InputStream doInBackground(Void... params) {
            //setting nameValuePairs
            nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("id_etablissement", idEtablissement));
            nameValuePairs.add(new BasicNameValuePair("type", type));

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
            progress2 = new ProgressDialog(getActivity());
            progress2.setMessage("Chargement de la liste des évènements...");
            progress2.show();
        }

        protected void onPostExecute(InputStream is) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);

                int i=0;
                // Access by key : value
                nbreponse = finalResult.length();
                String nb = String.valueOf(nbreponse);
                System.out.println("nb reponse evenements etablissements "+ nb);

                titre = new ArrayList<String>(nbreponse);
                ville = new ArrayList<String>(nbreponse);
                pays = new ArrayList<String>(nbreponse);
                date_debut = new ArrayList<String>(nbreponse);
                imgUrl = new ArrayList<String>(nbreponse);
                genre_musical = new ArrayList<String>(nbreponse);
                statut_recrutement = new ArrayList<String>(nbreponse);
                //id_etablissement = new ArrayList<String>(nbreponse);

                for (i = 0; i < finalResult.length(); i++) {

                    JSONObject element = finalResult.getJSONObject(i);

                    titre.add(element.getString("titre"));
                    date_debut.add(element.getString("date_debut"));
                    ville.add(element.getString("ville"));
                    pays.add(element.getString("pays"));
                    imgUrl.add(element.getString("img_url"));
                    genre_musical.add(element.getString("genre_musical"));
                    statut_recrutement.add(element.getString("statut_recrutement"));
                   // id_etablissement.add(element.getString("id_etablissement"));

                }
                /*for(i=0;i<finalResult.length();i++) {
                    afficherEvent(i);
                }*/

                is.close();

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress2.isShowing()) {
                progress2.dismiss();
            }

        }
    }
    protected  void afficherEvent(int i) {


        arrayList.add(titre.get(i).toString().toUpperCase() + "\n" + "Date : " + date_debut.get(i).toString()
                + "\n" + ville.get(i).toString().toUpperCase() + ", " + pays.get(i).toString().toUpperCase()
                + "\n" + "Genre : " + genre_musical.get(i).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), "")
                + "\n" + "Statut du recrutement : " + statut_recrutement.get(i).toString());

        list.setAdapter(adapter);
    }

    protected void afficheProfilContent(final int i)
    {

        Log.e("event ", etablissement_id.get(i).toString());
        idEtablissement = etablissement_id.get(i).toString();
        new Thread(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new ShowAllEventsTask().execute();
                    }
                });
            }
        }).start();
        Email.setText(email.get(i).toString());
        Nom.setText(nom.get(i).toString());
        Adresse.setText(adresse.get(i).toString());
        CP.setText(cp.get(i).toString());
        Ville.setText(ville.get(i).toString());
        Pays.setText(pays.get(i).toString());
        Siteweb.setText(siteweb.get(i).toString());
        Fixe.setText("0"+telfixe.get(i).toString());
        Mobile.setText("0"+telportable.get(i).toString());
        Facebook.setText(facebook.get(i).toString());
        Twitter.setText(twitter.get(i).toString());

        Genre.setText(genre_musical.get(i).toString().replace(String.valueOf("["), "").replace(String.valueOf("]"), ""));

        Type_etab.setText(type_etablissement.get(i).toString());

        btnFavoris.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AddEtbFavoriteTask(etablissement_id.get(i).toString(), id_artist);
                Toast.makeText(getActivity(), "Ajoute a vos favoris !", Toast.LENGTH_LONG).show();

            }
        });

        if(telfixe.get(i).toString().equals("0")){
            Fixe.setText("Inconnu");
        }
        if(telportable.get(i).toString().equals("0")){
            Mobile.setText("Inconnu");
        }
        if(type_etablissement.get(i).toString().equals("")){
            Type_etab.setText("Inconnu");
        }

        if(imgUrl.get(i).toString().length() != 0) {
            InputStream in = null;
            try {
                in = new java.net.URL(imgUrl.get(i).toString()).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgurl = BitmapFactory.decodeStream(in);

            img.setImageBitmap(getCircularBitmapWithBorder(imgurl, 1, Color.rgb(232,126,4)));

        }else
        {
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_profil_etb));

        }
    }

    public void insertContact(String name, String email, String tel) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, tel);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    protected void AddEtbFavoriteTask(String id_etablissement, String id_artist) {

        tag = "etablissement_favoris";
        InputStream is = null;
        String favorisOk = "no";

        //setting nameValuePairs
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        //adding string variables into the NameValuePairs
        nameValuePairs.add(new BasicNameValuePair("tag", tag));
        nameValuePairs.add(new BasicNameValuePair("id_etablissement", id_etablissement));
        nameValuePairs.add(new BasicNameValuePair("id_artist", id_artist));

        try {
            favorisOk = "ok";
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

            //displaying a toast message if the data is entered in the database
            msg = "Données enregistrées en BDD favoris etablissement";
            //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            is.close();

        } catch (ClientProtocolException e) {
            Log.e("ClientProtocole", "Log_tag");
            msg = "Erreur client protocole";
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("Log_tag", "IOException");
            e.printStackTrace();
            msg = "Erreur IOException";
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }


    }
}