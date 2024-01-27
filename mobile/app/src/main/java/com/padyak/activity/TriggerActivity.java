package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aar.tapholdupbutton.TapHoldUpButton;
import com.padyak.R;
import com.padyak.dto.EmergencyContact;
import com.padyak.fragment.AlertCancelFragment;
import com.padyak.fragment.ContactSelectFragment;
import com.padyak.utility.Helper;

import java.util.List;

public class TriggerActivity extends AppCompatActivity {

    com.aar.tapholdupbutton.TapHoldUpButton btnTrigger;
    TextView txRecipients;
    LinearLayout llRecipients;
    ConstraintLayout clMain,constraintLayout2,clPin;
    ImageView imageView22;
    TextView txPinCode;
    String generatedOtp;
    ProgressDialog progressDialog;
    public static TriggerActivity triggerActivity;
    List<EmergencyContact> emergencyContactSet = Helper.getInstance().getTempEmergencySet();
    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        triggerActivity = this;

        progressDialog = Helper.getInstance().progressDialog(triggerActivity,"Retrieving emergency contacts. Please wait...");
        clMain = findViewById(R.id.clMain);
        clPin = findViewById(R.id.clPin);
        txPinCode = findViewById(R.id.txPinCode);

        imageView22= findViewById(R.id.imageView22);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        btnTrigger = findViewById(R.id.btnTrigger);
        txRecipients = findViewById(R.id.txRecipients);
        llRecipients = findViewById(R.id.llRecipients);
        txRecipients.setText("Your SOS will be sent to " + emergencyContactSet.size() + ((emergencyContactSet.size() > 1) ? "people" : "person"));
        btnTrigger.setOnButtonClickListener(new TapHoldUpButton.OnButtonClickListener() {
            @Override
            public void onLongHoldStart(View v) {
                generatedOtp = Helper.getInstance().generatePinCode();
                txPinCode.setText(generatedOtp);
                imageView22.setVisibility(View.INVISIBLE);
                clPin.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLongHoldEnd(View v) {
                imageView22.setVisibility(View.VISIBLE);
                clPin.setVisibility(View.INVISIBLE);
                FragmentManager fm = getSupportFragmentManager();
                AlertCancelFragment alertCancelFragment = AlertCancelFragment.newInstance(generatedOtp);
                alertCancelFragment.setCancelable(false);
                alertCancelFragment.show(fm, "AlertCancelFragment");
            }

            @Override
            public void onClick(View v) {

            }
        });
    }

    public void sendSOS(){
        Log.d(Helper.getInstance().log_code, "sendSOS");
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(TriggerActivity.this,"Sending SOS. Please wait...");
        progressDialog.show();
        new Thread(()->{
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Log.d(Helper.getInstance().log_code, "sendSOS: " + e.getMessage());
            } finally {
                progressDialog.dismiss();
            }
        }).start();
    }
}