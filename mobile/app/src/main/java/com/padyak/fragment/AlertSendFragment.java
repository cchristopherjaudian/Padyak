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

import com.padyak.R;
import com.padyak.activity.frmAlertGroup;


public class AlertSendFragment extends DialogFragment {
    public AlertSendFragment() {

    }

    public static AlertSendFragment newInstance() {
        AlertSendFragment fragment = new AlertSendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btnAlertAck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alert_send, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        btnAlertAck = v.findViewById(R.id.btnAlertAck);
        btnAlertAck.setOnClickListener(l->{
            frmAlertGroup.frmAlertGroup.finish();
            dismiss();
        });
        return v;
    }
}