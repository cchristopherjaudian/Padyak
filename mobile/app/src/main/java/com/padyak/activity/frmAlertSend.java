package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

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
import com.padyak.fragment.AlertSendFragment;
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
    Button btnAlertGroup, btnSendContact;
    String alertDescription;
    int alertLevel;
    public static frmAlertSend frmAlertSend;
    FusedLocationProviderClient fusedLocationClient;
    ProgressDialog progressDialog;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_send);
        frmAlertSend = this;
        Bundle bundle = getIntent().getExtras();
        btnSendContact = findViewById(R.id.btnSendContact);
        btnAlertGroup = findViewById(R.id.btnAlertGroup);
        txAlertLevel = findViewById(R.id.txAlertLevel);
        txAlertDescription = findViewById(R.id.txAlertDescription);
        alertLevel = bundle.getInt("level");
        alertDescription = bundle.getString("Description");

        txAlertLevel.setText("Level " + alertLevel);
        txAlertDescription.setText(alertDescription);
        intent = new Intent(frmAlertSend.this, frmAlertGroup.class);
        btnAlertGroup.setOnClickListener(v -> {
            Bundle bundleAlert = new Bundle();
            bundleAlert.putInt("level",alertLevel);
            bundleAlert.putString("receiver","GROUP");
            intent.putExtras(bundleAlert);
            startActivity(intent);
        });
        btnSendContact.setOnClickListener(v -> {
            Bundle bundleAlert = new Bundle();
            bundleAlert.putInt("level",alertLevel);
            bundleAlert.putString("receiver","RESCUE");
            intent.putExtras(bundleAlert);
            startActivity(intent);
        });
    }
}