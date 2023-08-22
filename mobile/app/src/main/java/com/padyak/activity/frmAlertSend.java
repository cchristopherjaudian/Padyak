package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frmAlertSend extends AppCompatActivity {
    TextView txAlertLevel, txAlertDescription;
    EditText etContact;
    Button btnAlertGroup, btnSendContact;
    String alertDescription;
    int alertLevel;
    public static frmAlertSend frmAlertSend;
    FusedLocationProviderClient fusedLocationClient;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_send);
        frmAlertSend = this;
        Bundle bundle = getIntent().getExtras();
        btnSendContact = findViewById(R.id.btnSendContact);
        btnAlertGroup = findViewById(R.id.btnAlertGroup);
        etContact = findViewById(R.id.etContact);
        txAlertLevel = findViewById(R.id.txAlertLevel);
        txAlertDescription = findViewById(R.id.txAlertDescription);
        alertLevel = bundle.getInt("level");
        alertDescription = bundle.getString("Description");

        txAlertLevel.setText("Level " + alertLevel);
        txAlertDescription.setText(alertDescription);

        btnAlertGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmAlertSend.this, frmAlertGroup.class);
                Bundle bundleAlert = new Bundle();
                bundleAlert.putInt("level",alertLevel);
                intent.putExtras(bundleAlert);
                startActivity(intent);
            }
        });
        btnSendContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendTo = etContact.getText().toString().trim();
                sendAlert(sendTo);
            }
        });
    }

    private void sendAlert(String recipients) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable this application in Location permission using Android Settings", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(frmAlertSend.this).create();
        alertDialog.setTitle("Send Alert");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to send this alert?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                (d,w)->{
                    progressDialog = Helper.getInstance().progressDialog(com.padyak.activity.frmAlertSend.this,"Sending alert.");
                    progressDialog.show();

                    new Thread(()->{
                        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(android.location.Location location) {
                                        if (location != null) {
                                            double _lat = location.getLatitude();
                                            double _long = location.getLongitude();
                                            String fromLocationURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + _lat + "," + _long + "&key=" + getString(R.string.maps_publicapi);
                                            VolleyHttp fromVolley = new VolleyHttp(fromLocationURL, null, "MAP", frmAlertSend.this);
                                            String alertAddress = Helper.getInstance().generateAddress(fromVolley.getResponseBody(false));


                                            Map<String, Object> payload = new HashMap<>();
                                            payload.put("to",recipients);
                                            payload.put("level",alertLevel);
                                            payload.put("location",alertAddress);
                                            payload.put("latitude",_lat);
                                            payload.put("longitude",_long);

                                            VolleyHttp volleyHttp = new VolleyHttp("",payload,"alert", com.padyak.activity.frmAlertSend.this);
                                            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                                            runOnUiThread(()->{
                                                progressDialog.dismiss();
                                                if(responseJSON == null){
                                                    Toast.makeText(frmAlertSend, "Failed to send alert. Please try again", Toast.LENGTH_SHORT).show();
                                                } else{
                                                    Toast.makeText(frmAlertSend.this, "Alert sent successfully", Toast.LENGTH_SHORT).show();
                                                    frmAlertInfo.frmAlertInfo.finish();
                                                    finish();
                                                }
                                            });

                                        } else{
                                            runOnUiThread(()->{
                                                progressDialog.dismiss();
                                                Toast.makeText(frmAlertSend.this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    }
                                });
                    }).start();
                });
        alertDialog.show();

    }
}