package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.adapter.adapterParticipant;
import com.padyak.dto.CalendarEvent;
import com.padyak.dto.Participants;
import com.padyak.utility.CyclistHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class frmEventInfo extends AppCompatActivity {

    ImageView imgEvent;
    String eventId;
    Button btnEventRegister, btnEventParticipate, btnViewPayment;
    public static frmEventInfo frmEventInfo;

    boolean is_done, is_registered, is_ongoing, is_paid;
    String eventName, eventPhoto;
    TextView txEventInfoName, txEventInfoDesc, txEventInfoDate, txEventAward;
    RecyclerView rvParticipants;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterParticipant adapterParticipant;
    ProgressDialog progressDialog;

    String eventDate;
    String eventTime;
    String eventDescription;
    String eventAward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_info);
        frmEventInfo = this;
        eventId = getIntent().getStringExtra("eventId");
        imgEvent = findViewById(R.id.imgEvent);

        rvParticipants = findViewById(R.id.rvParticipants);
        linearLayoutManager = new LinearLayoutManager(this);
        rvParticipants.setLayoutManager(linearLayoutManager);

        txEventInfoName = findViewById(R.id.txEventInfoName);
        txEventInfoDesc = findViewById(R.id.txEventInfoDesc);
        txEventInfoDate = findViewById(R.id.txEventInfoDate);
        txEventAward = findViewById(R.id.txEventAward);
        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventParticipate = findViewById(R.id.btnEventParticipate);
        btnViewPayment = findViewById(R.id.btnViewPayment);
        btnEventParticipate.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(frmEventInfo.this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Event");
            alertDialog.setMessage("You can now start your ride. Press OK to proceed");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (d, w) -> {
                        Intent intent = new Intent(frmEventInfo, frmRide.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId",eventId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    });
            alertDialog.show();
        });
        btnViewPayment.setOnClickListener(v -> {
            Intent intent = new Intent(frmEventInfo.this, frmEventParticipants.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", eventId);
            bundle.putString("name", eventName);
            bundle.putString("img", eventPhoto);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        });
        btnEventRegister.setOnClickListener((e) -> {

            if (is_done) {
                Toast.makeText(frmEventInfo, "This event has already been completed", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Intent intent = new Intent(frmEventInfo.this, frmEventRegister.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", eventId);
                bundle.putString("name", eventName);
                bundle.putString("photoUrl", eventPhoto);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
        loadEventInfo();
    }

    public void loadEventInfo() {
        progressDialog = Helper.getInstance().progressDialog(frmEventInfo.this, "Retrieving event information.");
        progressDialog.show();
        new Thread(() -> {
            try {

                if (eventId.isEmpty()) throw new Exception("eventId is null");

                VolleyHttp volleyHttp = new VolleyHttp("/".concat(eventId), null, "event", com.padyak.activity.frmEventInfo.this);
                JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                if (responseJSON == null) throw new Exception("responseJSON is null");
                JSONObject eventObject = responseJSON.getJSONObject("data");
                String startTime = Helper.getInstance().ISOtoTime(eventObject.getString("startTime")).replace("+08:00", "");
                String endTime = Helper.getInstance().ISOtoTime(eventObject.getString("endTime")).replace("+08:00", "");
                is_registered = false;
                is_done = false;
                is_paid = false;
                eventDate = eventObject.getString("eventDate");
                eventTime = startTime.concat(" - ").concat(endTime);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                LocalDate ldDate = LocalDate.parse(eventDate);
                eventTime = formatter.format(ldDate).concat(" ").concat(eventTime);
                eventName = eventObject.getString("name");
                eventPhoto = eventObject.getString("photoUrl");
                eventDescription = eventObject.getString("eventDescription");
                eventAward = eventObject.getString("award");
                is_done = eventObject.getBoolean("isDone");
                is_ongoing = eventObject.getBoolean("isNow");
                JSONArray participantJSON = eventObject.optJSONArray("registeredUser");

                for (int i = 0; i < participantJSON.length(); i++) {
                    JSONObject participantObject = participantJSON.getJSONObject(i).getJSONObject("user");
                    if (participantJSON.getJSONObject(i).getString("status").equals("PAID") && participantObject.getString("id").equals(LoggedUser.getInstance().getUuid()))
                        is_paid = true;
                    if (participantObject.getString("id").equals(LoggedUser.getInstance().getUuid()))
                        is_registered = true;
                }
                Log.d(Helper.getInstance().log_code, "loadEventInfo: isRegistered "+ is_registered);
                Log.d(Helper.getInstance().log_code, "loadEventInfo: isDone "+ is_done);
                Log.d(Helper.getInstance().log_code, "loadEventInfo: isPaid "+ is_paid);
                runOnUiThread(() -> {
                    txEventInfoName.setText(eventName);
                    txEventInfoDesc.setText(eventDescription);
                    txEventAward.setText(eventAward);
                    txEventInfoDate.setText(eventTime);
                    Picasso.get().load(eventPhoto).into(imgEvent);

                    if (is_registered) {
                        btnEventRegister.setVisibility(View.GONE);
                        btnViewPayment.setVisibility(View.VISIBLE);
                    } else{
                        btnEventRegister.setVisibility(View.VISIBLE);
                        btnViewPayment.setVisibility(View.GONE);
                    }
                    if (is_registered && is_ongoing && is_paid) {
                        btnEventParticipate.setVisibility(View.VISIBLE);
                    } else if (is_registered && is_done) {
                        btnEventRegister.setVisibility(View.GONE);
                        btnEventParticipate.setVisibility(View.GONE);
                        btnViewPayment.setVisibility(View.VISIBLE);
                    }

                    if (!is_paid) {
                        btnEventParticipate.setVisibility(View.GONE);
                    }

                    progressDialog.dismiss();
                });

            } catch (Exception err) {
                Log.d(Helper.getInstance().log_code, "loadEventInfo: " + err.getMessage());
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(frmEventInfo, "Failed to load event information. Please try again", Toast.LENGTH_LONG).show();
                    finish();
                });
            } finally {
                runOnUiThread(() -> progressDialog.dismiss());
            }
        }).start();
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d(Helper.getInstance().log_code, "FCM_Message: " + message);
            try {
                CyclistHelper.getInstance().showMessageAlert(getSupportFragmentManager(),message);
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onReceive: " + e.getMessage());
                Log.d(Helper.getInstance().log_code, "onReceive: " + message);
            }
        }
    };
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("FCMIntentService"));
    }
}