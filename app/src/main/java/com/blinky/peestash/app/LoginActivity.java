package com.blinky.peestash.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.view.View.OnClickListener;

import javax.activation.DataHandler;

public class LoginActivity extends Activity {

    private Button  btConnect;
    EditText etEmail, etPassword;
    ProgressDialog progress;
    ImageView btArtist, btEtablissement;
    String email="";
    String password="";
    int actif=1;

    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);

        btArtist = (ImageView) findViewById(R.id.btnRegisterArtist);
        btEtablissement = (ImageView) findViewById(R.id.btnRegisterEtablissement);
        btConnect = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        // On met un Listener sur le bouton Artist
        btArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, RegisterArtistActivity.class);
                startActivity(intent);
            }
        });

        // On met un Listener sur le bouton Etablissement musical
        btEtablissement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, RegisterEtablissementActivity.class);
                startActivity(intent);
            }
        });
        // On met un Listener sur le bouton Se Connecter
        btConnect.setOnClickListener(new OnClickListener() {

            //création d'un nouveau thread pour la connexion vers la BDD

            public void onClick(View v) {

                new ConnexionTask().execute();
            }

        });

    }

    private class ConnexionTask extends AsyncTask<Void, Void, ArrayList> {
        ArrayList<String> valid = new ArrayList<String>();
        List<NameValuePair> nameValuePairs;
        InputStream is = null;
        String msg="";
        String result = null;

        @Override
        protected ArrayList doInBackground(Void... params) {

             email = ""+etEmail.getText().toString();
            password = ""+etPassword.getText().toString();
            //tag Connexion
            String tag = "connexion";

            //setting nameValuePairs
            nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("actif", Integer.toString(actif)));
            //setting the connection to the database
            try{
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


            }catch(ClientProtocolException e){

                Log.e("ClientProtocole", "Log_tag");
                msg = "Erreur client protocole";

            }catch(IOException e)
            {
                Log.e("Log_tag", "IOException");
                e.printStackTrace();
                msg = "Erreur IOException";
            }
            try{
                BufferedReader reader= new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuilder total = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    total.append(line);
                }

                result = total.toString();

                if(result.equals(null) || result.equals("[]"))
                {
                    valid.add("no");
                }
                else {
                    String id = result.substring(7, 11);
                    id = id.replace("\"", "");
                    id = id.replace(",", "");
                    id = id.trim();

                    valid.add(0,id);
                    valid.add("ok");

                    if (result.indexOf("artiste") != -1) {
                        valid.add("artiste");

                    } else if (result.indexOf("etablissement") != -1) {
                        valid.add("etablissement");
                    }

                }
                is.close();

            } catch (Exception e) {
                Log.i("tagconvertstr", "" + e.toString());
            }

           /* try {
                HttpClient client = new DefaultHttpClient();
                String getURL = "http://peestash.peestash.fr/index.php";
                HttpGet get = new HttpGet(getURL);
                HttpResponse responseGet = client.execute(get);
                HttpEntity resEntityGet = responseGet.getEntity();
                String sourceString = "";
                if (resEntityGet != null) {
                    //do something with the response
                    sourceString= new String(EntityUtils.toString(resEntityGet));
                    Log.i("GET RESPONSE", sourceString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return valid;
        }
        @Override
        protected void onPostExecute(ArrayList valid) {
            if(valid.indexOf("ok") == -1) {
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                   msg = "Erreur de connexion!";
                                   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();
            }else {
                if(valid.indexOf("artiste")==-1)
                {


                        String id= (valid.get(0)).toString();
                        Intent i = new Intent(LoginActivity.this, MainEtbActivity.class);
                        i.putExtra("id_user", id);
                        startActivity(i);


                }
                else {

                        String id= (valid.get(0)).toString();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("id_user", id);
                        startActivity(i);

                }
            }
        }
    }
}

