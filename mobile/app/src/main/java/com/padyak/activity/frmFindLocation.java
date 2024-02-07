package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.padyak.R;
import com.padyak.dto.FindLocation;
import com.padyak.dto.Location;
import com.padyak.utility.CyclistHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class frmFindLocation extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {
    FusedLocationProviderClient fusedLocationClient;
    TextView txNearestCategory, txNearestContact, txFindNearestTitle, txFindTitle, txNearestDistance, txNearestTime, txNearestRating;
    Button btnFindDirection;
    String findCategory, findCode;
    LatLng myPos, nearestLatLng;
    GoogleMap gMap;
    ImageView imgNearestImage;
    Location nearestLocation = null;
    double lowestDistance = 0d;
    double computed = 0d;
    String distanceTime = "";
    double distanceTimeRaw = 0d;
    String lowestDistanceTime = "";
    String ratings;
    String nearestUuid = "";
    ProgressDialog progressDialog;
    List<FindLocation> findLocationList = new ArrayList<>();
    ImageView imgRating1, imgRating2, imgRating3, imgRating4, imgRating5;
    List<ImageView> ratingList;
    List<Marker> markersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmFindLocation.this);
        setContentView(R.layout.activity_frm_find_location);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFind);
        mapFragment.getMapAsync(this);
        markersList = new ArrayList<>();
        imgRating1 = findViewById(R.id.imgRating1);
        imgRating2 = findViewById(R.id.imgRating2);
        imgRating3 = findViewById(R.id.imgRating3);
        imgRating4 = findViewById(R.id.imgRating4);
        imgRating5 = findViewById(R.id.imgRating5);

        ratingList = new ArrayList<>();
        ratingList.add(imgRating1);
        ratingList.add(imgRating2);
        ratingList.add(imgRating3);
        ratingList.add(imgRating4);
        ratingList.add(imgRating5);

        btnFindDirection = findViewById(R.id.btnFindDirection);
        txNearestContact = findViewById(R.id.txNearestContact);
        txNearestCategory = findViewById(R.id.txNearestCategory);
        txNearestDistance = findViewById(R.id.txNearestDistance);
        txNearestRating = findViewById(R.id.txNearestRating);
        txFindNearestTitle = findViewById(R.id.txFindNearestTitle);
        txNearestTime = findViewById(R.id.txNearestTime);
        txFindTitle = findViewById(R.id.txFindTitle);
        imgNearestImage = findViewById(R.id.imgNearestImage);
        Bundle bundle = getIntent().getExtras();
        findCode = bundle.getString("find", "");
        switch (findCode) {
            case "REPAIR_SHOP":
                findCategory = "Repair Shop";
                break;
            case "POLICE_STATION":
                findCategory = "Police Station";
                break;
            case "HOSPITAL":
                findCategory = "Hospital";
                break;
        }
        txNearestCategory.setText(findCategory);
        txFindTitle.setText("Find " + findCategory);

        btnFindDirection.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" + nearestLatLng.latitude + "," + nearestLatLng.longitude + "&dirflg=w");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void loadNearest() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable this application in Location permission using Android Settings", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        progressDialog = Helper.getInstance().progressDialog(frmFindLocation.this, "Searching for nearby " + findCategory + "s.");
        progressDialog.show();
        new Thread(() -> {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double _lat = location.getLatitude();
                            double _long = location.getLongitude();
                            myPos = new LatLng(_lat, _long);
                            findLocationList = new ArrayList<>();
                            List<Location> registeredLocations = new ArrayList<>();
                            VolleyHttp volleyHttp = new VolleyHttp("", null, "location", frmFindLocation.this);
                            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                            JSONArray dataArray = responseJSON.optJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                try {
                                    JSONObject locationObject = dataArray.getJSONObject(i);
                                    if (locationObject.get("type").equals(findCode)) {
                                        Location locationClass = new Location();
                                        locationClass.setId(locationObject.getString("id"));
                                        locationClass.setType(locationObject.getString("type"));
                                        locationClass.setLatitude(locationObject.getDouble("latitude"));
                                        locationClass.setLongitude(locationObject.getDouble("longitude"));
                                        locationClass.setName(locationObject.getString("name"));
                                        locationClass.setPhotoUrl(locationObject.getString("photoUrl"));
                                        locationClass.setRating(locationObject.getString("ratings"));
                                        locationClass.setContact(locationObject.getString("contact"));
                                        registeredLocations.add(locationClass);
                                    }
                                } catch (JSONException e) {
                                    Log.d(Helper.getInstance().log_code, "loadNearest: " + e.getMessage());
                                }
                            }
                            registeredLocations.forEach(l -> {

                                String fromLocationURL = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=" + l.getLatitude() + "%2C" + l.getLongitude() + "&origins=" + myPos.latitude + "%2C" + myPos.longitude + "&mode=walking&key=" + getString(R.string.maps_publicapi);
                                VolleyHttp fromVolley = new VolleyHttp(fromLocationURL, null, "MAP", frmFindLocation.this);
                                JSONObject distanceObject = fromVolley.getJsonResponse(false);
                                computed = 0d;

                                try {
                                    JSONObject distance = distanceObject.getJSONArray("rows")
                                            .getJSONObject(0)
                                            .getJSONArray("elements")
                                            .getJSONObject(0)
                                            .getJSONObject("distance");
                                    JSONObject duration = distanceObject.getJSONArray("rows")
                                            .getJSONObject(0)
                                            .getJSONArray("elements")
                                            .getJSONObject(0)
                                            .getJSONObject("duration");

                                    computed = distance.getDouble("value") / 1000;
                                    distanceTimeRaw = duration.getInt("value") / 60; // / 60;
                                    distanceTime = "<" + String.format("%.1f", distanceTimeRaw) + " min(s)";
                                    if (computed <= 15d) {
                                        findLocationList.add(
                                                new FindLocation(l.getLatitude(), l.getLongitude(), l.getName(), l.getPhotoUrl(), distanceTime, l.getId(), computed, l.getRating(), l.getContact())
                                        );
                                    }

                                    if (lowestDistance == 0d) {
                                        lowestDistance = computed;
                                        nearestLocation = l;
                                        lowestDistanceTime = distanceTime;
                                    } else {
                                        if (computed < lowestDistance) {
                                            lowestDistance = computed;
                                            nearestLocation = l;
                                            lowestDistanceTime = distanceTime;
                                        }
                                    }
                                } catch (JSONException e) {
                                    Log.d(Helper.getInstance().log_code, "onSuccess: " + e.getMessage());
                                }
                            });
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                markersList = new ArrayList<>();
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15f));
                                if (findLocationList.size() == 0) {
                                    Toast.makeText(this, "No nearby " + findCategory + " found", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {

                                    findLocationList.forEach(find -> {
                                        LatLng findLatLng = new LatLng(find.getLatitude(), find.getLongitude());
                                        Marker m = gMap.addMarker(new MarkerOptions()
                                                .position(findLatLng)
                                                .title(find.getLocationName())
                                                .snippet(find.getId())
                                        );
                                        markersList.add(m);
                                        if (find.getId().equals(nearestLocation.getId())) {
                                            loadNearestInfo(find, String.valueOf(lowestDistance), lowestDistanceTime);
                                        }
                                    });
                                    gMap.addMarker(new MarkerOptions()
                                            .position(myPos)
                                            .title("Me")
                                    );
                                    Optional<Marker> m = markersList.stream().filter(marker -> marker.getSnippet().equals(nearestLocation.getId())).findFirst();
                                    m.ifPresent(Marker::showInfoWindow);
                                }
                            });
                        } else {
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(frmFindLocation.this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_LONG).show();
                                finish();
                            });

                        }
                    });
        }).start();


    }

    public void loadNearestInfo(FindLocation location, String distance, String travelTime) {
        for (int i = 0; i < ratingList.size(); i++) {
            ratingList.get(i).setVisibility(View.INVISIBLE);
        }
        double dratings = Double.parseDouble(location.getRating());
        int ratings = (int)dratings;
        LatLng findLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        Picasso.get().load(location.getPhotoUrl()).into(imgNearestImage);
        txNearestDistance.setText(distance.concat("km"));
        txNearestTime.setText(travelTime);
        txFindNearestTitle.setText(location.getLocationName());
        txNearestRating.setText(String.valueOf(dratings));
        txNearestContact.setText(location.getContact());
        nearestLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        for (int i = 0; i < ratings; i++) {
            ratingList.get(i).setVisibility(View.VISIBLE);
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(findLatLng, 15f));
        Optional<Marker> m = markersList.stream().filter(marker -> marker.getSnippet().equals(location.getId())).findFirst();
        m.ifPresent(Marker::showInfoWindow);

        btnFindDirection.setEnabled(true);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        loadNearest();

        gMap.setOnMarkerClickListener(marker -> {
            if (marker.getSnippet() != null) {
                String markerId = marker.getSnippet();
                Optional<FindLocation> findLocationOptional = findLocationList.stream()
                        .filter(findLocation -> findLocation.getId().equals(markerId))
                        .findFirst();
                if (findLocationOptional.isPresent()) {
                    FindLocation findLocation = findLocationOptional.get();

                    String locationDistance = String.valueOf(findLocation.getDistance());
                    String locationTime = findLocation.getTravelTime();
                    loadNearestInfo(findLocation, locationDistance, locationTime);
                }
            } else{
                btnFindDirection.setEnabled(false);
            }
            return false;
        });
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

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