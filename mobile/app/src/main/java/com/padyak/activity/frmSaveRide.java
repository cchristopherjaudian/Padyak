package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.padyak.R;
import com.padyak.utility.GCPStorage;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class frmSaveRide extends AppCompatActivity implements OnMapsSdkInitializedCallback, OnMapReadyCallback {
    private static final int REQUEST_IMAGE_CAPTURE = 814;
    TextView txTimer, txDistanceValue;
    EditText etRideTitle, etRideCaption;
    Button btnBrowseImage, btnRideRegister, btnRideCancel;
    ImageView imgPreview;
    Double startPosLat, startPosLong, endPosLat, endPosLong;
    int rideDuration;
    Double rideDistance;
    boolean is_Capture = true;
    Bitmap bitmapRide;
    byte[] data;
    FirebaseStorage storage;
    StorageReference storageRef, rideRef;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, frmSaveRide.this);
        setContentView(R.layout.activity_frm_save_ride);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapRides);
        mapFragment.getMapAsync(this);

        rideDuration = getIntent().getIntExtra("rideDuration", 0);
        rideDistance = getIntent().getDoubleExtra("rideDistance", 0d);

        txTimer = findViewById(R.id.txTimer);
        txDistanceValue = findViewById(R.id.txDistanceValue);

        etRideTitle = findViewById(R.id.etRideTitle);
        etRideCaption = findViewById(R.id.etRideCaption);

        btnBrowseImage = findViewById(R.id.btnBrowseImage);
        btnRideRegister = findViewById(R.id.btnRideRegister);
        btnRideCancel = findViewById(R.id.btnRideCancel);

        imgPreview = findViewById(R.id.imgPreview);

        txTimer.setText(Helper.getInstance().formatDuration(rideDuration));
        txDistanceValue.setText(String.format("%.3f", rideDistance));

        btnRideCancel.setOnClickListener(v -> {
            frmRide.instance.finish();
            finish();
        });
        btnRideRegister.setOnClickListener(v -> {
            if (bitmapRide != null) {
                String ref = "rides/" + LoggedUser.getInstance().getUuid() + "/" + LocalDateTime.now().toString() + ".bmp";
                FirebaseApp.initializeApp(this);
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                rideRef = storageRef.child(ref);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapRide.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
                uploadTask = rideRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        saveRide("");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveRide(uri.toString());
                            }
                        });

                    }
                });
            } else {
                saveRide("n/a");
            }
        });
        btnBrowseImage.setOnClickListener(v -> {
            if (is_Capture) {
                is_Capture = false;

                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
            } else {
                is_Capture = true;
                btnBrowseImage.setText("Capture Image");
                imgPreview.setImageBitmap(null);
                imgPreview.getLayoutParams().height = 0;
            }


        });
    }

    private void saveRide(String imgURL) {
        try {
            if(imgURL.isEmpty()){
                Toast.makeText(this, "Failed to upload image. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }
            String fromLocationURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + startPosLat + "," + startPosLong + "&key=" + getString(R.string.maps_publicapi);
            String toLocationURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + endPosLat + "," + endPosLong + "&key=" + getString(R.string.maps_publicapi);

            VolleyHttp fromVolley = new VolleyHttp(fromLocationURL, null, "MAP", frmSaveRide.this);
            VolleyHttp toVolley = new VolleyHttp(toLocationURL, null, "MAP", frmSaveRide.this);

            String fromAddress = Helper.getInstance().generateAddress(fromVolley.getResponseBody(false));
            String toAddress = Helper.getInstance().generateAddress(toVolley.getResponseBody(false));

            Log.d(Helper.getInstance().log_code, "onCreate jsonFrom: " + fromAddress);
            Log.d(Helper.getInstance().log_code, "onCreate jsonTo: " + toAddress);

            Map<String, Object> payload = new HashMap<>();
            payload.put("post", etRideTitle.getText().toString());
            payload.put("distance", String.format("%.3f km", rideDistance));
            payload.put("movingTime", Helper.getInstance().formatDuration(rideDuration));
            payload.put("toLocation", toAddress);
            payload.put("fromLocation", fromAddress);
            payload.put("caption", etRideCaption.getText().toString());
            payload.put("photoUrl", imgURL);
            payload.put("fromLong", startPosLong);
            payload.put("toLong", endPosLong);
            payload.put("fromLat", startPosLat);
            payload.put("toLat", endPosLat);

            VolleyHttp volleyHttp = new VolleyHttp("", payload, "post", frmSaveRide.this);
            String response = volleyHttp.getResponseBody(true);


            JSONObject reader = new JSONObject(response);
            int responseStatus = reader.getInt("status");
            if (responseStatus == 200) {
                Toast.makeText(frmSaveRide.this, "Ride details successfully posted.", Toast.LENGTH_SHORT).show();
                frmRide.instance.finish();
                finish();
            } else {
                Toast.makeText(frmSaveRide.this, "Failed to submit ride details. Please try again", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(Helper.getInstance().log_code, "saveRide JSONException: " + e.getMessage());
        } catch (Exception ee){
            Log.d(Helper.getInstance().log_code, "saveRide Exception: " + ee.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        startPosLat = getIntent().getDoubleExtra("startPosLat", 0.0);
        startPosLong = getIntent().getDoubleExtra("startPosLong", 0.0);

        endPosLat = getIntent().getDoubleExtra("endPosLat", 0.0);
        endPosLong = getIntent().getDoubleExtra("endPosLong", 0.0);

        LatLng startPos = new LatLng(startPosLat, startPosLong);
        LatLng endPos = new LatLng(endPosLat, endPosLong);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endPos, 14.0f));
        Marker markerStart = googleMap.addMarker(new MarkerOptions()
                .position(startPos));
        Marker markerEnd = googleMap.addMarker(new MarkerOptions()
                .position(endPos));
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            switch (resultCode) {
                case RESULT_OK:
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        bitmapRide = bitmap;
                        imgPreview.setImageBitmap(bitmap);
                        imgPreview.getLayoutParams().height = 700;
                        btnBrowseImage.setText("Remove Image");
                    }
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }
}