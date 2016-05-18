package com.example.ali.gestionnotes;

/**
 * Created by ali on 11/04/2016.
 */
public class Config {
    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String USERNAME_SHARED_PREF = "username";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    //URL to our login.php file
    public static final String LOGIN_URL = "http://10.0.3.2/gestion_notes/login.php";
    public static final String URL_AFFICHER_ALL = "http://10.0.3.2/gestion_notes/afficher_all.php";
    public static final String URL_AFFICHER_ETUD = "http://10.0.3.2/gestion_notes/afficher_etud.php?login=";
    public static final String URL_NOTE = "http://10.0.3.2/gestion_notes/note.php?login=";
    public static final String URL_PROFILE = "http://10.0.3.2/gestion_notes/profile.php?login=";
    public static final String URL_EVENTS = "http://10.0.3.2/gestion_notes/events.php";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_CIN_ETUD = "etudiant.Nom";
    public static final String KEY_PRENOM_ETUD = "prenom";
    public static final String KEY_NOM_MODULE = "module.nom";
    public static final String KEY_LIBELLE_MODULE = "libile";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_CIN_ETUD  = "cin_etud";
    public static final String TAG_LOGIN  = "login";
    public static final String TAG_NOM_ETUD = "Nom";
    public static final String TAG_PRENOM_ETUD = "prenom";
    public static final String TAG_MODULE = "modules";
    public static final String TAG_CODE_MDLE = "code_mdle";
    public static final String TAG_CE = "ce";
    public static final String TAG_LIBELLE_FILIERE = "libile";
    public static final String TAG_CC1 = "cc1";
    public static final String TAG_CC2 = "cc2";
    public static final String TAG_CC3 = "cc3";
    public static final String TAG_COP = "cop";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_NAISSANCE = "date_n";
    public static final String TAG_ADRESS = "adress";
    public static final String TAG_ANNE_SCOLAIRE = "anne_scolaire";
    public static final String TAG_EVENT_TITLE = "title";
    public static final String TAG_EVENT_START = "start";
    public static final String TAG_EVENT_END = "end";
    public static final String TAG_EVENT_NOM = "nom";
    public static final String TAG_EVENT_PRENOM = "prenom";

    //etudiant id to pass with intent
    public static final String CIN_ETUD = "cin_etud";

    //Keys for username and password as defined in our $_POST['key'] in login.php
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";


}
