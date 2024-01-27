package com.padyak.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.activity.TriggerActivity;
import com.padyak.components.OtpEditText;
import com.padyak.utility.Helper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertCancelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertCancelFragment extends DialogFragment {

    static String otp;

    public AlertCancelFragment() {
        // Required empty public constructor
    }


    public static AlertCancelFragment newInstance(String generatedOtp) {
        AlertCancelFragment fragment = new AlertCancelFragment();
        otp = generatedOtp;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    OtpEditText otpEditText;
    TextView txCountdown;
    Thread resetThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alert_cancel, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        otpEditText = v.findViewById(R.id.etOTP);
        txCountdown = v.findViewById(R.id.txCountdown);
        otpEditText.requestFocus();

        resetThread = new Thread(() -> {
            for (int i = 10; i > 0; i--) {
                final int count = i;
                txCountdown.post(() -> txCountdown.setText(String.valueOf(count)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d(Helper.getInstance().log_code, "onCreateView: " + e.getMessage());
                }

            }
            txCountdown.post(() -> {
                TriggerActivity.triggerActivity.sendSOS();
                dismiss();
            });
        });
        otpEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (otpEditText.getText().toString().trim().length() == 4) {
                    String inputOtp = otpEditText.getText().toString();
                    if (inputOtp.equals(otp)) {
                        dismiss();
                    } else {
                        otpEditText.setText("");
                        Toast.makeText(v.getContext(), "Invalid Pin Code. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });


        resetThread.start();
        return v;
    }
}