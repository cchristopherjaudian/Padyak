package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import com.padyak.R;
import com.padyak.adapter.adapterAlertGroup;
import com.padyak.dto.GroupContact;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frmAlertGroup extends AppCompatActivity {
    CheckBox chkSelectAll;
    RecyclerView rvAlertGroup;
    Button btnSendGroup;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterAlertGroup adapterAlertGroup;
    List<GroupContact> groupContacts;
    FusedLocationProviderClient fusedLocationClient;
    int alertLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_group);
        alertLevel = getIntent().getIntExtra("level",0);
        chkSelectAll = findViewById(R.id.chkSelectAll);
        btnSendGroup = findViewById(R.id.btnSendGroup);
        rvAlertGroup = findViewById(R.id.rvAlertGroup);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvAlertGroup.setLayoutManager(linearLayoutManager);

        btnSendGroup.setOnClickListener(v -> {
            String selectedUsers = adapterAlertGroup.getChecked();
            sendAlert(selectedUsers);

        });

        chkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapterAlertGroup.setCheck(isChecked);
                adapterAlertGroup.notifyDataSetChanged();
            }
        });
        new Thread(()->{
            runOnUiThread(this::loadContacts);
        }).start();
    }

    public void loadContacts() {

        VolleyHttp volleyHttp = new VolleyHttp("",null,"user", frmAlertGroup.this);
        JSONObject volleyObject = volleyHttp.getJsonResponse(true);
        if(volleyObject == null){
            Toast.makeText(this, "Failed to retrieve list of cyclists. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        } else{
            groupContacts = new ArrayList<>();
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
        adapterAlertGroup = new adapterAlertGroup(groupContacts);
        rvAlertGroup.setAdapter(adapterAlertGroup);
    }

    private void sendAlert(String recipients) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable this application in Location permission using Android Settings", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if (location != null) {
                            double _lat = location.getLatitude();
                            double _long = location.getLongitude();
                            String fromLocationURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + _lat + "," + _long + "&key=" + getString(R.string.maps_publicapi);
                            VolleyHttp fromVolley = new VolleyHttp(fromLocationURL, null, "MAP", frmAlertGroup.this);
                            String alertAddress = Helper.getInstance().generateAddress(fromVolley.getResponseBody(false));


                            Map<String, Object> payload = new HashMap<>();
                            payload.put("to",recipients);
                            payload.put("level",alertLevel);
                            payload.put("location",alertAddress);
                            payload.put("latitude",_lat);
                            payload.put("longitude",_long);

                            VolleyHttp volleyHttp = new VolleyHttp("",payload,"alert", frmAlertGroup.this);
                            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                            if(responseJSON == null){
                                Toast.makeText(frmAlertGroup.this, "Failed to send alert. Please try again", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(frmAlertGroup.this, "Alert sent successfully.", Toast.LENGTH_SHORT).show();
                                frmAlertInfo.frmAlertInfo.finish();
                                frmAlertSend.frmAlertSend.finish();
                                finish();
                            }
                        } else{
                            Toast.makeText(frmAlertGroup.this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}