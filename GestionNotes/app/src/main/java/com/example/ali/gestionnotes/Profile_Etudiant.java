package com.example.ali.gestionnotes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile_Etudiant extends AppCompatActivity {

    TextView cin, ce, nom, prenom, filiere, date_n, phone, email;
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__etudiant);

        if (Build.VERSION.SDK_INT >= 21){
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setEnterTransition(slide);
        }


        cin = (TextView) findViewById(R.id.cin_profile);
        ce = (TextView) findViewById(R.id.ce_profile);
        nom = (TextView) findViewById(R.id.nom_profile);
        prenom = (TextView) findViewById(R.id.prenom_profile);
        date_n = (TextView) findViewById(R.id.date_n_profile);
        phone = (TextView) findViewById(R.id.phone_profile);
        email = (TextView) findViewById(R.id.email_profile);
        filiere = (TextView) findViewById(R.id.libile_profile);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");

        getProfile();

    }

    private void getProfile(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile_Etudiant.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONObject c = result.getJSONObject(0);
                    String ccin = c.getString(Config.TAG_CIN_ETUD);
                    String cce = c.getString(Config.TAG_CE);
                    String cnom = c.getString(Config.TAG_NOM_ETUD);
                    String cprenom = c.getString(Config.TAG_PRENOM_ETUD);
                    String cdate_n = c.getString(Config.TAG_NAISSANCE);
                    String cphone = c.getString(Config.TAG_PHONE);
                    String cemail = c.getString(Config.TAG_EMAIL);
                    String cfilier = c.getString(Config.TAG_LIBELLE_FILIERE);

                    cin.setText(ccin);
                    ce.setText(cce);
                    nom.setText(cnom);
                    prenom.setText(cprenom);
                    date_n.setText(cdate_n);
                    phone.setText(cphone);
                    email.setText(cemail);
                    filiere.setText(cfilier);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //showEtudiant(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
                String s = rh.sendGetRequestParam(Config.URL_PROFILE,username);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("êtes vous sûr de vouloir se deconnecter?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.USERNAME_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        else if (id == R.id.profile){
            Intent intentProfile = new Intent(this, Profile_Etudiant.class);
            startActivity(intentProfile);
        }
        else if (id == R.id.note){
            Intent intentProfile = new Intent(this, Note.class);
            startActivity(intentProfile);
        }
        return super.onOptionsItemSelected(item);
    }

}


