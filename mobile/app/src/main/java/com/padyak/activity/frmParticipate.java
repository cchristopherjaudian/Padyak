package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.adapter.adapterParticipant;
import com.padyak.dto.Participants;
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

public class frmParticipate extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {
    TextView txEventTitle;
    RecyclerView rvParticipants;
    Button btnCloseEvent;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterParticipant adapterParticipant;
    ProgressDialog progressDialog;
    String eventId,eventName;
    GoogleMap gMap;
    Map<String, Object> payload;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference eventRef;
    Map<String,Marker> participantMarkers;
    Marker myMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmParticipate.this);
        setContentView(R.layout.activity_frm_participate);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapRides);
        mapFragment.getMapAsync(this);
        eventId = getIntent().getStringExtra("eventId");
        eventName = getIntent().getStringExtra("eventName");
        database = FirebaseDatabase.getInstance(getString(R.string.trackURL));
        myRef = database.getReference(LoggedUser.loggedUser.getUuid());
        eventRef = database.getReference();
        myRef.onDisconnect().removeValue();
        txEventTitle = findViewById(R.id.txEventTitle);
        rvParticipants = findViewById(R.id.rvParticipants);
        btnCloseEvent = findViewById(R.id.btnCloseEvent);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvParticipants.setLayoutManager(linearLayoutManager);

        txEventTitle.setText(eventName);
        btnCloseEvent.setOnClickListener(v -> finish());

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Object object = snapshot.getValue();
                String json = new Gson().toJson(object);
                try {
                    JSONObject jObject = new JSONObject(json);
                    Log.d("Firebase_Location", "onChildAdded: " + jObject);
                    String uuid = jObject.getString("id");
                    String name = jObject.getString("name");
                    double _lat = jObject.getDouble("latitude");
                    double _long = jObject.getDouble("longitude");
                    if(participantMarkers != null){
                        if(!participantMarkers.containsKey(uuid) && !uuid.equals(LoggedUser.getInstance().getUuid())){
                            Marker newMarker = gMap.addMarker(new MarkerOptions()
                                    .title(name)
                                    .position(new LatLng(_lat,_long)));
                            participantMarkers.put(uuid,newMarker);
                        } else {
                            Marker moveMarker = participantMarkers.get(uuid);
                            if(moveMarker == null){
                                moveMarker = gMap.addMarker(new MarkerOptions()
                                        .title(name)
                                        .position(new LatLng(_lat,_long)));
                            } else{
                                moveMarker.setPosition(new LatLng(_lat,_long));
                            }

                            participantMarkers.put(uuid,moveMarker);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Object object = snapshot.getValue();
                String json = new Gson().toJson(object);
                try {
                    JSONObject jObject = new JSONObject(json);
                    Log.d("Firebase_Location", "onChildChanged: " + jObject);
                    String uuid = jObject.getString("id");
                    if(!uuid.equals(LoggedUser.getLoggedUser().getUuid())){
                        double _lat = jObject.getDouble("latitude");
                        double _long = jObject.getDouble("longitude");
                        Marker moveMarker = participantMarkers.get(uuid);
                        Log.d("Firebase_Location", "moveMarker: " + moveMarker.getPosition().latitude);
                        moveMarker.setPosition(new LatLng(_lat,_long));
                        participantMarkers.put(uuid,moveMarker);
                        Log.d("Firebase_Location", "moveMarker: " + moveMarker.getPosition().latitude);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })   ;

    }

    public void loadMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable this application in Location permission using Android Settings", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                .setIntervalMillis(3000)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(4000)
                .build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    payload = new HashMap<>();
                    payload.put("id",LoggedUser.getInstance().getUuid());
                    payload.put("name", LoggedUser.getInstance().getFirstName().concat(" ").concat(LoggedUser.loggedUser.getLastName()));
                    payload.put("photoUrl", LoggedUser.getInstance().getImgUrl());
                    payload.put("latitude", location.getLatitude());
                    payload.put("longitude", location.getLongitude());
                    myRef.setValue(payload);

                    if(myMarker == null){
                        myMarker = gMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),location.getLongitude())));

                    } else{
                        myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                    }
                }

            }
        };
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double _lat = location.getLatitude();
                            double _long = location.getLongitude();
                            LatLng myPos = new LatLng(_lat, _long);
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15f));
                        }
                    }
                });
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
    public void loadParticipants() {


        progressDialog = Helper.getInstance().progressDialog(frmParticipate.this, "Retrieving participants.");
        progressDialog.show();


        new Thread(() -> {
            try {
                if (eventId.isEmpty()) throw new Exception("eventId is null");

                VolleyHttp volleyHttp = new VolleyHttp("/".concat(eventId), null, "event", frmParticipate.this);
                JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                if (responseJSON == null) throw new Exception("responseJSON is null");
                JSONObject eventObject = responseJSON.getJSONObject("data");
                JSONArray participantJSON = eventObject.optJSONArray("registeredUser");
                runOnUiThread(()->{

                });
                List<Participants> participantsList = new ArrayList<>();
                participantMarkers = new HashMap<>();
                for (int i = 0; i < participantJSON.length(); i++) {
                    JSONObject participantObject = participantJSON.getJSONObject(i).getJSONObject("user");
                    Participants participants = new Participants();
                    participants.setUserImage(participantObject.getString("photoUrl"));
                    participants.setUserName(participantObject.getString("firstname").concat(" ").concat(participantObject.getString("lastname")));
                    participantsList.add(participants);
                    participantMarkers.put(participantObject.getString("id"),null);
                }
                runOnUiThread(() -> {
                    adapterParticipant = new adapterParticipant(participantsList);
                    rvParticipants.setAdapter(adapterParticipant);
                });
            } catch (Exception err) {
                Log.d(Helper.getInstance().log_code, "loadParticipants: " + err.getMessage());
            } finally {
                runOnUiThread(() -> progressDialog.dismiss());
            }
        }).start();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        loadParticipants();
        loadMap();
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        database = null;
        myRef = null;
        eventRef = null;
    }
}