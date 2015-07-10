package com.blinky.peestash.app;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.*;

import android.widget.*;
import com.blinky.peestash.app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.NameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private String id_user="", valid="ok";
    List<String> statuts, adresses;
    ArrayAdapter<String> statutAdapter, adresseAdapter;
    Spinner spinnerStatut, spinnerAdresse;

    private EditText Titre, fromDateEtxt, HeureDebut, toDateEtxt, HeureFin;
    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private CheckBox rock, pop, metal, jazz, funk, electro, blues, rap, folk, classique;
    private String genremusical = "", titre="", cp="", ville="", pays="", statut="", adresse="", genre_musical="",
            dateDebut="", dateFin="", heureDebut="", heureFin="", facebook="", siteweb="", description="", statut_recrutement="";

    private ArrayList<String> genrelist = new ArrayList<String>();

    private EditText Adresse, Cp, Ville, Pays, Facebook, Siteweb, Description;
    LinearLayout root;
    LinearLayout.LayoutParams rootParams;
    int layoutHeight;

    Button btnCreateEvent, btnLoadPicture;
    final Context context = getActivity();
    List<NameValuePair> nameValuePairs;

    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String type="", tag="", img="";
    String msg;
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    ProgressDialog progress;
    InputStream is;

    View rootView;

    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
        /*test*/
    }

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_user = getArguments().getString("id_user");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_event, container, false);
        params.put("id", id_user);
        type = "event";
        prgDialog = new ProgressDialog(getActivity());
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        findViewsById();

        Locale locale = new Locale("FR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);

        // Spinner element
        spinnerStatut = (Spinner) rootView.findViewById(R.id.spinnerStatut);
        // Spinner Drop down elements
        statuts = new ArrayList<String>();

        // Creating adapter for spinner
        statutAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, statuts);

        // Spinner click listener
        spinnerStatut.setOnItemSelectedListener(this);

        statuts.add(String.valueOf("Ferme"));
        statuts.add(String.valueOf("Ouvert"));

        // Drop down layout style - list view with radio button
        statutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerStatut.setAdapter(statutAdapter);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        setDateTimeField();
        setHourTimeField();

        addListenerOnChkWindows();
        // Spinner element
        spinnerAdresse = (Spinner) rootView.findViewById(R.id.spinnerStatut);
        // Spinner Drop down elements
        adresses = new ArrayList<String>();

        // Creating adapter for spinner
        adresseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, adresses);


        spinnerAdresse = (Spinner) rootView.findViewById(R.id.spinnerAdresse);
        // Spinner click listener
        spinnerAdresse.setOnItemSelectedListener(new SelectAdress());

        adresses.add(String.valueOf("Par defaut celle de mon etablissement"));
        adresses.add(String.valueOf("Ajouter une adresse"));

        // Drop down layout style - list view with radio button
        adresseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerAdresse.setAdapter(adresseAdapter);
        // Get the layout id
        root = (LinearLayout) rootView.findViewById(R.id.layout_hidden);

        // Lastly, set the height of the layout
        Adresse = (EditText)rootView.findViewById(R.id.Adresse);
        Cp = (EditText)rootView.findViewById(R.id.Cp);
        Ville = (EditText)rootView.findViewById(R.id.Ville);
        Pays = (EditText)rootView.findViewById(R.id.Pays);

        Titre = (EditText)rootView.findViewById(R.id.Titre);
        Facebook = (EditText)rootView.findViewById(R.id.Facebook);
        Siteweb = (EditText)rootView.findViewById(R.id.Siteweb);
        Description = (EditText)rootView.findViewById(R.id.Description);

        btnCreateEvent = (Button)rootView.findViewById(R.id.btnCreateEvent);

        btnLoadPicture = (Button) rootView.findViewById(R.id.buttonLoadPicture);

        // On met un Listener sur le bouton Artist
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new Thread(new Runnable() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                //update datas in database
                                is = null;
                                String result = null;
                                String tag = "add_event";

                                params.put("tag", tag);

                                titre = "" + Titre.getText().toString();
                                dateDebut = "" + fromDateEtxt.getText().toString();
                                dateFin = "" + toDateEtxt.getText().toString();
                                heureDebut = "" + HeureDebut.getText().toString();
                                heureFin = "" + HeureFin.getText().toString();
                                description = "" + Description.getText().toString();
                                facebook = "" + Facebook.getText().toString();
                                siteweb = "" + Siteweb.getText().toString();
                                genre_musical = "" + genrelist.toString();
                                statut = "" + statut.toString();
                               /* msg = tag + id_user + " " + titre  + " " + dateDebut  + " " + dateFin  + " " + heureDebut  + " " + heureFin  + " " + description  + " " + facebook  + " " + siteweb  + " " + genre_musical  + " " +statut  + " " +adresse  + " " +cp + " " + ville + " " + pays;
                                System.out.println(msg);*/
                                msg = "";
                                valid = "ok";

                                if (titre == "") {
                                    valid = "no";
                                    msg = "Veuillez renseigner le titre de votre ?v?nement\n";
                                } else {
                                    params.put("titre", titre);
                                }

                                if (dateDebut == "") {
                                    valid = "no";
                                    msg += "Veuillez pr?ciser la date de votre ?v?nement\n";
                                } else {
                                    params.put("date_debut", dateDebut);
                                }

                                params.put("date_fin", dateFin);

                                if (heureDebut == "") {
                                    valid = "no";
                                    msg += "Veuillez renseigner l'heure de d?but de votre ?v?nement\n";

                                } else {

                                    params.put("heure_debut", heureDebut);
                                }

                                if (heureFin == "") {
                                    valid = "no";
                                    msg += "Veuillez renseigner l'heure o? se termine votre ?v?nement\n";

                                } else {

                                    params.put("heure_fin", heureFin);
                                }

                                if (description == "" || description.length() > 180) {
                                    valid = "no";
                                    msg += "Veuillez renseigner correctement la description de votre ?v?nement. Celle-ci doit " +
                                            "comporter au maximum 180 caract?res.\n";

                                } else {

                                    params.put("description", description);
                                }

                                if (genrelist.isEmpty()) {
                                    valid = "no";
                                    msg += "Veuillez renseigner au minimum un genre musical correspondant ? votre ?v?nement\n";

                                } else {

                                    params.put("genre_musical", genre_musical);
                                }

                                if (adresse == "Ajouter une adresse") {

                                    adresse = "" + Adresse.getText().toString();
                                    cp = "" + Cp.getText().toString();
                                    ville = "" + Ville.getText().toString();
                                    pays = "" + Pays.getText().toString();

                                    if (adresse == "") {
                                        valid = "no";
                                        msg += "Veuillez renseigner correctement l'adresse de votre ?v?nement.\n";
                                    } else {

                                        params.put("adresse", adresse);
                                    }

                                    if (cp == "" || cp.length() > 5 || cp.length() < 5) {
                                        valid = "no";
                                        msg += "Veuillez renseigner correctement le code postal.\n";
                                    } else {

                                        params.put("code_postal", cp);
                                    }

                                    if (ville != "") {
                                        valid = "no";
                                        msg += "Veuillez renseigner correctement la ville o? aura lieu votre ?v?nement.\n";
                                    } else {

                                        params.put("ville", ville);
                                    }

                                    if (pays != "") {
                                        valid = "no";
                                        msg += "Veuillez pr?ciser le pays dans lequel aura lieu votre ?v?nement";
                                    } else {

                                        params.put("pays", pays);
                                    }
                                }
                                params.put("statut_recrutement", statut);
                                params.put("facebook", facebook);
                                params.put("siteweb", siteweb);
                                if (valid == "ok") {

                                    //uploadImage();
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    // Don't forget to change the IP address to your LAN address. Port no as well.
                                    client.post("http://peestash.peestash.fr/index.php", params, new AsyncHttpResponseHandler() {
                                        // When the response returned by REST has Http
                                        // response code '200'
                                        @Override
                                        public void onSuccess(String response) {
                                            // Create new fragment and transaction
                                            Fragment newFragment = new EventsEtbFragment();
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

                                        // When the response returned by REST has Http
                                        // response code other than '200' such as '404',
                                        // '500' or '403' etc

                                        public void onFailure(int statusCode, Throwable error,
                                                              String content) {
                                            // Hide Progress Dialog
                                            prgDialog.hide();
                                            // When Http response code is '404'
                                            if (statusCode == 404) {
                                                Toast.makeText(getActivity(),
                                                        "Requested resource not found",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            // When Http response code is '500'
                                            else if (statusCode == 500) {
                                                Toast.makeText(getActivity(),
                                                        "Something went wrong at server end",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            // When Http response code other than 404, 500
                                            else {
                                                Toast.makeText(
                                                        getActivity(),
                                                        "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                                                + statusCode, Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                    });


                                } else {
                                  /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

                                    // set title
                                    alertDialogBuilder.setTitle("Formulaire incorrect");
                                    alertDialogBuilder.setIcon(R.drawable.ic_attention);

                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage(msg)
                                            .setCancelable(true)
                                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // if this button is clicked, close
                                                    // current activity
                                                    dialog.cancel();
                                                }
                                            });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();

                                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*/
                                    System.out.print("error");
                                }

                            }

                        });

                    }

                    private void uploadImage() {
                        // When Image is selected from Gallery
                        if (imgPath != null && !imgPath.isEmpty()) {
                            prgDialog.setMessage("Conversion de l'image...");
                            prgDialog.show();
                            // Convert image to String using Base64
                            encodeImagetoString();
                            // When Image is not selected from Gallery
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "Vous devez choisir une image pour lancer l'enregistrement.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();

            }

        });

        btnLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        statut = spinnerStatut.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void findViewsById() {

        //r?cup?ration des checkbox genres musicaux
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

        fromDateEtxt = (EditText) rootView.findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) rootView.findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        HeureDebut = (EditText) rootView.findViewById(R.id.HeureDebut);
        HeureDebut.setInputType(InputType.TYPE_NULL);

        HeureFin = (EditText) rootView.findViewById(R.id.HeureFin);
        HeureFin.setInputType(InputType.TYPE_NULL);

    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private void setHourTimeField() {

        HeureDebut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        HeureDebut.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        HeureFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        HeureFin.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }
    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
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
    private class SelectAdress implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            adresse = spinnerAdresse.getItemAtPosition(position).toString();
          /*  String msg = adresse;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*/

            if(adresse == "Ajouter une adresse") {
                layoutHeight=500;
                rootParams = (LinearLayout.LayoutParams) root.getLayoutParams();
                rootParams.height = layoutHeight;
                root.setLayoutParams(rootParams);
                Adresse.setVisibility(View.VISIBLE);
                Cp.setVisibility(View.VISIBLE);
                Ville.setVisibility(View.VISIBLE);
                Pays.setVisibility(View.VISIBLE);

            }else
            {
                layoutHeight=0;
                rootParams = (LinearLayout.LayoutParams) root.getLayoutParams();
                rootParams.height = layoutHeight;
                root.setLayoutParams(rootParams);
                Adresse.setVisibility(View.INVISIBLE);
                Cp.setVisibility(View.INVISIBLE);
                Ville.setVisibility(View.INVISIBLE);
                Pays.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }

    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    // When Image is selected from Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                params.put("imgPath", imgPath);

            } else {
                Toast.makeText(getActivity(), "Vous n'avez s?lectionn? aucune image.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Une erreur est survenue", Toast.LENGTH_LONG)
                    .show();
        }

    }


    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Enregistrement des informations...");
                // Put converted Image string into Async Http Post param

                params.put("filename", fileName);
                params.put("imgPath", imgPath);
                params.put("image", encodedString);

                AsyncHttpClient client = new AsyncHttpClient();
                // Don't forget to change the IP address to your LAN address. Port no as well.
                client.post("http://peestash.peestash.fr/index.php", params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        //System.out.println(params);
                        getActivity().finish();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc

                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getActivity(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getActivity(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });

            }
        }.execute(null, null, null);
    }


}
