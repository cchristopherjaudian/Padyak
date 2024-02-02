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
import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.dto.EmergencyContact;
import com.padyak.dto.UserAlert;
import com.padyak.fragment.AlertCancelFragment;
import com.padyak.fragment.ContactSelectFragment;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TriggerActivity extends AppCompatActivity {

    com.aar.tapholdupbutton.TapHoldUpButton btnTrigger;
    TextView txRecipients;
    LinearLayout llRecipients;
    ConstraintLayout clMain,constraintLayout2,clPin;
    ImageView imageView22;
    TextView txPinCode;
    String generatedOtp;
    ProgressDialog progressDialog;
    String responseMessage = "";
    public static TriggerActivity triggerActivity;
    List<EmergencyContact> emergencyContactSet = Helper.getInstance().getTempEmergencySet();
    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        triggerActivity = this;

        progressDialog = Helper.getInstance().progressDialog(triggerActivity,"Retrieving emergency contacts.");
        clMain = findViewById(R.id.clMain);
        clPin = findViewById(R.id.clPin);
        txPinCode = findViewById(R.id.txPinCode);

        imageView22= findViewById(R.id.imageView22);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        btnTrigger = findViewById(R.id.btnTrigger);
        txRecipients = findViewById(R.id.txRecipients);
        llRecipients = findViewById(R.id.llRecipients);
        txRecipients.setText("Your SOS will be sent to " + emergencyContactSet.size() + ((emergencyContactSet.size() > 1) ? " people" : " person"));
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void sendSOS(){
        Log.d(Helper.getInstance().log_code, "sendSOS");

        ProgressDialog progressDialog = Helper.getInstance().progressDialog(TriggerActivity.this,"Sending SOS. Please wait...");
        progressDialog.show();
        new Thread(()->{
            try {
                CompletableFuture<Map<String,Object>> locationFuture = Helper.getInstance().getCurrentLocation(TriggerActivity.this);

                UserAlert userAlert = new UserAlert(
                        LoggedUser.getLoggedUser().getUuid(),
                        Helper.getInstance().formatDate(LocalDateTime.now().toLocalDate().toString()),
                        Helper.getInstance().formatTime(LocalDateTime.now().toLocalTime().toString()),
                        LoggedUser.getLoggedUser().getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()),
                        locationFuture.get().get("name").toString(),
                        Double.parseDouble(locationFuture.get().get("latitude").toString()),
                        Double.parseDouble(locationFuture.get().get("longitude").toString())
                );

                String userAlertPayload = new Gson().toJson(userAlert);
                List<EmergencyContact> emergencyContacts = Helper.getInstance().getTempEmergencySet();
                emergencyContacts.forEach(em->{
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("message",userAlertPayload);
                    payload.put("topic",em.getContact());
                    Log.d(Helper.getInstance().log_code, "sendSOS: " + payload);
                    VolleyHttp volleyHttp = new VolleyHttp("/notify",payload,"alert",TriggerActivity.this);
                    String response = volleyHttp.getResponseBody(true);
                    Log.d(Helper.getInstance().log_code, "sendSOS: " + response);
                });

                //int responseStatus = reader.getInt("status");
                //if(responseStatus != 200) throw new Exception("Response Status: " + responseStatus);
                responseMessage = "Alert sent successfully";
            } catch (Exception e) {
                Log.d(Helper.getInstance().log_code, "sendSOS: " + e.getMessage());
                responseMessage = "Failed to send SOS. Please try again.";

            } finally {
                runOnUiThread(()-> Toast.makeText(this, responseMessage, Toast.LENGTH_LONG).show());
                progressDialog.dismiss();
                finish();
            }
        }).start();
    }
}