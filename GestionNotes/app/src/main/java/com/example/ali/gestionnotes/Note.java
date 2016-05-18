package com.example.ali.gestionnotes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Note extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    private String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if (Build.VERSION.SDK_INT >= 21){
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transition_b);
            getWindow().setExitTransition(transition);
           // Explode explode = new Explode();
            //explode.setDuration(2000);
            //getWindow().setEnterTransition(explode);
        }

        listView = (ListView)findViewById(R.id.listView);

        getJSON();


        listView.setOnItemClickListener(this);
    }



    private void showEtudiant(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String param1 = jo.getString(Config.TAG_CODE_MDLE);
                String param2 = jo.getString(Config.TAG_MODULE);

                HashMap<String,String> etudiant = new HashMap<>();
                etudiant.put(Config.TAG_CODE_MDLE,param1);
                etudiant.put(Config.TAG_MODULE,param2);
                list.add(etudiant);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                Note.this, list, R.layout.list_item,
                new String[]{Config.TAG_CODE_MDLE,Config.TAG_MODULE},
                new int[]{R.id.param1, R.id.param2});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Note.this, "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEtudiant();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_AFFICHER_ETUD, user);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        Intent intent = new Intent(this, ViewEtudiant.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String note = map.get(Config.TAG_CODE_MDLE).toString();

        //String note = parent.getAdapter().getItem(position).toString();
        intent.putExtra(Config.TAG_CODE_MDLE, note);
        //info.setText(note);
        startActivity(intent, compat.toBundle());
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
