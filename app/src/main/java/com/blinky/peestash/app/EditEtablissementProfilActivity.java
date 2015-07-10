package com.blinky.peestash.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class EditEtablissementProfilActivity extends Activity implements AdapterView.OnItemSelectedListener {

    String id_user = "", type="";
    private EditText editNom, editAdresse, editCP, editVille, editPays, editMobile,
            editFixe, editSiteweb, editMdp, editFacebook, editConfirmMdp, editEmail, editConfirmEmail;
    TextView affichageEmail;
    private Button btnSave;
    private String nom ="", email ="", confirmEmail="", ville ="", type_etablissement="", adresse ="", cp ="", pays ="",
            telportable ="", telfixe ="", siteweb ="", imgUrl ="", password ="", confirmMdp="", facebook="";
    String msg="";
    ProgressDialog progress;
    ImageView img;
    Verify test = new Verify();
    private CheckBox rock, pop, metal, jazz, funk, electro, blues, rap, folk, classique;
    private String genremusical = "",  genre_musical = "";
    private ArrayList<String> genrelist = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    Spinner spinnerType;
    List<String> genre_etab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_etablissement_profil);
        int i;

        // Spinner element
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        // Spinner Drop down elements
        genre_etab = new ArrayList<String>();

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genre_etab);

        Bundle var = this.getIntent().getExtras();
        id_user = var.getString("id_user");
        editEmail = (EditText) findViewById(R.id.editEmail);
        affichageEmail = (TextView) findViewById(R.id.affichageEmail);
        editConfirmEmail = (EditText) findViewById(R.id.editConfirmEmail);
        editAdresse = (EditText) findViewById(R.id.editAdresse);
        editCP = (EditText) findViewById(R.id.editCP);
        editNom = (EditText) findViewById(R.id.editNom);
        editVille = (EditText) findViewById(R.id.editVille);
        editPays = (EditText) findViewById(R.id.editPays);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editFixe = (EditText) findViewById(R.id.editFixe);
        editSiteweb = (EditText) findViewById(R.id.editSiteweb);
        img = (ImageView) findViewById(R.id.img);
        editFacebook = (EditText) findViewById(R.id.editFB);
        editMdp = (EditText) findViewById(R.id.editMdp);
        editConfirmMdp = (EditText) findViewById(R.id.editConfirmMdp);

        //récupération des checkbox genres musicaux
        rock = (CheckBox) findViewById(R.id.rock);
        pop = (CheckBox) findViewById(R.id.pop);
        metal = (CheckBox) findViewById(R.id.metal);
        rap = (CheckBox) findViewById(R.id.rap);
        funk = (CheckBox) findViewById(R.id.funk);
        classique = (CheckBox) findViewById(R.id.classique);
        blues = (CheckBox) findViewById(R.id.blues);
        electro = (CheckBox) findViewById(R.id.electro);
        folk = (CheckBox) findViewById(R.id.folk);
        jazz = (CheckBox) findViewById(R.id.jazz);

        btnSave = (Button) findViewById(R.id.btnSave);
        addListenerOnChkWindows();
        int id=Integer.parseInt(id_user);

        new ReadProfilTask().execute(id);
        // On met un Listener sur le bouton Artist
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //update datas in database
                InputStream is = null;
                String tag = "update_EtablissementProfil";
                nom = "" + editNom.getText().toString();
                email = "" + editEmail.getText().toString();
                confirmEmail = "" + editConfirmEmail.getText().toString();
                confirmMdp = "" + editConfirmMdp.getText().toString();
                adresse = "" + editAdresse.getText().toString();
                cp = "" + editCP.getText().toString();
                ville = "" + editVille.getText().toString();
                pays = "" + editPays.getText().toString();
                telfixe = "" + editFixe.getText().toString();
                telportable = "" + editMobile.getText().toString();
                siteweb = "" + editSiteweb.getText().toString();
                facebook = "" + editFacebook.getText().toString();
                password = "" + editMdp.getText().toString();
                type_etablissement = "" + type_etablissement.toString();
                genre_musical = "" + genrelist.toString();

                //setting nameValuePairs
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                //adding string variables into the NameValuePairs
                nameValuePairs.add(new BasicNameValuePair("tag", tag));
                nameValuePairs.add(new BasicNameValuePair("id", id_user));
                nameValuePairs.add(new BasicNameValuePair("nom", nom));
                nameValuePairs.add(new BasicNameValuePair("adresse", adresse));
                nameValuePairs.add(new BasicNameValuePair("code_postal", cp));
                nameValuePairs.add(new BasicNameValuePair("ville", ville));
                nameValuePairs.add(new BasicNameValuePair("pays", pays));
                nameValuePairs.add(new BasicNameValuePair("tel_fixe", telfixe));
                nameValuePairs.add(new BasicNameValuePair("tel_portable", telportable));
                nameValuePairs.add(new BasicNameValuePair("siteweb", siteweb));
                nameValuePairs.add(new BasicNameValuePair("facebook", facebook));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("genre_musical", genre_musical));
                nameValuePairs.add(new BasicNameValuePair("type_etablissement", type_etablissement));

                String emailvalid = "ok", passwordvalid = "ok", msg = "";

                if (email != "") {
                    if (test.checkEmailWriting(email)) {

                        if (test.checkEmail(email, confirmEmail)) {
                            emailvalid = "ok";
                            nameValuePairs.add(new BasicNameValuePair("email", email));
                        } else {
                            emailvalid = "no";
                            msg = "Veuillez écrire correctement l'email et la confirmation d'e-mail";
                        }
                    } else {
                        emailvalid = "no";
                        msg = "Veuillez écrire un email correct.";
                    }

                } else {
                    email = "" + affichageEmail.getText().toString();
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                }
                if (password != "") {

                    if (test.checkMdpWriting(password)) {
                        if (test.checkMdp(password, confirmMdp)) {
                            passwordvalid = "ok";
                            nameValuePairs.add(new BasicNameValuePair("password", password));
                        } else {
                            passwordvalid = "no";
                            msg = msg + "Veuillez écrire votre password et votre confirmation de password correctement\n";
                        }

                    } else {
                        passwordvalid = "no";
                        msg = msg + "Votre mot de passe doit contenir au minimum 3 caractères.\n";
                    }
                } else {
                    password = "" + editMdp.getText().toString();
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                }

                if (emailvalid == "ok" && passwordvalid == "ok") {
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

                        //displaying a toast message if the data is entered in the database
                        msg = "Données modifiées en BDD établissement";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    } catch (ClientProtocolException e) {
                        Log.e("ClientProtocole", "Log_tag");
                        msg = "Erreur client protocole";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e("Log_tag", "IOException");
                        e.printStackTrace();
                        msg = "Erreur IOException";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    msg = "";
                }
            }
        });

        Button btn = (Button) findViewById(R.id.btnImage);

        View.OnClickListener listnr = new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        type="etablissement";
                        Intent i = new Intent(EditEtablissementProfilActivity.this, UploadActivity.class);
                        i.putExtra("id_user", id_user);
                        i.putExtra("type", type);
                        startActivity(i);

                    }
                }).start();
            }

        };
        // Spinner click listener
        spinnerType.setOnItemSelectedListener(this);

           genre_etab.add(String.valueOf(""));
           genre_etab.add(String.valueOf("Cafe concert"));
           genre_etab.add(String.valueOf("Salle de concert"));
           genre_etab.add(String.valueOf("Bar"));
           genre_etab.add(String.valueOf("Discotheque"));
           genre_etab.add(String.valueOf("Association"));
           genre_etab.add(String.valueOf("Autre"));


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btn.setOnClickListener(listnr);


    }
    public void addListenerOnChkWindows() {

        //listener sur les checkbox des genres musicaux
        rock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "rock";
                    genrelist.add(genremusical);

                } else {
                    genremusical = "rock";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        pop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "pop";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "pop";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }

            }
        });
        metal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "metal";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "metal";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }

            }
        });
        classique.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "classique";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "classique";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }

            }
        });
        folk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "folk";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "folk";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        funk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "funk";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "funk";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        jazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "jazz";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "jazz";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        rap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "rap";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "rap";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        blues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "blues";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "blues";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
        electro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    genremusical = "electro";
                    genrelist.add(genremusical);
                } else {
                    genremusical = "electro";
                    int pos = genrelist.indexOf(genremusical);
                    genrelist.remove(pos);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        type_etablissement = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ReadProfilTask extends AsyncTask<Integer, Void, InputStream>
    {
        @Override
        protected InputStream doInBackground(Integer... params) {

            //tag récupération des informations de profil établissement
            String tag = "read_EtablissementProfil";
            InputStream is = null;

            //setting nameValuePairs
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("id_user", id_user));
            //Toast.makeText(getApplicationContext(), id_user, Toast.LENGTH_LONG).show();
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
                msg = "Erreur client protocole";

            } catch (IOException e) {
                Log.e("Log_tag", "IOException");
                e.printStackTrace();
                msg = "Erreur IOException";
            }
            return is;
        }
        protected void onProgressUpdate(Void params) {
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(EditEtablissementProfilActivity.this);
            progress.setMessage("Chargement de vos informations de profil...");
            progress.show();
        }


        protected void onPostExecute(InputStream is) {
            int i, pos;
            String result = null;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                Bitmap imgurl;
                // Spinner element
                spinnerType = (Spinner) findViewById(R.id.spinnerType);

                // Access by key : value
                for (i = 0; i < finalResult.length(); i++) {
                    JSONObject element = finalResult.getJSONObject(0);

                    email = element.getString("email");
                    adresse = element.getString("adresse");
                    cp = element.getString("code_postal");
                    nom = element.getString("nom");
                    ville = element.getString("ville");
                    pays = element.getString("pays");
                    telportable = element.getString("tel_portable");
                    telfixe = element.getString("tel_fixe");
                    siteweb = element.getString("siteweb");
                    imgUrl = element.getString("image_url");
                    facebook = element.getString("facebook");
                    genre_musical = element.getString("genre_musical");
                    type_etablissement = element.getString("type_etablissement");

                    editNom.setText(nom);
                    affichageEmail.setText(email);
                    editAdresse.setText(adresse);
                    editCP.setText(cp);
                    editVille.setText(ville);
                    editPays.setText(pays);
                    editMobile.setText(telportable);
                    editFixe.setText(telfixe);
                    editSiteweb.setText(siteweb);
                    editFacebook.setText(facebook);
                    editMdp.setText(password);

                    //verification et affichage des genres musicaux en bdd
                    pos = genre_musical.indexOf("rock");
                    if (pos != -1) {
                        rock.setChecked(true);
                        genrelist.add("rock");
                    }
                    pos = genre_musical.indexOf("pop");
                    if (pos != -1) {
                        pop.setChecked(true);
                        genrelist.add("pop");
                    }
                    pos = genre_musical.indexOf("metal");
                    if (pos != -1) {
                        metal.setChecked(true);
                        genrelist.add("metal");
                    }
                    pos = genre_musical.indexOf("folk");
                    if (pos != -1) {
                        folk.setChecked(true);
                        genrelist.add("folk");
                    }
                    pos = genre_musical.indexOf("funk");
                    if (pos != -1) {
                        funk.setChecked(true);
                        genrelist.add("funk");
                    }
                    pos = genre_musical.indexOf("classique");
                    if (pos != -1) {
                        classique.setChecked(true);
                        genrelist.add("classique");
                    }
                    pos = genre_musical.indexOf("rap");
                    if (pos != -1) {
                        rap.setChecked(true);
                        genrelist.add("rap");
                    }
                    pos = genre_musical.indexOf("electro");
                    if (pos != -1) {
                        electro.setChecked(true);
                        genrelist.add("electro");
                    }
                    pos = genre_musical.indexOf("jazz");
                    if (pos != -1) {
                        jazz.setChecked(true);
                        genrelist.add("jazz");
                    }
                    pos = genre_musical.indexOf("blues");
                    if (pos != -1) {
                        blues.setChecked(true);
                        genrelist.add("blues");
                    }

                    if(imgUrl.length()!=0)
                    {
                        InputStream in = new java.net.URL(imgUrl).openStream();
                        imgurl = BitmapFactory.decodeStream(in);
                        img.setImageBitmap(imgurl);
                    }
                }
                // attaching data adapter to spinner
                spinnerType.setAdapter(dataAdapter);

                int posi = genre_etab.indexOf(type_etablissement);
                spinnerType.setSelection(posi);

                is.close();

                result = total.toString();

                if (result.equals(null) || result.equals("[]")) {
                    msg = "Erreur de lecture";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }

    }
}