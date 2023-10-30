package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.padyak.R;
import com.padyak.adapter.adapterCoverPhoto;
import com.padyak.dto.CoverPhoto;
import com.padyak.fragment.fragmentUserUploaded;
import com.padyak.fragment.fragmentViewQR;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class frmEventRegister extends AppCompatActivity {
    String[] gender = {"-Please select a payment method-", "GCash", "Over the Counter"};
    ArrayAdapter<String> aaPayment;
    Spinner spinnerPayment;
    Button btnEventRegister, btnEventCancel, btnRegisterPayment;
    TextView txEventName, txProofPayment, txViewQR;
    ImageView imgEventRegister;
    CheckBox checkBox;
    Bitmap bitmapPayment;
    byte[] data;
    FirebaseStorage storage;
    StorageReference storageRef, eventRef;
    UploadTask uploadTask;
    String eventId, eventName, eventImg;
    String paymentType;
    ProgressDialog progressDialog;
    String paymentQR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_register);
        spinnerPayment = findViewById(R.id.spinnerPayment);
        btnRegisterPayment = findViewById(R.id.btnRegisterPayment);
        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);
        checkBox = findViewById(R.id.checkBox);

        txProofPayment = findViewById(R.id.txProofPayment);
        txEventName = findViewById(R.id.txEventName);
        txViewQR = findViewById(R.id.txViewQR);
        imgEventRegister = findViewById(R.id.imgEventRegister);

        eventId = getIntent().getStringExtra("id");
        eventName = getIntent().getStringExtra("name");
        eventImg = getIntent().getStringExtra("photoUrl");

        aaPayment = new ArrayAdapter<String>(frmEventRegister.this, R.layout.sp_format, gender);
        aaPayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(aaPayment);

        txEventName.setText(eventName);
        Picasso.get().load(eventImg).into(imgEventRegister);

        btnEventCancel.setOnClickListener(v -> finish());
        txViewQR.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            fragmentViewQR editNameDialogFragment = fragmentViewQR.newInstance("PaymentQR",paymentQR);
            editNameDialogFragment.setCancelable(false);
            editNameDialogFragment.show(fm, "PaymentQR");
        });
        btnRegisterPayment.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 814);
        });
        btnEventRegister.setOnClickListener(v -> {
            if (!checkBox.isChecked()) {
                Toast.makeText(this, "Please tick the waiver agreement to proceed.", Toast.LENGTH_LONG).show();
                return;
            }
            if (bitmapPayment == null) {
                Toast.makeText(this, "Please attach a screenshot of payment proof to proceed.", Toast.LENGTH_LONG).show();
                return;
            }
            if (spinnerPayment.getSelectedItemPosition() < 1) {
                Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_LONG).show();
                return;
            }

            paymentType = spinnerPayment.getSelectedItemPosition() == 1 ? "GCASH" : "OTC";
            AlertDialog alertDialog = new AlertDialog.Builder(frmEventRegister.this).create();
            alertDialog.setTitle("Event Registration");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to register in this event?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    (d, w) -> {
                        processRegistration();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.show();

        });
        loadQR();
    }

    public void loadQR() {
        progressDialog = Helper.getInstance().progressDialog(frmEventRegister.this, "Retrieving payment info.");
        progressDialog.show();
        new Thread(() -> {
            VolleyHttp volleyHttp = new VolleyHttp("/storage/LATEST", null, "admin", frmEventRegister.this);
            String response = volleyHttp.getResponseBody(true);
            runOnUiThread(() -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int responseCode = jsonResponse.getInt("status");
                    if (responseCode != 200) throw new Exception("Response Code " + responseCode);
                    JSONObject urlObject = jsonResponse.getJSONObject("data");
                    paymentQR = urlObject.getString("url");
                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "loadCoverPhoto JSONException: " + e.getMessage());
                } catch (Exception ee) {
                    Log.d(Helper.getInstance().log_code, "loadCoverPhoto Exception: " + ee.getMessage());
                } finally {
                    progressDialog.dismiss();
                }
            });
        }).start();
    }

    public void processRegistration() {
        progressDialog = Helper.getInstance().progressDialog(frmEventRegister.this, "Processing registration.");
        progressDialog.show();
        new Thread(() -> {
            String ref = "payment/" + LoggedUser.getInstance().getUuid() + "/" + LocalDateTime.now().toString() + ".jpg";
            FirebaseApp.initializeApp(this);
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            eventRef = storageRef.child(ref);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapPayment.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
            uploadTask = eventRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    savePayment("");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            savePayment(uri.toString());
                        }
                    });

                }
            });
        }).start();
    }

    private void savePayment(String imgURI) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("paymentUrl", imgURI);
        payload.put("paymentType", paymentType);

        VolleyHttp volleyHttp = new VolleyHttp("/cyclist/".concat(eventId), payload, "event-patch", frmEventRegister.this);
        JSONObject responseJSON = volleyHttp.getJsonResponse(true);
        runOnUiThread(() -> {
            progressDialog.dismiss();
            if (responseJSON == null) {
                Toast.makeText(this, "Failed to process request. Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(frmEventRegister.this).create();
                alertDialog.setTitle("Event Registration");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("You have successfully registered in this event. Press OK to continue");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (d, w) -> {
                            if (null != frmEventInfo.frmEventInfo)
                                frmEventInfo.frmEventInfo.finish();
                            finish();
                        });
                alertDialog.show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 814 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                bitmapPayment = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                txProofPayment.setText("Image Attached Successfully");
            } catch (IOException e) {
                bitmapPayment = null;
                Toast.makeText(this, "Failed to retrieve image. Please try again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {

    }
}