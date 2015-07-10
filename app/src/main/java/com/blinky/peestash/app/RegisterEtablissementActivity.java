package com.blinky.peestash.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RegisterEtablissementActivity extends Activity {

    EditText etName, etEmail, etConfirmEmail, etPassword, etConfirmMdp;
    Button btSubmit;
    String nom, email, confirmEmail, password, confirmPassword, tag, msg, guid;
    Verify test = new Verify();
    String msgTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_etablissement);
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        guid = randomUUIDString;

        etName = (EditText) findViewById(R.id.editName);
        etEmail = (EditText) findViewById(R.id.editEmail);
        etConfirmEmail = (EditText) findViewById(R.id.editConfirmEmail);
        etPassword = (EditText) findViewById(R.id.editPassword);
        etConfirmMdp = (EditText) findViewById(R.id.editConfirmMdp);
        btSubmit = (Button) findViewById(R.id.btregister);

        // On met un Listener sur le bouton
        btSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //put the editText value into string variables
                nom = "" + etName.getText().toString();
                email = "" + etEmail.getText().toString();
                confirmEmail = "" + etConfirmEmail.getText().toString();
                password = "" + etPassword.getText().toString();
                confirmPassword = "" + etConfirmMdp.getText().toString();

                new RegisterProfilTask().execute();

            }
        });
    }

    private class RegisterProfilTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            tag = "etablissement_register";
            InputStream is = null;
            String loginOk="no";
            int actif=0;
            //setting nameValuePairs
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //adding string variables into the NameValuePairs
            nameValuePairs.add(new BasicNameValuePair("nom", nom));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("guid", guid));
            nameValuePairs.add(new BasicNameValuePair("actif", Integer.toString(actif)));

            String emailvalid = "ok", passwordvalid = "ok";

            if (test.checkEmailWriting(email)&&email!="") {
                if (test.checkEmail(email, confirmEmail)) {
                    emailvalid = "ok";
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                } else {
                    emailvalid = "no";
                    msg = "Veuillez écrire l'email et la confirmation d'email correctement";
                }
            } else {
                emailvalid = "no";
                msg = "Veuillez écrire correctement votre email";
            }
            if(test.checkMdpWriting(password))
            {
                if(test.checkMdp(password, confirmPassword)) {
                    passwordvalid = "ok";
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                }else
                {
                    passwordvalid = "no";
                    msg = msg + "\nVeuillez écrire votre password et votre confirmation de password correctement";
                }

            }else
            {
                passwordvalid="no";
                msg = msg + "\nVotre mot de passe doit contenir au minimum 3 caractères.";
            }

            if (emailvalid == "ok" && passwordvalid == "ok") {
                //setting the connection to the database
                try {
                    loginOk ="ok";
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
                    //msg = "Données enregistrées en BDD artist";
                    // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    is.close();

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

            }
            return loginOk;
        }

        protected void onProgressUpdate(Void params) {

        }

        protected void onPostExecute(String loginOk) {
            if(loginOk=="ok") {

                Intent i = new Intent(RegisterEtablissementActivity.this, LoginActivity.class);
                startActivity(i);
                msg="Bienvenue ! Vous allez recevoir un email dans un instant pour confirmer votre inscription !";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                //envoyer l'email
                Resources res = getResources();
                msgTemplate = String.format(res.getString(R.string.email_template), nom, email, guid);
                try {
                    GmailSender sender = new GmailSender("peestashgirls", "peestash2015");
                    sender.sendMail("Confirmer votre compte sur Peestash",
                            msgTemplate,
                            "peestashgirls@gmail.com",
                            email);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }

            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                msg = "";
            }
        }

    }
}