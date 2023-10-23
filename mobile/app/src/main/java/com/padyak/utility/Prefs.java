package com.padyak.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Prefs {
    private static Prefs prefs;
    private String spTag = "padyak";

    public final static String ADMIN_KEY = "isAdmin";
    public final static String PASSWORD_KEY = "password";
    public final static String ID_KEY = "id";
    public final static String IMG_KEY = "photoUrl";
    public final static String FN_KEY = "firstname";
    public final static String LN_KEY = "lastname";
    public final static String EMAIL_KEY = "emailAddress";
    public final static String GENDER_KEY = "gender";
    public final static String BDAY_KEY = "birthday";
    public final static String PHONE_KEY = "contactNumber";
    public final static String WEIGHT_KEY = "weight";
    public final static String HEIGHT_KEY = "height";
    public final static String AUTH = "auth";



    public Prefs() {
    }

    public static Prefs getInstance() {
        if (prefs == null) prefs = new Prefs();
        return prefs;
    }

    public boolean getUser(Context c) {
        try {
            SharedPreferences sharedPreferences = c.getSharedPreferences(spTag, Context.MODE_PRIVATE);
            if(sharedPreferences == null) throw new Exception("");
            //LoggedUser.getInstance().setIs_admin(sharedPreferences.getBoolean(ADMIN_KEY, false));
            LoggedUser.getInstance().setUuid(sharedPreferences.getString(ID_KEY, ""));
            LoggedUser.getInstance().setImgUrl(sharedPreferences.getString(IMG_KEY, ""));
            LoggedUser.getInstance().setFirstName(sharedPreferences.getString(FN_KEY, ""));
            LoggedUser.getInstance().setLastName(sharedPreferences.getString(LN_KEY, ""));
            LoggedUser.getInstance().setEmail(sharedPreferences.getString(EMAIL_KEY, ""));
            LoggedUser.getInstance().setGender(sharedPreferences.getString(GENDER_KEY, ""));
            LoggedUser.getInstance().setBirthDate(sharedPreferences.getString(BDAY_KEY, ""));
            LoggedUser.getInstance().setPhoneNumber(sharedPreferences.getString(PHONE_KEY, ""));
            LoggedUser.getInstance().setWeight(sharedPreferences.getString(WEIGHT_KEY, ""));
            LoggedUser.getInstance().setHeight(sharedPreferences.getString(HEIGHT_KEY, ""));
            LoggedUser.getInstance().setAuth(sharedPreferences.getString(AUTH, ""));
            LoggedUser.getInstance().setPassword(sharedPreferences.getString(PASSWORD_KEY, ""));
            Log.d(Helper.getInstance().log_code, "getUser: " + LoggedUser.getInstance().toString());
            return true;
        } catch (Exception err) {
            Log.d(Helper.getInstance().log_code, "getUser: " + err.getMessage());
            return false;
        }
    }

    public boolean setUser(Context c, String k, Object v) {
        try {

            SharedPreferences sharedPreferences = c.getSharedPreferences(spTag, Context.MODE_PRIVATE);
            SharedPreferences.Editor gEdit = sharedPreferences.edit();
            if(v instanceof Boolean){
                gEdit.putBoolean(k,(Boolean)v);
            } else{
                gEdit.putString(k,(String)v);
            }
            gEdit.apply();
            getUser(c);
            return true;
        } catch (Exception err) {
            return false;
        }
    }
}
