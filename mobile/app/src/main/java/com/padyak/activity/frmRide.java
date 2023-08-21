package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.padyak.R;
import com.padyak.utility.Helper;

import java.time.Duration;
import java.time.Instant;

public class frmRide extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {
    boolean startTracking = false;
    Helper helper = Helper.getInstance();
    Instant timeStart,timeEnd;
    Duration instantDuration;
    String instantFormat;
    TextView txTimer,txDistance;
    LocationManager mLocationManager;
    ImageButton btnPlayRide;

    GoogleMap gMap;
    Marker marker;
    double startPosLat, startPosLong;
    int instantInterval;
    double rideDistance = 0d;
    double endPosLat, endPosLong;
    LatLng previousLocation,newLocation;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationClient;
    public static frmRide instance;

    ProgressDialog progressDialog;
    int tempCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmRide.this);
        setContentView(R.layout.activity_frm_ride);
        instance = this;

        btnPlayRide = findViewById(R.id.btnPlayRide);
        txTimer = findViewById(R.id.txTimer);
        txDistance = findViewById(R.id.txDistance);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapRides);
        mapFragment.getMapAsync(this);

        btnPlayRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startTracking){
                    btnPlayRide.setImageResource(R.drawable.baseline_stop_24);
                    timeStart = Instant.now();
                    startTracking = true;
                    getLocation(gMap);
                    if(marker != null){
                        startPosLat = marker.getPosition().latitude;
                        startPosLong = marker.getPosition().longitude;
                    }
                } else{
                    btnPlayRide.setImageResource(R.drawable.baseline_play_arrow_24);
                    startTracking = false;
                    endPosLat = marker.getPosition().latitude;
                    endPosLong = marker.getPosition().longitude;

                    Bundle b = new Bundle();
                    b.putInt("rideDuration", (int)(Duration.ofSeconds(instantInterval).toMillis() / 1000));
                    b.putDouble("rideDistance",rideDistance);
                    b.putDouble("startPosLat",startPosLat);
                    b.putDouble("startPosLong",startPosLong);
                    b.putDouble("endPosLat",endPosLat);
                    b.putDouble("endPosLong",endPosLong);
                    Intent intent = new Intent(frmRide.this, frmSaveRide.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
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

        } else{

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
                    if(startTracking){
                        for (Location location : locationResult.getLocations()) {
                            if(marker == null ){
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(previousLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle)));
                            }

                                previousLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                                newLocation = new LatLng(location.getLatitude(),location.getLongitude());

                                rideDistance += helper.calculateDistance(previousLocation,newLocation);

                                txDistance.setText(String.format("%.3f",rideDistance));
                                ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
                                final float[] previousStep = {0f};
                                double deltaLatitude = newLocation.latitude - previousLocation.latitude;
                                double deltaLongitude = newLocation.longitude - previousLocation.longitude;
                                animation.setDuration(2000);
                                animation.addUpdateListener(animation1 -> {
                                    float deltaStep = (Float) animation1.getAnimatedValue() - previousStep[0];
                                    previousStep[0] = (Float) animation1.getAnimatedValue();
                                    marker.setPosition(new LatLng(marker.getPosition().latitude + deltaLatitude * deltaStep * 1 / 100, marker.getPosition().longitude + deltaStep * deltaLongitude * 1 / 100));
                                });
                                animation.start();
                                tempCounter++;
                                if(tempCounter >=10){
                                    tempCounter = 0;
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, gMap.getCameraPosition().zoom));
                                }

                    }

                }
            }};
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
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15.0f));
                            } else{
                                Toast.makeText(frmRide.this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

            if(startTracking){
                fusedLocationClient.requestLocationUpdates(locationRequest,
                        locationCallback,
                        Looper.getMainLooper());
                new Thread(()->{
                    while(startTracking){
                        timeEnd = Instant.now();
                        instantInterval = (int)(Duration.between(timeStart, timeEnd).toMillis()/ 1000.0);
                        instantDuration = Duration.ofSeconds(instantInterval);

                        instantFormat = helper.formatDuration(instantInterval);
                        runOnUiThread(()->{
                            txTimer.setText(instantFormat);
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                        }
                    }

                }).start();
            }

        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        progressDialog = Helper.getInstance().progressDialog(frmRide.this, "Retrieving current location.");
        progressDialog.show();
        getLocation(gMap);
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }


}