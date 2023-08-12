package com.padyak.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Prefs {
    private static Prefs prefs;
    private String spTag = "padyak";

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
            LoggedUser.getInstance().setIs_admin(sharedPreferences.getBoolean("is_admin", false));
            LoggedUser.getInstance().setUuid(sharedPreferences.getString("uuid", ""));
            LoggedUser.getInstance().setImgUrl(sharedPreferences.getString("imgUrl", ""));
            LoggedUser.getInstance().setFirstName(sharedPreferences.getString("firstName", ""));
            LoggedUser.getInstance().setLastName(sharedPreferences.getString("lastName", ""));
            LoggedUser.getInstance().setEmail(sharedPreferences.getString("email", ""));
            LoggedUser.getInstance().setGender(sharedPreferences.getString("gender", ""));
            LoggedUser.getInstance().setBirthDate(sharedPreferences.getString("birthDate", ""));
            LoggedUser.getInstance().setPhoneNumber(sharedPreferences.getString("phoneNumber", ""));
            LoggedUser.getInstance().setWeight(sharedPreferences.getString("weight", ""));
            LoggedUser.getInstance().setHeight(sharedPreferences.getString("height", ""));
            return true;
        } catch (Exception err) {
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
