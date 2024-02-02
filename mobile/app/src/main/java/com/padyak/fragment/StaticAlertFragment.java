package com.padyak.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.dto.UserAlert;
import com.padyak.utility.Helper;

import org.json.JSONException;
import org.json.JSONObject;

public class StaticAlertFragment extends DialogFragment {
    public StaticAlertFragment() {
        // Required empty public constructor
    }

    public static StaticAlertFragment newInstance(String message) throws JSONException {
        Bundle bundle = new Bundle();
        String newMessage = "";
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONObject jsonMessage = jsonObject.getJSONObject("message");
            newMessage = jsonMessage.toString();
        }catch (Exception err){
            if(message.contains("message=")){
                newMessage = message.replaceFirst("message=","");
                newMessage = newMessage.substring(1, newMessage.length() -1);
            } else{
                newMessage = message;
            }
        }
        Log.d(Helper.getInstance().log_code, "newInstance: " + newMessage);
        if(newMessage.startsWith("null")) return null;
        bundle.putString("message", newMessage);
        StaticAlertFragment dialogFragment = new StaticAlertFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextView textView61;
    Button btnAlertAck;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View v = inflater.inflate(R.layout.fragment_static_alert, container, false);
        textView61 = v.findViewById(R.id.textView61);
        btnAlertAck = v.findViewById(R.id.btnAlertAck);
        String receivedMessage = getArguments().getString("message");
        try {
            Gson gson = new Gson();
            UserAlert userAlert = gson.fromJson(receivedMessage, UserAlert.class);
            Log.d(Helper.getInstance().log_code, "onCreateView: " + userAlert);
            if(userAlert.getUserId() == null) throw new Exception("Invalid message");
            StringBuffer sbf = new StringBuffer();
            sbf.append(userAlert.getAlertDate());
            sbf.append(" At ").append(userAlert.getAlertTime());
            sbf.append("\nThis is ").append(userAlert.getUserName());
            sbf.append(". I have an accident. I need help!!");
            sbf.append(" Here\'s my location ").append(userAlert.getAddressName());
            textView61.setText(sbf.toString());
        } catch (Exception err){
            textView61.setText(receivedMessage);
            Log.d(Helper.getInstance().log_code, "onCreateView: " + err.getMessage());
        }

        btnAlertAck.setOnClickListener(l->{
            dismiss();
        });
        return v;
    }
}