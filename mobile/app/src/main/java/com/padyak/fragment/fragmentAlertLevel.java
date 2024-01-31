package com.padyak.fragment;

import android.content.Intent;
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
import com.padyak.activity.frmAlertInfo;
import com.padyak.activity.frmMemberAlertInfo;
import com.padyak.dto.UserAlert;
import com.padyak.dto.UserAlertLevel;
import com.padyak.utility.Helper;

import org.json.JSONException;
import org.json.JSONObject;

public class fragmentAlertLevel extends DialogFragment {


    public fragmentAlertLevel() {
        // Required empty public constructor
    }
    public static fragmentAlertLevel newInstance(String message)  {
        Bundle bundle = new Bundle();
        String newMessage = "";
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONObject jsonMessage = jsonObject.getJSONObject("message");
            newMessage = jsonMessage.toString();
        }catch (Exception err){
            newMessage = message.replaceFirst("message=","");
            newMessage = newMessage.substring(1, newMessage.length() -1);
        }

        Log.d(Helper.getInstance().log_code, "newInstance: " + newMessage);
        bundle.putString("message", newMessage);
        fragmentAlertLevel fragment = new fragmentAlertLevel();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView textView62;
    Button btnAlertAck;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alert_level, container, false);
        textView62 = v.findViewById(R.id.textView62);
        btnAlertAck = v.findViewById(R.id.btnAlertAck);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        String receivedMessage = getArguments().getString("message");
        Gson gson = new Gson();
        UserAlertLevel userAlertLevel = gson.fromJson(receivedMessage, UserAlertLevel.class);
        Log.d(Helper.getInstance().log_code, "onCreateView: " + userAlertLevel);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(userAlertLevel.getUserName());
        stringBuffer.append(" sent an Alert Level ");
        stringBuffer.append(userAlertLevel.getAlertLevel());
        textView62.setText(stringBuffer.toString());

        btnAlertAck.setOnClickListener(l->{
            Intent intent = new Intent(v.getContext(), frmMemberAlertInfo.class);
            Bundle b = new Bundle();
            b.putString("message",receivedMessage);
            intent.putExtras(b);
            v.getContext().startActivity(intent);
            dismiss();
        });
        return v;
    }
}