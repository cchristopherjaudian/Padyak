package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ValidationMenuActivity extends AppCompatActivity {
    Button btnValidatePayment,btnUploadQR;
    Bitmap bitmapPayment;

    FirebaseStorage storage;
    StorageReference storageRef, eventRef;
    UploadTask uploadTask;
    ProgressDialog progressDialog;
    boolean data_inserted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_menu);

        btnValidatePayment = findViewById(R.id.btnValidatePayment);
        btnUploadQR = findViewById(R.id.btnUploadQR);

        btnValidatePayment.setOnClickListener(e->{
            Intent intent = new Intent(ValidationMenuActivity.this,EventPaymentActivity.class);
            startActivity(intent);
        });

        btnUploadQR.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 814);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 814 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                progressDialog = Helper.getInstance().progressDialog(ValidationMenuActivity.this, "Uploading QR to storage.");
                progressDialog.show();
                bitmapPayment = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                String ref = "qr/GCash.jpg";
                FirebaseApp.initializeApp(this);
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                eventRef = storageRef.child(ref);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapPayment.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataIntent = baos.toByteArray();
                uploadTask = eventRef.putBytes(dataIntent);
                progressDialog.dismiss();
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ValidationMenuActivity.this, "Failed to upload QR. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadQR(uri.toString());
                                Toast.makeText(ValidationMenuActivity.this, "Payment QR uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //txProofPayment.setText("Image Attached Successfully");
            } catch (IOException e) {
                bitmapPayment = null;
                Toast.makeText(this, "Failed to retrieve image. Please try again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void uploadQR(String imgURI){
        progressDialog = Helper.getInstance().progressDialog(ValidationMenuActivity.this, "Retrieving QR link.");
        progressDialog.show();
        data_inserted = false;
        Map<String, Object> payload = new HashMap<>();
        payload.put("url", imgURI);

        new Thread(()->{
            VolleyHttp volleyHttp = new VolleyHttp("/storage", payload, "admin", ValidationMenuActivity.this);
            String response = volleyHttp.getResponseBody(true);

            try {
                JSONObject responseJSON = new JSONObject(response);
                int responseCode = responseJSON.getInt("status");
                if (responseCode == 200) data_inserted = true;
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onCreate: JSON " + e.getMessage());
            }
            runOnUiThread(() -> {
                progressDialog.dismiss();
                if (data_inserted) {
                    Toast.makeText(this, "Payment QR uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to upload QR. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

    }
}