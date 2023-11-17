package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
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
import com.padyak.dto.Location;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class frmFindLocation extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {
    FusedLocationProviderClient fusedLocationClient;
    TextView txNearestCategory, txNearestAddress, txFindNearestTitle, txFindTitle,txNearestDistance,txNearestTime;
    Button btnFindDirection;
    String findCategory, findCode;
    LatLng myPos,nearestLatLng;
    GoogleMap gMap;
    ImageView imgNearestImage;
    Location nearestLocation = null;
    double lowestDistance = 0d;
    double computed = 0d;
    String distanceTime = "";
    double distanceTimeRaw = 0d;
    String lowestDistanceTime = "";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmFindLocation.this);
        setContentView(R.layout.activity_frm_find_location);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFind);
        mapFragment.getMapAsync(this);

        btnFindDirection = findViewById(R.id.btnFindDirection);
        txNearestCategory = findViewById(R.id.txNearestCategory);
        txNearestDistance = findViewById(R.id.txNearestDistance);
        txNearestAddress = findViewById(R.id.txNearestAddress);
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
        txFindNearestTitle.setText("Nearest " + findCategory);
        txFindTitle.setText("Find " + findCategory);

        btnFindDirection.setOnClickListener(v->{
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
        progressDialog = Helper.getInstance().progressDialog(frmFindLocation.this,"Searching for nearest " + findCategory + ".");
        progressDialog.show();
        new Thread(()->{
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            if (location != null) {
                                double _lat = location.getLatitude();
                                double _long = location.getLongitude();
                                myPos = new LatLng(_lat, _long);

                                List<Location> registeredLocations = new ArrayList<>();
                                VolleyHttp volleyHttp = new VolleyHttp("",null,"location",frmFindLocation.this);
                                JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                                JSONArray dataArray = responseJSON.optJSONArray("data");
                                for(int i = 0;i < dataArray.length();i++){
                                    try {
                                        JSONObject locationObject = dataArray.getJSONObject(i);
                                        if(locationObject.get("type").equals(findCode)){
                                            Location locationClass = new Location();
                                            locationClass.setId(locationObject.getString("id"));
                                            locationClass.setType(locationObject.getString("type"));
                                            locationClass.setLatitude(locationObject.getDouble("latitude"));
                                            locationClass.setLongitude(locationObject.getDouble("longitude"));
                                            locationClass.setName(locationObject.getString("name"));
                                            locationClass.setPhotoUrl(locationObject.getString("photoUrl"));
                                            registeredLocations.add(locationClass);
                                        }
                                    } catch (JSONException e) {
                                        Log.d(Helper.getInstance().log_code, "loadNearest: " + e.getMessage());
                                    }
                                }
                                registeredLocations.forEach(l->{

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
                                        distanceTimeRaw = duration.getInt("value")/60/60;
                                        distanceTime = "<" + String.format("%.1f",distanceTimeRaw) + " hr(s)";
                                    }catch (JSONException e) {
                                        Log.d(Helper.getInstance().log_code, "onSuccess: " + e.getMessage());
                                    }


                                    //computed = Helper.getInstance().calculateDistance(myPos, new LatLng(l.getLatitude(),l.getLongitude()));

                                    if(lowestDistance == 0d){
                                        lowestDistance = computed;
                                        nearestLocation = l;
                                        lowestDistanceTime = distanceTime;
                                    } else{
                                        if(computed < lowestDistance){
                                            lowestDistance = computed;
                                            nearestLocation = l;
                                            lowestDistanceTime = distanceTime;
                                        }
                                    }
                                });
                                runOnUiThread(()->{
                                    progressDialog.dismiss();
                                    txNearestAddress.setText(nearestLocation.getName());
                                    txNearestDistance.setText(String.format("%.2f",lowestDistance) + " km");
                                    txNearestTime.setText(lowestDistanceTime);
                                    nearestLatLng = new LatLng(nearestLocation.getLatitude(),nearestLocation.getLongitude());
                                    Picasso.get().load(nearestLocation.getPhotoUrl()).into(imgNearestImage);
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestLatLng, 12f));

                                    Marker markerStart = gMap.addMarker(new MarkerOptions()
                                            .position(myPos).title("Current Location"));
                                    Marker markerDestination = gMap.addMarker(new MarkerOptions()
                                            .position(nearestLatLng)
                                            .title("Destination")
                                            .snippet(nearestLocation.getName()));
                                    markerDestination.showInfoWindow();
                                });

                            } else{
                                runOnUiThread(()->{
                                    progressDialog.dismiss();
                                    Toast.makeText(frmFindLocation.this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_LONG).show();
                                    finish();
                                });

                            }
                        }
                    });
        }).start();


    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        loadNearest();
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }
}