package com.example.ali.gestionnotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
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

public class ViewEtudiant extends AppCompatActivity{


    TextView cc1, cc2, cc3, cop, moyenne, titre_module, valide;
    private String code_mdle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_etudiant);


        if (Build.VERSION.SDK_INT >= 21){
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setEnterTransition(slide);
        }




        cc1 = (TextView) findViewById(R.id.cc1);
        cc2 = (TextView) findViewById(R.id.cc2);
        cc3 = (TextView) findViewById(R.id.cc3);
        cop = (TextView) findViewById(R.id.cop);
        moyenne = (TextView) findViewById(R.id.mmoyenne);
        titre_module = (TextView) findViewById(R.id.module);
        valide = (TextView) findViewById(R.id.valide);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");

        Intent intent = getIntent();
        code_mdle = intent.getStringExtra(Config.TAG_CODE_MDLE);

        getEtudiant();

        //cc1.setText(code_mdle);

    }

    private void getEtudiant(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewEtudiant.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                    JSONObject c = result.getJSONObject(0);
                    String c1 = c.getString(Config.TAG_CC1);
                    String c2 = c.getString(Config.TAG_CC2);
                    String c3 = c.getString(Config.TAG_CC3);
                    String ccop = c.getString(Config.TAG_COP);
                    String titre = c.getString(Config.TAG_MODULE);

                    cc1.setText(c1);
                    cc2.setText(c2);
                    cc3.setText(c3);
                    cop.setText(ccop);
                    titre_module.setText(titre);

                    float n1, n2, n3, nop;
                    double m;
                    n1 = Float.valueOf(c1);
                    n2 = Float.valueOf(c2);
                    n3 = Float.valueOf(c3);
                    nop = Float.valueOf(ccop);
                    m = Math.floor(((n1+n2+n3+nop)/4)*100)/100;
                    String moy = Double.toString(m);
                    moyenne.setText(moy);

                    if (m>=10){
                        valide.setText("module validé");
                        valide.setTextColor(Color.GREEN);
                    }
                    else {
                        valide.setText("module non validé");
                        valide.setTextColor(Color.RED);
                    }


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
                String s = rh.sendGetRequestParam(Config.URL_NOTE+username+"&code_mdle=",code_mdle);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }




}
