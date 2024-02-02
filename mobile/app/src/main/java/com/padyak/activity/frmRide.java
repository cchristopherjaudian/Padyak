package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.service.TrackingService;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class frmRide extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {
    boolean startTracking = false;
    CardView cardView8;
    Helper helper = Helper.getInstance();
    Instant timeStart, timeEnd;
    Duration instantDuration;
    String instantFormat;
    TextView txTimer, txDistance;
    LocationManager mLocationManager;
    ImageButton btnPlayRide, btnStopRide, btnPauseRide;
    ImageButton imgSOS;
    GoogleMap gMap;
    Marker marker;
    Map<String, Marker> participantMarkers;
    double startPosLat, startPosLong;
    int instantInterval = 0;
    int previousInterval = 0;
    double rideDistance = 0d;
    double endPosLat, endPosLong;
    LatLng previousLocation, newLocation;
    Map<String, Object> payload;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationClient;
    public static frmRide instance;
    String eventId = "";
    ProgressDialog progressDialog;
    int tempCounter = 0;
    boolean isMinimized;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference eventRef;

    boolean isPaused = false;
    String notifDistance = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmRide.this);
        setContentView(R.layout.activity_frm_ride);
        if(getIntent().hasExtra("eventId")){
            eventId = getIntent().getStringExtra("eventId");
        }
        Log.d(Helper.getInstance().log_code, "onCreate eventInfo: " + eventId);
        instance = this;
        participantMarkers = new HashMap<>();
        cardView8 = findViewById(R.id.cardView8);
        btnPauseRide = findViewById(R.id.btnPauseRide);
        btnPlayRide = findViewById(R.id.btnPlayRide);
        btnStopRide = findViewById(R.id.btnStopRide);
        imgSOS = findViewById(R.id.imgSOS);
        txTimer = findViewById(R.id.txTimer);
        txDistance = findViewById(R.id.txDistance);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapRides);
        mapFragment.getMapAsync(this);
        imgSOS.setOnClickListener(v->{
            Intent intent = new Intent(frmRide.this, TriggerActivity.class);
            startActivity(intent);
        });
        btnPlayRide.setOnClickListener(v -> {

            cardView8.setVisibility(View.VISIBLE);
            btnPlayRide.setVisibility(View.INVISIBLE);
            btnStopRide.setVisibility(View.VISIBLE);
            //timeStart = Instant.now();
            startTracking = true;
            if(!isPaused){
                getLocation(gMap);
            }

            if (marker != null) {
                startPosLat = marker.getPosition().latitude;
                startPosLong = marker.getPosition().longitude;
            }

            new Thread(() -> {
                while (startTracking) {
                    if (!isPaused) {
                        if(timeStart == null) timeStart = Instant.now();
                        timeEnd = Instant.now();
                        instantInterval = previousInterval + ((int) (Duration.between(timeStart, timeEnd).toMillis() / 1000.0));
                        instantDuration = Duration.ofSeconds(instantInterval);

                        instantFormat = helper.formatDuration(instantInterval);
                        runOnUiThread(() -> {
                            txTimer.setText(instantFormat);
                        });
                    } else {
                        isPaused = false;
                        timeStart = Instant.now();
                    }
                    try {
                        Log.d("Padyak_Time", "onCreate: timeStart " + timeStart);
                        Log.d("Padyak_Time", "onCreate: timeEnd " + timeEnd);
                        if(isMinimized){
                            Intent intent = new Intent(frmRide.this, TrackingService.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("distance",notifDistance.concat("km"));
                            bundle.putString("time",instantFormat);
                            intent.putExtras(bundle);
                            startService(intent);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }).start();

        });
        btnStopRide.setOnClickListener(v -> {
            isMinimized = false;
            isPaused = false;
            cardView8.setVisibility(View.INVISIBLE);
            btnPlayRide.setVisibility(View.VISIBLE);
            btnStopRide.setVisibility(View.INVISIBLE);
            startTracking = false;
            endPosLat = marker.getPosition().latitude;
            endPosLong = marker.getPosition().longitude;
            if(database != null) database.goOffline();

            myRef = null;
            database = null;
            eventRef = null;
            Bundle b = new Bundle();
            b.putInt("rideDuration", (int) (Duration.ofSeconds(instantInterval).toMillis() / 1000));
            b.putDouble("rideDistance", rideDistance);
            b.putDouble("startPosLat", startPosLat);
            b.putDouble("startPosLong", startPosLong);
            b.putDouble("endPosLat", endPosLat);
            b.putDouble("endPosLong", endPosLong);
            Intent intent = new Intent(frmRide.this, frmSaveRide.class);
            intent.putExtras(b);
            startActivity(intent);
        });
        btnPauseRide.setOnClickListener(v -> {
            isPaused = true;
            startTracking = false;
            previousInterval = instantInterval;
            cardView8.setVisibility(View.INVISIBLE);
            btnPlayRide.setVisibility(View.VISIBLE);
            btnStopRide.setVisibility(View.INVISIBLE);

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(gMap);
            }
        }
    }

    public void getLocation(GoogleMap googleMap) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            progressDialog.dismiss();
            ActivityCompat.requestPermissions(frmRide.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            if(!eventId.isEmpty()){
                database = FirebaseDatabase.getInstance(getString(R.string.trackURL));
                eventRef = database.getReference(eventId);
                myRef = database.getReference(eventId.concat("/").concat(LoggedUser.loggedUser.getUuid()));
                myRef.onDisconnect().removeValue();
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
                            if (!participantMarkers.containsKey(uuid) && !uuid.equals(LoggedUser.getInstance().getUuid())) {
                                Marker newMarker = googleMap.addMarker(new MarkerOptions()
                                        .title(name)
                                        .position(new LatLng(_lat, _long)));
                                participantMarkers.put(uuid, newMarker);
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
                            if (!uuid.equals(LoggedUser.getLoggedUser().getUuid())) {
                                double _lat = jObject.getDouble("latitude");
                                double _long = jObject.getDouble("longitude");
                                Marker moveMarker = participantMarkers.get(uuid);
                                Log.d("Firebase_Location", "moveMarker: " + moveMarker.getPosition().latitude);
                                moveMarker.setPosition(new LatLng(_lat, _long));
                                participantMarkers.put(uuid, moveMarker);
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
                });
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
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
                    if (startTracking) {
                        for (Location location : locationResult.getLocations()) {
                            if (marker == null) {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .title("Me")
                                        .position(previousLocation));
                            }
                            previousLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                            newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            rideDistance += helper.calculateDistance(previousLocation, newLocation);
                            notifDistance = String.format("%.4f", rideDistance);
                            txDistance.setText(notifDistance);

                            if(!eventId.isEmpty()){
                                new Thread(() -> {
                                    payload = new HashMap<>();
                                    payload.put("id", LoggedUser.getInstance().getUuid());
                                    payload.put("name", LoggedUser.getInstance().getFirstName().concat(" ").concat(LoggedUser.loggedUser.getLastName()));
                                    payload.put("photoUrl", LoggedUser.getInstance().getImgUrl());
                                    payload.put("latitude", newLocation.latitude);
                                    payload.put("longitude", newLocation.longitude);
                                    myRef.setValue(payload);
                                    Log.d(Helper.getInstance().log_code, "onLocationResult: Payload sent " + payload);
                                }).start();
                            }


                            Log.d(Helper.getInstance().log_code, "onLocationResult: previousLocation " + previousLocation.latitude + " @ " + previousLocation.longitude);
                            Log.d(Helper.getInstance().log_code, "onLocationResult: newLocation " + newLocation.latitude + " @ " + newLocation.longitude);
                            Log.d(Helper.getInstance().log_code, "onLocationResult: rideDistance " + rideDistance);

                            ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
                            final float[] previousStep = {0f};
                            double deltaLatitude = newLocation.latitude - previousLocation.latitude;
                            double deltaLongitude = newLocation.longitude - previousLocation.longitude;
                            animation.setDuration(2000);
                            animation.addUpdateListener(animation1 -> {
                                float deltaStep = (Float) animation1.getAnimatedValue() - previousStep[0];
                                previousStep[0] = (Float) animation1.getAnimatedValue();
                                marker.setPosition(new LatLng(marker.getPosition().latitude + deltaLatitude * deltaStep * 1 / 100, marker.getPosition().longitude + deltaStep * deltaLongitude * 1 / 100));
                                if(!marker.isInfoWindowShown()) marker.showInfoWindow();
                            });
                            animation.start();
                            tempCounter++;
                            if (tempCounter >= 10) {
                                tempCounter = 0;
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16.0f));
                            }

                        }

                    }
                }
            };
            progressDialog.show();
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            progressDialog.dismiss();
                            if (location != null) {
                                startPosLat = location.getLatitude();
                                startPosLong = location.getLongitude();
                                LatLng myPos = new LatLng(startPosLat, startPosLong);
                                previousLocation = myPos;
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 16.0f));
                            } else {
                                Toast.makeText(frmRide.this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });

            if (startTracking) {
                fusedLocationClient.requestLocationUpdates(locationRequest,
                        locationCallback,
                        Looper.getMainLooper());

            }

        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        progressDialog = Helper.getInstance().progressDialog(frmRide.this, "Retrieving current location.");
        progressDialog.show();
        getLocation(gMap);
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }

    @Override
    public void onBackPressed() {
        
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.d(Helper.getInstance().log_code, "onUserLeaveHint: Minimized");
        isMinimized = true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        isMinimized = false;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(getString(R.string.foreground_notif_channel)));
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