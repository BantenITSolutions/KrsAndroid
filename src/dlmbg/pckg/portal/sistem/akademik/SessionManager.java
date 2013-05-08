package dlmbg.pckg.portal.sistem.akademik;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
 
    // Editor for Shared preferences
    Editor editor;
 
    // Context
    Context _context;
 
    // Shared pref mode
    int PRIVATE_MODE = 0;
 
    // Sharedpref file name
    private static final String PREF_NAME = "SiakadAndroidDLMNG32";
 
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
 
    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";
 
    // Name (make variable public to access from outside)
    public static final String KEY_NAMA = "nama";
 
    // Name (make variable public to access from outside)
    public static final String KEY_JURUSAN = "jurusan";
 
    // Name (make variable public to access from outside)
    public static final String KEY_ANGKATAN = "semester";
 
    // Name (make variable public to access from outside)
    public static final String KEY_PEMBIMBING = "pembimbing";
 
    // Name (make variable public to access from outside)
    public static final String KEY_PRODI = "prodi";
 
    // Name (make variable public to access from outside)
    public static final String KEY_SEMESTER = "semester";
 
    // Name (make variable public to access from outside)
    public static final String KEY_KRS_ID = "krs_id";
 
    // Name (make variable public to access from outside)
    public static final String KEY_AWAL_KRS = "krs_mulai";
 
    // Name (make variable public to access from outside)
    public static final String KEY_AKHIR_KRS = "krs_selesai";
 
    // Name (make variable public to access from outside)
    public static final String KEY_TA = "smt_ta";
 
    // Name (make variable public to access from outside)
    public static final String KEY_MAX_SKS = "sks";
 
    // Name (make variable public to access from outside)
    public static final String KEY_IPK = "ipk";
 
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
 
    /**
     * Create login session
     * */
    public void createLoginSession(String username, String nama, String jurusan, String angkatan, String pembimbing, String prodi, String semester, String krs_id, 
    		String awal_krs, String akhir_krs, String smt_ta, String ipk, String sks){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
 
        // Storing name in pref
        editor.putString(KEY_USERNAME, username);
 
        // Storing email in pref
        editor.putString(KEY_NAMA, nama);
 
        // Storing email in pref
        editor.putString(KEY_JURUSAN, jurusan);
 
        // Storing email in pref
        editor.putString(KEY_ANGKATAN, angkatan);
 
        // Storing email in pref
        editor.putString(KEY_PEMBIMBING, pembimbing);
 
        // Storing email in pref
        editor.putString(KEY_PRODI, prodi);
 
        // Storing email in pref
        editor.putString(KEY_SEMESTER, semester);
 
        // Storing email in pref
        editor.putString(KEY_KRS_ID, krs_id);
 
        // Storing email in pref
        editor.putString(KEY_AWAL_KRS, awal_krs);
 
        // Storing email in pref
        editor.putString(KEY_AKHIR_KRS, akhir_krs);
 
        // Storing email in pref
        editor.putString(KEY_TA, smt_ta);
 
        // Storing email in pref
        editor.putString(KEY_IPK, ipk);
 
        // Storing email in pref
        editor.putString(KEY_MAX_SKS, sks);
 
        // commit changes
        editor.commit();
    }   
 
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, PortalSistemAkademikActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 
            // Staring Login Activity
            _context.startActivity(i);
        }
 
    }
 
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
 
        // user email id
        user.put(KEY_NAMA, pref.getString(KEY_NAMA, null));
 
        // user email id
        user.put(KEY_JURUSAN, pref.getString(KEY_JURUSAN, null));
 
        // user email id
        user.put(KEY_ANGKATAN, pref.getString(KEY_ANGKATAN, null));
 
        // user email id
        user.put(KEY_PEMBIMBING, pref.getString(KEY_PEMBIMBING, null));
 
        // user email id
        user.put(KEY_PRODI, pref.getString(KEY_PRODI, null));
 
        // user email id
        user.put(KEY_SEMESTER, pref.getString(KEY_SEMESTER, null));
 
        // user email id
        user.put(KEY_KRS_ID, pref.getString(KEY_KRS_ID, null));
 
        // user email id
        user.put(KEY_AWAL_KRS, pref.getString(KEY_AWAL_KRS, null));
 
        // user email id
        user.put(KEY_AKHIR_KRS, pref.getString(KEY_AKHIR_KRS, null));
 
        // user email id
        user.put(KEY_TA, pref.getString(KEY_TA, null));
 
        // user email id
        user.put(KEY_IPK, pref.getString(KEY_IPK, null));
 
        // user email id
        user.put(KEY_MAX_SKS, pref.getString(KEY_MAX_SKS, null));
 
        // return user
        return user;
    }
 
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
 
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, PortalSistemAkademikActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 
        // Staring Login Activity
        _context.startActivity(i);
    }
 
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
