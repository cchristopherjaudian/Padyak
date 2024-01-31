package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.dto.UserAlertLevel;
import com.padyak.fragment.AlertSendFragment;
import com.padyak.utility.Constants;
import com.padyak.utility.Helper;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
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
    int alertLevel;
    String memberName, locationName,receivers;
    String alertTime;
    String photoUrl;
    ProgressDialog progressDialog;
    UserAlertLevel userAlertLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmMemberAlertInfo.this);
        setContentView(R.layout.activity_frm_member_alert_info);
        if(getIntent().hasExtra("message")){
            Gson gson = new Gson();
            userAlertLevel = gson.fromJson(getIntent().getStringExtra("message"), UserAlertLevel.class);

            latitude = userAlertLevel.getLatitude();
            longitude = userAlertLevel.getLongitude();
            memberName = userAlertLevel.getUserName();
            locationName = userAlertLevel.getAddressName();
            alertTime = userAlertLevel.getAlertDate().concat(" ").concat(userAlertLevel.getAlertTime());
            photoUrl = userAlertLevel.getPhotoUrl();
            alertLevel = userAlertLevel.getAlertLevel();
            receivers = userAlertLevel.getReceivers();
        } else{
            latitude = getIntent().getDoubleExtra("latitude", 0d);
            longitude = getIntent().getDoubleExtra("longitude", 0d);
            alertId = getIntent().getStringExtra("id");
            memberName = getIntent().getStringExtra("name");
            locationName = getIntent().getStringExtra("location");
            alertTime = getIntent().getStringExtra("date").replace("T"," ").replace("+08:00","");
            photoUrl = getIntent().getStringExtra("photoUrl");
            alertLevel = getIntent().getIntExtra("level", 0) + 1;
            btnConfirmAlert.setVisibility(View.INVISIBLE);
        }


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
        txAlertTime.setText(alertTime);
        txAlertAddress.setText(locationName);
        Picasso.get().load(photoUrl).into(imgDP);
        txAlertLevel.setText("Level ".concat(String.valueOf(alertLevel)));
        txAlertDescription.setText(Constants.alertMap.get(alertLevel));
        btnCancelAlert.setOnClickListener(v -> finish());
        btnConfirmAlert.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(frmMemberAlertInfo.this).create();
            alertDialog.setTitle("Member Alert");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to confirm this alert?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes",(d,w)->{
                acknowledgeAlert();
            });
            alertDialog.show();

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
    public void acknowledgeAlert(){
        progressDialog = Helper.getInstance().progressDialog(frmMemberAlertInfo.this,"Acknowledging Alert.");
        progressDialog.show();
        new Thread(()->{

            Map<String, String> userPayload = new HashMap<>();
            userPayload.put("firstname", memberName );
            userPayload.put("lastname","test");
            userPayload.put("photoUrl",photoUrl);
            userPayload.put("id", LocalDateTime.now().toString());

            String jsonParsed = new Gson().toJson(userPayload);
            Map<String, Object> payload = new HashMap<>();
            payload.put("to",receivers);
            payload.put("level",alertLevel-1);
            payload.put("location",locationName);
            payload.put("latitude",latitude);
            payload.put("longitude",longitude);
            payload.put("sender",jsonParsed);

            VolleyHttp volleyHttp = new VolleyHttp("",payload,"alert", frmMemberAlertInfo.this);
            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
            runOnUiThread(()->{
                progressDialog.dismiss();
                if(responseJSON == null){
                    Toast.makeText(frmMemberAlertInfo.this, "Failed to send alert. Please try again", Toast.LENGTH_LONG).show();
                } else{
                    AdminMainActivity.adminMainActivity.showMessage = 1;
                    frmMemberAlertInfo.this.finish();
                }

            });

        }).start();
    }
    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }

    @Override
    public void onBackPressed() {

    }
    public void setStatus(String status){
        progressDialog = Helper.getInstance().progressDialog(frmMemberAlertInfo.this,"Updating alert.");
        progressDialog.show();
        new Thread(()->{
            Map<String, Object> payload = new HashMap<>();
            payload.put("status",status);
            VolleyHttp volleyHttp = new VolleyHttp("/".concat(alertId),payload,"alert-patch",frmMemberAlertInfo.this);
            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
            runOnUiThread(()->{
                progressDialog.dismiss();
                if(responseJSON == null){
                    Toast.makeText(this, "Failed to confirm alert. Please try again", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(this, "Alert confirmed successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        }).start();

    }
}