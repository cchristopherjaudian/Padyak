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

import java.lang.reflect.Type;
import java.util.Map;

public class CyclistHelper {
    public static CyclistHelper cyclistHelper;

    public static CyclistHelper getInstance(){
        if(cyclistHelper == null) cyclistHelper = new CyclistHelper();
        return cyclistHelper;
    }
    public void showMessageAlert(FragmentManager fm, String message) throws JSONException {
        DialogFragment dialogFragment;
        Log.d(Helper.getInstance().log_code, "showMessageAlert: " + message);
        if(message.contains("receivers")){
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            JSONObject jsonObject = new JSONObject(message);
            String messageObject = jsonObject.getString("message");
            Map<String, Object> messageMap = new Gson().fromJson(messageObject,type);
            String receivers = messageMap.get("receivers").toString();
            if(!receivers.contains(LoggedUser.getLoggedUser().getPhoneNumber())) return;
            dialogFragment = StaticAlertFragment.newInstance(messageMap.get("message").toString());
            //dialogFragment = fragmentAlertLevel.newInstance(messageMap.get("message").toString());
        } else{
            dialogFragment = StaticAlertFragment.newInstance(message);
        }

        if(dialogFragment == null) return;
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "dialogFragment");
    }
    public void showAlertSuccess(Context context){
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        AlertSendFragment alertSendFragment = AlertSendFragment.newInstance("PADYAK ALERT SENT SUCCESSFULLY!");
        alertSendFragment.setCancelable(false);
        alertSendFragment.show(fm, "AlertSendFragment");
    }
}
