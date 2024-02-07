package com.padyak.utility;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.padyak.fragment.AlertSendFragment;
import com.padyak.fragment.StaticAlertFragment;

import org.json.JSONException;
import org.json.JSONObject;
import com.padyak.fragment.fragmentAlertLevel;
import java.lang.reflect.Type;
import java.util.Map;

public class AdminHelper {

    public static AdminHelper adminHelper;

    public static AdminHelper getInstance(){
        if(adminHelper == null) adminHelper = new AdminHelper();
        return adminHelper;
    }
    public static void showMessageAlert(FragmentManager fm, String message) throws JSONException {
        DialogFragment dialogFragment = null;
        if(message.contains("receivers")) {
            dialogFragment = fragmentAlertLevel.newInstance(message);
        }
        if(dialogFragment == null) return;
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "dialogFragment");
    }
//    public static void showAlertSuccess(Context context){
//        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
//        AlertSendFragment alertSendFragment = AlertSendFragment.newInstance("PADYAK ALERT SENT SUCCESSFULLY!");
//        alertSendFragment.setCancelable(false);
//        alertSendFragment.show(fm, "AlertSendFragment");
//    }
}
