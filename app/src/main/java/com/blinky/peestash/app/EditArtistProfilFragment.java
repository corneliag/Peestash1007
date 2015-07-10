package com.blinky.peestash.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditArtistProfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditArtistProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditArtistProfilFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    private OnFragmentInteractionListener mListener;


    String id_user = "", type="";
    private EditText editPseudo, editEmail, editConfirmEmail, editAdress, editCP, editNom, editPrenom, editVille, editPays, editTelMobile,
            editTelFixe, editSoundcloud, editSiteweb, editFacebook, editTwitter, editPassword, editConfirmMdp, editTypeArtiste;
    TextView affichageEmail;
    int i;
    //ImageView img;
    private Button btnSave;
    private String pseudo = "", nom = "", prenom = "", age = "", email = "", confirmEmail="", ville = "", adresse = "", cp = "", pays = "",
            telportable = "", telfixe = "", dispo = "", soundcloud = "", twitter="", facebook="", siteweb = "", imgUrl = "", genre_musical = "", password = "", confirmMdp="", type_artiste="";
    private CheckBox rock, pop, metal, jazz, funk, electro, blues, rap, folk, classique, lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche;
    private String genremusical = "", disponibilites = "";

    private ArrayList<String> genrelist = new ArrayList<String>();
    private ArrayList<String> dispolist = new ArrayList<String>();
    Verify test = new Verify();
    String msg="";
    ProgressDialog progress;
    List<String> categories;
    ArrayAdapter<String> dataAdapter, typeAdapter;
    Spinner spinnerAge;
    List<String> type_art;
    Spinner spinnerTypeArtiste;
    private WebView wv;

    String html;

    // TODO: Rename and change types and number of parameters
    public static EditArtistProfilFragment newInstance(String param1, String param2) {
        EditArtistProfilFragment fragment = new EditArtistProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditArtistProfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_user = getArguments().getString("id_user");
            /*Toast.makeText(getActivity(), "id user: " + id_user,
                    Toast.LENGTH_LONG).show();*/
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_artist_profil, container, false);

        ImageView btnBack = (ImageView) rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 1;
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("id_user", id_user);
                i.putExtra("position", position);
                startActivity(i);
                getActivity().finish();
            }
        });
        // Spinner element
        spinnerAge = (Spinner) rootView.findViewById(R.id.spinnerAge);
        // Spinner Drop down elements
        categories = new ArrayList<String>();

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Spinner element
        spinnerTypeArtiste = (Spinner) rootView.findViewById(R.id.spinnerTypeArtiste);
        // Spinner Drop down elements
        type_art = new ArrayList<String>();

        // Creating adapter for spinner
        typeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type_art);

        //tag recuperation des informations de profil artiste

        Bundle var = getActivity().getIntent().getExtras();
        id_user = var.getString("id_user");
        affichageEmail = (TextView) rootView.findViewById(R.id.affichageEmail);

        editEmail = (EditText) rootView.findViewById(R.id.editEmail);
        editConfirmEmail = (EditText) rootView.findViewById(R.id.editConfirmEmail);
        editPseudo = (EditText) rootView.findViewById(R.id.editPseudo);
        editAdress = (EditText) rootView.findViewById(R.id.editAdress);
        editCP = (EditText) rootView.findViewById(R.id.editCP);
        editNom = (EditText) rootView.findViewById(R.id.editNom);
        editPrenom = (EditText) rootView.findViewById(R.id.editPrenom);
        editVille = (EditText) rootView.findViewById(R.id.editVille);
        editPays = (EditText) rootView.findViewById(R.id.editPays);
        editTelMobile = (EditText) rootView.findViewById(R.id.editTelMobile);
        editTelFixe = (EditText) rootView.findViewById(R.id.editTelFixe);
        editSoundcloud = (EditText) rootView.findViewById(R.id.editSoundcloud);
        editFacebook = (EditText) rootView.findViewById(R.id.editFacebook);
        editTwitter = (EditText) rootView.findViewById(R.id.editTwitter);
        editSiteweb = (EditText) rootView.findViewById(R.id.editSiteweb);
        // img = (ImageView) findViewById(R.id.img);
        editPassword = (EditText) rootView.findViewById(R.id.editPassword);
        editConfirmMdp = (EditText) rootView.findViewById(R.id.editConfirmMdp);

        //recuperation des checkbox genres musicaux
        rock = (CheckBox) rootView.findViewById(R.id.rock);
        pop = (CheckBox) rootView.findViewById(R.id.pop);
        metal = (CheckBox) rootView.findViewById(R.id.metal);
        rap = (CheckBox) rootView.findViewById(R.id.rap);
        funk = (CheckBox) rootView.findViewById(R.id.funk);
        classique = (CheckBox) rootView.findViewById(R.id.classique);
        blues = (CheckBox) rootView.findViewById(R.id.blues);
        electro = (CheckBox) rootView.findViewById(R.id.electro);
        folk = (CheckBox) rootView.findViewById(R.id.folk);
        jazz = (CheckBox) rootView.findViewById(R.id.jazz);

        //recuperation des checkbox disponibilites
        lundi = (CheckBox) rootView.findViewById(R.id.dispolundi);
        mardi = (CheckBox) rootView.findViewById(R.id.dispomardi);
        mercredi = (CheckBox) rootView.findViewById(R.id.dispomercredi);
        jeudi = (CheckBox) rootView.findViewById(R.id.dispojeudi);
        vendredi = (CheckBox) rootView.findViewById(R.id.dispovendredi);
        samedi = (CheckBox) rootView.findViewById(R.id.disposamedi);
        dimanche = (CheckBox) rootView.findViewById(R.id.dispodimanche);

        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        addListenerOnChkWindows();
        int id;
        id = Integer.parseInt(id_user);
        new ReadProfilTask().execute(id);

        wv = (WebView) rootView.findViewById(R.id.webView);

        // On met un Listener sur le bouton Artist
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new Thread(new Runnable() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                //update datas in database
                                InputStream is = null;
                                String result = null;
                                String tag = "update_ArtistProfil";

                                nom = "" + editNom.getText().toString();
                                prenom = "" + editPrenom.getText().toString();
                                pseudo = "" + editPseudo.getText().toString();
                                email = "" + editEmail.getText().toString();
                                confirmEmail = "" + editConfirmEmail.getText().toString();
                                adresse = "" + editAdress.getText().toString();
                                cp = "" + editCP.getText().toString();
                                ville = "" + editVille.getText().toString();
                                pays = "" + editPays.getText().toString();
                                telfixe = "" + editTelFixe.getText().toString();
                                telportable = "" + editTelMobile.getText().toString();
                                dispo = "" + dispolist.toString();
                                soundcloud = "" + editSoundcloud.getText().toString();
                                siteweb = "" + editSiteweb.getText().toString();
                                facebook = "" + editFacebook.getText().toString();
                                twitter = "" + editTwitter.getText().toString();
                                age = "" + age;
                                type_artiste = "" + type_artiste;
                                genre_musical = "" + genrelist.toString();
                                password = "" + editPassword.getText().toString();
                                confirmMdp = "" + editConfirmMdp.getText().toString();

                                //setting nameValuePairs
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                                //adding string variables into the NameValuePairs
                                nameValuePairs.add(new BasicNameValuePair("tag", tag));
                                nameValuePairs.add(new BasicNameValuePair("id", id_user));
                                nameValuePairs.add(new BasicNameValuePair("nom", nom));
                                nameValuePairs.add(new BasicNameValuePair("prenom", prenom));
                                nameValuePairs.add(new BasicNameValuePair("pseudo", pseudo));
                                nameValuePairs.add(new BasicNameValuePair("adresse", adresse));
                                nameValuePairs.add(new BasicNameValuePair("code_postal", cp));
                                nameValuePairs.add(new BasicNameValuePair("ville", ville));
                                nameValuePairs.add(new BasicNameValuePair("pays", pays));
                                nameValuePairs.add(new BasicNameValuePair("tel_fixe", telfixe));
                                nameValuePairs.add(new BasicNameValuePair("tel_portable", telportable));
                                nameValuePairs.add(new BasicNameValuePair("disponibilites", dispo));
                                nameValuePairs.add(new BasicNameValuePair("soundcloud", soundcloud));
                                nameValuePairs.add(new BasicNameValuePair("facebook", facebook));
                                nameValuePairs.add(new BasicNameValuePair("twitter", twitter));
                                nameValuePairs.add(new BasicNameValuePair("siteweb", siteweb));
                                nameValuePairs.add(new BasicNameValuePair("age", age));
                                nameValuePairs.add(new BasicNameValuePair("type_artiste", type_artiste));
                                nameValuePairs.add(new BasicNameValuePair("genre_musical", genre_musical));


                                String emailvalid = "ok", passwordvalid = "ok", msg = "";

                                if (email != "") {
                                    if (test.checkEmailWriting(email)) {

                                        if (test.checkEmail(email, confirmEmail)) {
                                            emailvalid = "ok";
                                            nameValuePairs.add(new BasicNameValuePair("email", email));
                                        } else {
                                            emailvalid = "no";
                                            msg = "Veuillez ecrire correctement l'email et la confirmation d'e-mail.\n";
                                        }
                                    } else {
                                        emailvalid = "no";
                                        msg = "Veuillez ecrire un email correct.\n";
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
                                            msg = msg + "Veuillez ecrire votre password et votre confirmation de password correctement\n";
                                        }

                                    } else {
                                        passwordvalid = "no";
                                        msg = msg + "Votre mot de passe doit contenir au minimum 3 caractères.\n";
                                    }
                                } else {
                                    password = "" + editPassword.getText().toString();
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
                                        msg = "Vos informations ont bien ete modifiees";
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                        int position = 1;
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        i.putExtra("id_user", id_user);
                                        i.putExtra("position", position);
                                        startActivity(i);
                                        getActivity().finish();


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
                                } else {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                    msg = "";

                                }
                            }
                        });

                    }
                }).start();

            }

        });

        // Spinner click listener
        spinnerAge.setOnItemSelectedListener(this);

        for (i = 16; i < 90; i++) {
            categories.add(String.valueOf(i));
        }
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner click listener
        spinnerTypeArtiste.setOnItemSelectedListener(new AddTypeArtist());

        type_art.add(String.valueOf(""));
        type_art.add(String.valueOf("Musicien solo amateur"));
        type_art.add(String.valueOf("Musicien solo professionnel"));
        type_art.add(String.valueOf("Groupe amateur"));
        type_art.add(String.valueOf("Groupe professionnel"));
        type_art.add(String.valueOf("DJ amateur"));
        type_art.add(String.valueOf("DJ professionnel"));


        // Drop down layout style - list view with radio button
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
        //listener sur les checkbox des jours de disponibilite
        lundi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "lundi";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "lundi";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
        mardi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "mardi";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "mardi";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
        mercredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "mercredi";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "mercredi";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
        jeudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "jeudi";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "jeudi";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
        vendredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "vendredi";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "vendredi";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
        samedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "samedi";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "samedi";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
        dimanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    disponibilites = "dimanche";
                    dispolist.add(disponibilites);
                } else {
                    disponibilites = "dimanche";
                    int pos = dispolist.indexOf(disponibilites);
                    dispolist.remove(pos);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        age = spinnerAge.getItemAtPosition(position).toString();


    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ReadProfilTask extends AsyncTask<Integer, Void, InputStream> {
        int i, pos;
        String result = null;
        String tag = "read_ArtistProfil";

        protected InputStream doInBackground(Integer... id) {

            InputStream is = null;
            //setting nameValuePairs
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
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
                // Bitmap imgurl;

                // Spinner element
                spinnerAge = (Spinner) rootView.findViewById(R.id.spinnerAge);
                spinnerTypeArtiste = (Spinner)rootView.findViewById(R.id.spinnerTypeArtiste);

                // Access by key : value
                for (i = 0; i < finalResult.length(); i++) {
                    JSONObject element = finalResult.getJSONObject(0);

                    pseudo = element.getString("pseudo");
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
                    soundcloud = element.getString("soundcloud");
                    facebook = element.getString("facebook");
                    twitter = element.getString("twitter");
                    siteweb = element.getString("siteweb");
                    // imgUrl = element.getString("image_url");
                    genre_musical = element.getString("genre_musical");
                    age = element.getString("age");
                    type_artiste = element.getString("type_artiste");

                   /* if(imgUrl.length()!=0)
                    {
                        InputStream in = new java.net.URL(imgUrl).openStream();
                        imgurl = BitmapFactory.decodeStream(in);
                        img.setImageBitmap(imgurl);
                    }*/

                    editNom.setText(nom);
                    editPrenom.setText(prenom);
                    editPseudo.setText(pseudo);
                    affichageEmail.setText(email);
                    editAdress.setText(adresse);
                    editCP.setText(cp);
                    editVille.setText(ville);
                    editPays.setText(pays);
                    editTelMobile.setText("0"+telportable);
                    editTelFixe.setText("0"+telfixe);
                    editSoundcloud.setText(soundcloud);
                    editSiteweb.setText(siteweb);
                    editFacebook.setText(facebook);
                    editTwitter.setText(twitter);
                    editPassword.setText(password);

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
                    //vérification et affichage des disponibilités en bdd
                    pos = dispo.indexOf("lundi");
                    if (pos != -1) {
                        lundi.setChecked(true);
                        dispolist.add("lundi");
                    }
                    pos = dispo.indexOf("mardi");
                    if (pos != -1) {
                        mardi.setChecked(true);
                        dispolist.add("mardi");
                    }
                    pos = dispo.indexOf("mercredi");
                    if (pos != -1) {
                        mercredi.setChecked(true);
                        dispolist.add("mercredi");
                    }
                    pos = dispo.indexOf("jeudi");
                    if (pos != -1) {
                        jeudi.setChecked(true);
                        dispolist.add("jeudi");
                    }
                    pos = dispo.indexOf("vendredi");
                    if (pos != -1) {
                        vendredi.setChecked(true);
                        dispolist.add("vendredi");
                    }
                    pos = dispo.indexOf("samedi");
                    if (pos != -1) {
                        samedi.setChecked(true);
                        dispolist.add("samedi");
                    }
                    pos = dispo.indexOf("dimanche");
                    if (pos != -1) {
                        dimanche.setChecked(true);
                        dispolist.add("dimanche");
                    }

                }

                if(soundcloud.length()!=0) {
                    html = "<iframe width=\"100%\" height=\"400\" scrolling=\"yes\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/" + soundcloud + "&amp;auto_play=false&amp;hide_related=false&amp;show_comments=false&amp;show_user=false&amp;show_reposts=false&amp;show_artwork=false&amp;buying=false\"></iframe>";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                }else {
                    html = "Vous n'avez pas renseigné l'ID de votre playlist Soundcloud";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

                }
                // attaching data adapter to spinner
                spinnerAge.setAdapter(dataAdapter);

                int posi = categories.indexOf(age);

                spinnerAge.setSelection(posi);

                // attaching data adapter to spinner
                spinnerTypeArtiste.setAdapter(typeAdapter);

                int pos= type_art.indexOf(type_artiste);
                System.out.print("pos"+pos);
                if(pos>-1&&pos<7) {
                    spinnerTypeArtiste.setSelection(pos);
                }else {
                    spinnerTypeArtiste.setSelection(0);
                }

                is.close();

                result = total.toString();

                if (result.equals(null) || result.equals("[]")) {
                    String msg = "Erreur de lecture";
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    private class AddTypeArtist implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if((spinnerTypeArtiste.getItemAtPosition(position).toString())!="") {
                type_artiste = spinnerTypeArtiste.getItemAtPosition(position).toString();
            }
            String msg = "type "+type_artiste;
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
