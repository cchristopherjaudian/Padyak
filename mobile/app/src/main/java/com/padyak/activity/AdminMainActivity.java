package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.dto.UserAlert;
import com.padyak.fragment.AlertSendFragment;
import com.padyak.fragment.ContactSelectFragment;
import com.padyak.fragment.StaticAlertFragment;
import com.padyak.fragment.fragmentAlertAck;
import com.padyak.fragment.fragmentAlertLevel;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AdminMainActivity extends AppCompatActivity {
    CardView cardAdminProfile;
    RelativeLayout rlEvents, rlAlert, rlTrack,rlValidation;
    TextView txMainProfileName, txProfileDay;
    ImageView imgAdminProfile;
    ProgressDialog progressDialog;
    Intent intent;
    public int showMessage = 0;
    public static AdminMainActivity adminMainActivity;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            try {
                showDialog(message);
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onReceive: " + e.getMessage());
                Log.d(Helper.getInstance().log_code, "onReceive: " + message);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        adminMainActivity = this;
        rlEvents = findViewById(R.id.rlEvents);
        rlAlert = findViewById(R.id.rlAlert);
        rlTrack = findViewById(R.id.rlTrack);
        rlValidation = findViewById(R.id.rlValidation);
        cardAdminProfile = findViewById(R.id.cardAdminProfile);
        txMainProfileName = findViewById(R.id.txMainProfileName);
        txProfileDay = findViewById(R.id.txProfileDay);
        imgAdminProfile = findViewById(R.id.imgAdminProfile);


        cardAdminProfile.setOnClickListener(v->{
            Intent intent = new Intent(AdminMainActivity.this,ProfileActivity.class);
            startActivity(intent);
        });
        rlEvents.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this, frmEventCrud.class);
            startActivity(intent);
        });
        rlAlert.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this, frmAdminAlert.class);
            startActivity(intent);
        });
        rlTrack.setOnClickListener(v -> {
            progressDialog = Helper.getInstance().progressDialog(AdminMainActivity.this, "Retrieving current event.");
            progressDialog.show();
            new Thread(() -> {
                VolleyHttp volleyHttp = new VolleyHttp("/now", null, "event", AdminMainActivity.this);
                JSONObject jsonObject = volleyHttp.getJsonResponse(true);
                runOnUiThread(() -> {
                    if (jsonObject == null) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Failed to retrieve current event. Please try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        progressDialog.dismiss();
                        JSONObject eventObject = jsonObject.optJSONArray("data").getJSONObject(0);
                        String eventId = eventObject.getString("id");
                        String eventName = eventObject.getString("name");
                        String eventPhoto = eventObject.getString("photoUrl");
                        Bundle b = new Bundle();
                        b.putString("id", eventId);
                        b.putString("name", eventName);
                        b.putString("photoUrl", eventPhoto);
                        intent = new Intent(AdminMainActivity.this, frmTrackEvent.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    } catch (JSONException e) {

                        Toast.makeText(this, "Failed to retrieve current event. Please try again.", Toast.LENGTH_LONG).show();
                        Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                    }
                });

            }).start();


        });
        rlValidation.setOnClickListener(v->{
            intent = new Intent(AdminMainActivity.this,ValidationMenuActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("FCMIntentService"));
        txMainProfileName.setText("Hey ".concat(LoggedUser.getInstance().getFirstName()));
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgAdminProfile);
        LocalDate dateNow = LocalDate.now();
        String dayToday = dateNow.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        txProfileDay.setText(dayToday.toUpperCase().concat("|").concat(dateNow.format(formatter)));
        if(showMessage == 1){
            showMessage();
        }
    }

    public void showDialog(String message) throws JSONException {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment dialogFragment;
        if(message.contains("receivers")){
            dialogFragment = fragmentAlertLevel.newInstance(message);
        } else{
            dialogFragment = StaticAlertFragment.newInstance(message);
        }
        dialogFragment.setCancelable(false);
        dialogFragment.show(fm, "dialogFragment");
    }
    public void showMessage(){
        showMessage = 0;
        FragmentManager fm = getSupportFragmentManager();
        AlertSendFragment alertSendFragment = AlertSendFragment.newInstance("Acknowledgement Successfully Sent");
        alertSendFragment.setCancelable(false);
        alertSendFragment.show(fm, "AlertSendFragment");
    }
}