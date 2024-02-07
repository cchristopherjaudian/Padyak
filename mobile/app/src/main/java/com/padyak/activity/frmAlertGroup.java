package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.adapter.adapterAlertGroup;
import com.padyak.dto.GroupContact;
import com.padyak.dto.UserAlert;
import com.padyak.dto.UserAlertLevel;
import com.padyak.fragment.AlertSendFragment;
import com.padyak.fragment.ContactSelectFragment;
import com.padyak.utility.Constants;
import com.padyak.utility.CyclistHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class frmAlertGroup extends AppCompatActivity {
    CheckBox chkSelectAll;
    RecyclerView rvAlertGroup;
    Button btnSendGroup;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterAlertGroup adapterAlertGroup;
    List<GroupContact> groupContacts;
    FusedLocationProviderClient fusedLocationClient;
    int alertLevel;
    ProgressDialog progressDialog;
    String receiver;
    public static frmAlertGroup frmAlertGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_group);
        frmAlertGroup = this;
        receiver = getIntent().getStringExtra("receiver");
        alertLevel = getIntent().getIntExtra("level",0);
        chkSelectAll = findViewById(R.id.chkSelectAll);
        btnSendGroup = findViewById(R.id.btnSendGroup);
        rvAlertGroup = findViewById(R.id.rvAlertGroup);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvAlertGroup.setLayoutManager(linearLayoutManager);

        btnSendGroup.setOnClickListener(v -> {


            String selectedUsers = adapterAlertGroup.getChecked();
            if(selectedUsers.isEmpty()){
                Toast.makeText(this, "Please select atleast 1 contact from the list.", Toast.LENGTH_LONG).show();
                return;
            }
            AlertDialog alertDialog = new AlertDialog.Builder(frmAlertGroup.this).create();
            alertDialog.setTitle("Send Alert");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to send this alert?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    (d,w)->{
                        sendAlert(selectedUsers);
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.show();


        });

        chkSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            adapterAlertGroup.setCheck(isChecked);
            adapterAlertGroup.notifyDataSetChanged();
        });
        loadContacts();

    }

    public void loadContacts() {
        progressDialog = Helper.getInstance().progressDialog(frmAlertGroup.this,"Retrieving list of contact.");
        progressDialog.show();
        new Thread(()->{
            String endpoint = receiver.equals("RESCUE") ? "contacts" : "user";
            VolleyHttp volleyHttp = new VolleyHttp("",null,endpoint, frmAlertGroup.this);
            JSONObject volleyObject = volleyHttp.getJsonResponse(true);
            runOnUiThread(()->{
                if(volleyObject == null){
                    Toast.makeText(this, "Failed to retrieve list of contact. Please try again.", Toast.LENGTH_LONG).show();
                    finish();
                } else{
                    groupContacts = new ArrayList<>();
                    if(receiver.equals("RESCUE")){
                        try {
                            JSONObject contactObject = volleyObject.getJSONObject("data");
                            JSONArray rescueArray = contactObject.optJSONArray("rescueGroups");
                            JSONArray emergencyArray = contactObject.optJSONArray("emergencyContacts");
                            for(int i = 0; i < rescueArray.length(); i++){
                                JSONObject rescueObj = rescueArray.getJSONObject(i);
                                GroupContact groupContact = new GroupContact();
                                groupContact.setSelected(false);
                                groupContact.setUserName(rescueObj.getString("name"));
                                groupContact.setUserImage("");
                                groupContact.setUserContact(rescueObj.getString("contact"));
                                groupContacts.add(groupContact);
                            }

                            for(int i = 0; i < emergencyArray.length(); i++){
                                JSONObject emergencyObj = emergencyArray.getJSONObject(i);
                                StringBuffer stringBuffer = new StringBuffer();
                                stringBuffer.append(emergencyObj.getString("firstname"));
                                if(emergencyObj.has("lastname") && !emergencyObj.isNull("lastname")){
                                    stringBuffer.append(" ");
                                    stringBuffer.append(emergencyObj.getString("lastname"));
                                }
                                GroupContact groupContact = new GroupContact();
                                groupContact.setSelected(false);
                                groupContact.setUserName(stringBuffer.toString());
                                groupContact.setUserImage("");
                                groupContact.setUserContact(emergencyObj.getString("contact"));
                                groupContacts.add(groupContact);
                            }
                        } catch (JSONException e) {
                            Log.d(Helper.getInstance().log_code, "loadContacts: " + e.getMessage());
                        }
                    } else{
                        JSONArray cyclistArray = volleyObject.optJSONArray("data");
                        for(int i = 0; i < cyclistArray.length(); i++){
                            try {
                                JSONObject cyclistObject = cyclistArray.getJSONObject(i);
                                if(cyclistObject.getString("id").equals(LoggedUser.getInstance().getUuid())) continue;
                                GroupContact groupContact = new GroupContact();
                                groupContact.setSelected(false);
                                groupContact.setUserName(cyclistObject.getString("firstname").concat(" ").concat(cyclistObject.getString("lastname")));
                                groupContact.setUserImage(cyclistObject.getString("photoUrl"));
                                groupContact.setUserContact(cyclistObject.getString("contactNumber"));
                                groupContacts.add(groupContact);
                            } catch (JSONException e) {
                                Log.d(Helper.getInstance().log_code, "loadContacts: " + e.getMessage());
                            }
                        }
                    }

                }
                adapterAlertGroup = new adapterAlertGroup(groupContacts);
                rvAlertGroup.setAdapter(adapterAlertGroup);
                progressDialog.dismiss();
            });
        }).start();

    }

    private void sendAlert(String recipients) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable this application in Location permission using Android Settings", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        progressDialog = Helper.getInstance().progressDialog(frmAlertGroup.this,"Sending alert.");
        progressDialog.show();
        new Thread(()->{
            try {
                CompletableFuture<Map<String,Object>> locationFuture = Helper.getInstance().getCurrentLocation(frmAlertGroup.this);
                UserAlertLevel userAlertLevel = new UserAlertLevel(
                        LoggedUser.getLoggedUser().getUuid(),
                        Helper.getInstance().formatDate(LocalDateTime.now().toLocalDate().toString()),
                        Helper.getInstance().formatTime(LocalDateTime.now().toLocalTime().toString()),
                        LoggedUser.getLoggedUser().getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()),
                        locationFuture.get().get("name").toString(),
                        Double.parseDouble(locationFuture.get().get("latitude").toString()),
                        Double.parseDouble(locationFuture.get().get("longitude").toString()),
                        Constants.alertMap.get(alertLevel),
                        alertLevel,
                        recipients,
                        LoggedUser.getLoggedUser().getImgUrl(),
                        LoggedUser.getLoggedUser().getPhoneNumber()
                );
                String userAlertPayload = new Gson().toJson(userAlertLevel);
                Map<String, Object> payload = new HashMap<>();
                payload.put("message",userAlertPayload);
                payload.put("topic","admin-review");
                Log.d(Helper.getInstance().log_code, "sendSOS: " + payload);
                VolleyHttp volleyHttp = new VolleyHttp("/notify",payload,"alert",frmAlertGroup.this);
                String response = volleyHttp.getResponseBody(true);
                JSONObject reader = new JSONObject(response);
                int responseStatus = reader.getInt("status");
                runOnUiThread(()->progressDialog.dismiss());
                if(responseStatus != 200) throw new Exception("Response Status: " + responseStatus);
                runOnUiThread(()->{
                    frmAlertSend.frmAlertSend.finish();
                    frmAlertInfo.frmAlertInfo.finish();
                    frmMain.frmMain.showAlert = 1;
                    finish();
                });

            } catch (Exception e) {
                Log.d(Helper.getInstance().log_code, "sendSOS: " + e.getMessage());
                runOnUiThread(()-> Toast.makeText(this, "Failed to send SOS. Please try again.", Toast.LENGTH_LONG).show());
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