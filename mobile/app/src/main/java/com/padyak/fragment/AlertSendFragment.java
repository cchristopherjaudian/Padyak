package com.padyak.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.activity.frmAlertGroup;


public class AlertSendFragment extends DialogFragment {
    public AlertSendFragment() {

    }

    public static AlertSendFragment newInstance(String message) {
        AlertSendFragment fragment = new AlertSendFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btnAlertAck;
TextView textView61;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alert_send, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        btnAlertAck = v.findViewById(R.id.btnAlertAck);
        textView61 = v.findViewById(R.id.textView61);
        textView61.setText(getArguments().getString("message"));
        btnAlertAck.setOnClickListener(l->{
            frmAlertGroup.frmAlertGroup.finish();
            dismiss();
        });
        return v;
    }
}