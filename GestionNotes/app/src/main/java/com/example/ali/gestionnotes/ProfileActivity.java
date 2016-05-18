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
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView textView, header_name, header_email;
    private ListView listView;
    private String JSON_STRING;
    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;
    private boolean mUserSawDrawer = false;
    private View nav_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        //mSelectedId = savedInstanceState == null ? R.id.profile : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(mSelectedId);

        if (Build.VERSION.SDK_INT >= 21){
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transition_a);
            getWindow().setExitTransition(transition);
        }
        textView = (TextView) findViewById(R.id.textView);




        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
       // drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

       // NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        // android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.fragment_navigation_drawer, fragment);
        //fragmentTransaction.commit();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");

        //Showing the current logged in username to textview
        textView.setText("Bonjour " + username);
        //header_name.setText(username);


        listView = (ListView) findViewById(R.id.lv);

        getJSON();
    }

    private void showEvents(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String start = jo.getString(Config.TAG_EVENT_START);
                String end = jo.getString(Config.TAG_EVENT_END);
                String title = jo.getString(Config.TAG_EVENT_TITLE);
                String nom = jo.getString(Config.TAG_EVENT_NOM);
                String prenom = jo.getString(Config.TAG_EVENT_PRENOM);

                HashMap<String,String> event = new HashMap<>();
                event.put(Config.TAG_EVENT_START,start);
                event.put(Config.TAG_EVENT_END,end);
                event.put(Config.TAG_EVENT_TITLE,title);
                event.put(Config.TAG_EVENT_NOM, nom);
                event.put(Config.TAG_EVENT_PRENOM, prenom);
                list.add(event);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ProfileActivity.this, list, R.layout.list_events,
                new String[]{Config.TAG_EVENT_START,Config.TAG_EVENT_END,Config.TAG_EVENT_TITLE, Config.TAG_EVENT_NOM, Config.TAG_EVENT_PRENOM},
                new int[]{R.id.start, R.id.end, R.id.title, R.id.nom_event, R.id.prenom_event});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileActivity.this, "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEvents();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_EVENTS);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    //Logout function
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        else if (id == R.id.profile){
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            Intent intentProfile = new Intent(this, Profile_Etudiant.class);
            startActivity(intentProfile, compat.toBundle());
        }
        else if (id == R.id.note){
            ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            Intent intentProfile2 = new Intent(this, Note.class);
            startActivity(intentProfile2, compat2.toBundle());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
    private void navigate(int mSelectedId) {
        Intent intent = null;
        if (mSelectedId == R.id.profile) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            intent = new Intent(this, Profile_Etudiant.class);
            startActivity(intent, compat.toBundle());
        }
        if (mSelectedId == R.id.note) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            intent = new Intent(this, Note.class);
            startActivity(intent, compat.toBundle());
        }
        if (mSelectedId == R.id.menuLogout) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            logout();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        navigate(mSelectedId);
        menuItem.setChecked(false);
        return true;
    }
}
