package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class frmMemberAlertInfo extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {

    String alertId;
    Button btnCancelAlert, btnConfirmAlert;
    TextView txAlertAddress, txAlertTime, txAlertName;
    TextView txAlertDescription, txAlertLevel;
    ImageView imgDP;
    double latitude;
    double longitude;
    String memberName, locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmMemberAlertInfo.this);
        setContentView(R.layout.activity_frm_member_alert_info);

        latitude = getIntent().getDoubleExtra("latitude", 0d);
        longitude = getIntent().getDoubleExtra("longitude", 0d);

        alertId = getIntent().getStringExtra("id");

        memberName = getIntent().getStringExtra("name");
        locationName = getIntent().getStringExtra("location");

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapAlertInfo);
        mapFragment.getMapAsync(this);

        btnConfirmAlert = findViewById(R.id.btnConfirmAlert);
        btnCancelAlert = findViewById(R.id.btnCancelAlert);

        txAlertName = findViewById(R.id.txAlertName);
        txAlertTime = findViewById(R.id.txAlertTime);
        txAlertAddress = findViewById(R.id.txAlertAddress);

        txAlertLevel = findViewById(R.id.txAlertLevel);
        txAlertDescription = findViewById(R.id.txAlertDescription);

        imgDP = findViewById(R.id.imgDP);


        txAlertName.setText(memberName);
        txAlertTime.setText(getIntent().getStringExtra("date"));
        txAlertAddress.setText(locationName);

        Picasso.get().load(getIntent().getStringExtra("photoUrl")).into(imgDP);

        txAlertLevel.setText("Level ".concat(String.valueOf(getIntent().getIntExtra("level", 0))));
        txAlertDescription.setText(Helper.getInstance().getAlertDescription(getIntent().getIntExtra("level", 0)));

        btnCancelAlert.setOnClickListener(v -> finish());
        btnConfirmAlert.setOnClickListener(v -> {
            setStatus("COMPLETED");
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng myPos = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 16.0f));


        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(myPos)
                .title(memberName)
                .snippet(locationName));
        if (marker != null) marker.showInfoWindow();
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }

    @Override
    public void onBackPressed() {

    }

    public void setStatus(String status){
        Map<String, Object> payload = new HashMap<>();
        payload.put("status",status);
        VolleyHttp volleyHttp = new VolleyHttp("/".concat(alertId),payload,"alert-patch",frmMemberAlertInfo.this);
        JSONObject responseJSON = volleyHttp.getJsonResponse(true);
        if(responseJSON == null){
            Toast.makeText(this, "Failed to confirm alert. Please try again", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Alert confirmed successfully.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}