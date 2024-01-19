package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    ImageView imgAdminProfile;
    ImageView imgShowPassword;
    TextView txSave, txCreatePassword;
    TextView txEmergencyCount;
    ConstraintLayout constraintLayout4;
    ArrayAdapter<String> aaGender,aaHeightUnit;
    EditText etCreatePassword, etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateBirthdate, etCreateHeight, etCreateWeight;
    Spinner etCreateGender,etHeightUnit;
    String[] gender = {"-Please select a gender-", "Male", "Female"};
    String[] heightUnit = {"-", "cm", "in"};
    boolean inputValid;
    ProgressDialog progressDialog;
    CardView cardView11;
    Bitmap bitmapDP;
    UploadTask uploadTask;

    FirebaseStorage storage;
    StorageReference storageRef, eventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        imgAdminProfile = findViewById(R.id.imgAdminProfile);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgAdminProfile);
        txSave = findViewById(R.id.txSave);
        cardView11 = findViewById(R.id.cardView11);
        txCreatePassword = findViewById(R.id.txCreatePassword);
        etCreatePassword = findViewById(R.id.etCreatePassword);
        etCreateEmail = findViewById(R.id.txAddEventTitle);
        etCreateFirstName = findViewById(R.id.etCreateFirstName);
        etCreateLastName = findViewById(R.id.etCreateLastName);
        etCreateContact = findViewById(R.id.etCreateContact);
        etCreateBirthdate = findViewById(R.id.etCreateBirthdate);
        etCreateHeight = findViewById(R.id.etCreateHeight);
        etCreateWeight = findViewById(R.id.etCreateWeight);
        etCreateGender = findViewById(R.id.etCreateGender);
        etHeightUnit = findViewById(R.id.etHeightUnit);

        constraintLayout4 = findViewById(R.id.constraintLayout4);
        txEmergencyCount = findViewById(R.id.txEmergencyCount);

        aaHeightUnit = new ArrayAdapter<String>(UpdateProfileActivity.this, R.layout.sp_format, heightUnit);
        aaHeightUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etHeightUnit.setAdapter(aaHeightUnit);
        aaGender = new ArrayAdapter<String>(UpdateProfileActivity.this, R.layout.sp_format, gender);
        aaGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCreateGender.setAdapter(aaGender);

        etCreateBirthdate.setInputType(InputType.TYPE_NULL);
        etCreateBirthdate.setOnClickListener(v -> {
            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    etCreateBirthdate.setText(year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));
                }
            };
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    UpdateProfileActivity.this, datePickerListener,
                    mYear, mMonth, mDay);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePickerDialog.show();
        });

        cardView11.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(UpdateProfileActivity.this).create();
            alertDialog.setTitle("Profile Picture");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Please select an image source");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery", (d, w) -> {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera", (d, w) -> {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", (d, w) -> {

            });
            alertDialog.show();
        });
        constraintLayout4.setOnClickListener(v->{
            Intent intent = new Intent(UpdateProfileActivity.this, EmergencyListActivity.class);
            startActivity(intent);
        });
        txSave.setOnClickListener(v -> {
            EditText[] editTexts = {etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateHeight, etCreateWeight};
            inputValid = true;
            Arrays.stream(editTexts).forEach(e -> {
                if (e.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please input a valid " + e.getTag().toString() + ".", Toast.LENGTH_LONG).show();
                    inputValid = false;
                }
            });
            if (etCreateGender.getSelectedItemPosition() < 1) inputValid = false;

            if (!inputValid) return;
            if (LoggedUser.getInstance().getAuth().equals("IN_APP")) {
                if (!Helper.getInstance().checkString(etCreatePassword.getText().toString().trim())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(UpdateProfileActivity.this).create();
                    alertDialog.setTitle("Password Confirmation");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Password should contain these criterias:\nAtleast 8 Characters\nContains atleast 1 special character\nContains atleast 1 numeric value\nShould start with a capital letter");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            (d, w) -> {

                            });

                    alertDialog.show();
                    return;
                }
            }
            AlertDialog alertDialog = new AlertDialog.Builder(UpdateProfileActivity.this).create();
            alertDialog.setTitle("Profile Settings");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to update your account?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (d, w) -> {

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (d, w) -> {
                updateAccount();
            });
            alertDialog.show();


        });
        List<String> heightList =  Arrays.asList(LoggedUser.getInstance().getHeight().split("@"));

        etCreatePassword.setVisibility(View.GONE);
        etCreateEmail.setText(LoggedUser.getInstance().getEmail());
        etCreateFirstName.setText(LoggedUser.getInstance().getFirstName());
        etCreateLastName.setText(LoggedUser.getInstance().getLastName());
        etCreateContact.setText(LoggedUser.getInstance().getPhoneNumber());
        etCreateBirthdate.setText(LoggedUser.getInstance().getBirthDate());
        if(heightList.size() < 2){
            etCreateHeight.setText(LoggedUser.getInstance().getHeight());
        } else{
            etCreateHeight.setText(heightList.get(0));
            etHeightUnit.setSelection(Arrays.asList(heightUnit).indexOf(heightList.get(1)));
        }

        etCreateWeight.setText(LoggedUser.getInstance().getWeight());
        etCreateGender.setSelection(Arrays.asList(gender).indexOf(LoggedUser.getInstance().getGender()));
        if (LoggedUser.getInstance().getAuth().equals("IN_APP")) {
            etCreatePassword.setVisibility(View.VISIBLE);
            imgShowPassword.setVisibility(View.VISIBLE);
            etCreatePassword.setText(LoggedUser.getInstance().getPassword());
            etCreateContact.setEnabled(false);
            Log.d(Helper.getInstance().log_code, "onCreate IT: " + etCreatePassword.getInputType());
            imgShowPassword.setOnClickListener(v->{
                if(etCreatePassword.getInputType() == 129){
                    etCreatePassword.setInputType(InputType.TYPE_CLASS_TEXT);
                } else{
                    etCreatePassword.setInputType(129);
                }
            });
        } else {
            imgShowPassword.setVisibility(View.GONE);
            etCreatePassword.setVisibility(View.GONE);
            txCreatePassword.setVisibility(View.GONE);
            etCreateEmail.setEnabled(false);
        }
    }

    private void updateAccount() {
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(UpdateProfileActivity.this, "Processing request.");
        progressDialog.show();
        Map<String, Object> params = new HashMap<>();
        if (!LoggedUser.getInstance().getAuth().equals("SSO"))
            params.put(Prefs.PASSWORD_KEY, etCreatePassword.getText().toString().trim());
        if (LoggedUser.getInstance().getAuth().equals("SSO"))
            params.put(Prefs.PHONE_KEY, etCreateContact.getText().toString().trim());
        params.put(Prefs.FN_KEY, etCreateFirstName.getText().toString().trim());
        params.put(Prefs.LN_KEY, etCreateLastName.getText().toString().trim());
        if (!LoggedUser.getInstance().getAuth().equals("SSO"))
            params.put(Prefs.EMAIL_KEY, etCreateEmail.getText().toString().trim());
        params.put(Prefs.GENDER_KEY, etCreateGender.getSelectedItem().toString());
        params.put(Prefs.BDAY_KEY, etCreateBirthdate.getText().toString().trim());
        params.put(Prefs.HEIGHT_KEY, etCreateHeight.getText().toString().trim() + "@" + etHeightUnit.getSelectedItem().toString());
        params.put(Prefs.WEIGHT_KEY, etCreateWeight.getText().toString().trim());
        params.put(Prefs.IMG_KEY, LoggedUser.getInstance().getImgUrl());
        VolleyHttp volleyHttp = new VolleyHttp("", params, "user-patch", UpdateProfileActivity.this);
        String json = volleyHttp.getResponseBody(true);
        progressDialog.dismiss();
        try {
            JSONObject reader = new JSONObject(json);
            int responseStatus = reader.getInt("status");
            if (responseStatus == 200) {
                Gson gson = new Gson();

                if (!LoggedUser.getInstance().getAuth().equals("SSO"))
                    Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.PASSWORD_KEY, etCreatePassword.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.FN_KEY, etCreateFirstName.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.LN_KEY, etCreateLastName.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.EMAIL_KEY, etCreateEmail.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.GENDER_KEY, etCreateGender.getSelectedItem().toString());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.BDAY_KEY, etCreateBirthdate.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.PHONE_KEY, etCreateContact.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.WEIGHT_KEY, etCreateWeight.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.HEIGHT_KEY, etCreateHeight.getText().toString().trim() + "@" + etHeightUnit.getSelectedItem().toString());
                Prefs.getInstance().setUser(UpdateProfileActivity.this,Prefs.EMERGENCY,gson.toJson(Helper.getInstance().getTempEmergencySet()));
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
            Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    Bitmap bitmap = imageReturnedIntent.getParcelableExtra("data");
                    bitmapDP = bitmap;
                    imgAdminProfile.setImageBitmap(bitmap);
                    uploadDP();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap = imageReturnedIntent.getParcelableExtra("data");
                    imgAdminProfile.setImageBitmap(bitmap);
                    Log.d(Helper.getInstance().log_code, "onActivityResult: " + selectedImage.toString());
                    uploadDP(selectedImage);
                }
                break;
        }
    }

    public void uploadDP(Uri imgURI) {
        new Thread(() -> {
            try {
                runOnUiThread(()->{
                    progressDialog = Helper.getInstance().progressDialog(UpdateProfileActivity.this, "Uploading profile picture.");
                    progressDialog.show();
                });

                bitmapDP = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);
                String ref = "dp/" + LoggedUser.getInstance().getUuid() + LocalDateTime.now().toString() + ".jpg";
                FirebaseApp.initializeApp(this);
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                eventRef = storageRef.child(ref);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapDP.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataIntent = baos.toByteArray();
                uploadTask = eventRef.putBytes(dataIntent);
                runOnUiThread(()-> progressDialog.dismiss());

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        runOnUiThread(() -> Toast.makeText(UpdateProfileActivity.this, "Failed to upload QR. Please try again", Toast.LENGTH_LONG).show());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateDP(uri.toString());
                            }
                        });
                    }
                });
            } catch (IOException e) {
                bitmapDP = null;
                runOnUiThread(() -> Toast.makeText(this, "Failed to retrieve image. Please try again", Toast.LENGTH_LONG).show());
            } finally {
                runOnUiThread(()->{
                    if(progressDialog != null) progressDialog.dismiss();
                });
            }
        }).start();

    }
    public void uploadDP() {
        new Thread(() -> {
            try {
                runOnUiThread(()->{
                    progressDialog = Helper.getInstance().progressDialog(UpdateProfileActivity.this, "Uploading profile picture.");
                    progressDialog.show();
                });
                String ref = "dp/" + LoggedUser.getInstance().getUuid() + LocalDateTime.now().toString() + ".jpg";
                FirebaseApp.initializeApp(this);
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                eventRef = storageRef.child(ref);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapDP.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataIntent = baos.toByteArray();
                uploadTask = eventRef.putBytes(dataIntent);
                runOnUiThread(()-> progressDialog.dismiss());

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        runOnUiThread(() -> Toast.makeText(UpdateProfileActivity.this, "Failed to upload QR. Please try again", Toast.LENGTH_LONG).show());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateDP(uri.toString());
                            }
                        });
                    }
                });
            } catch (Exception e) {
                bitmapDP = null;
                runOnUiThread(() -> Toast.makeText(this, "Failed to retrieve image. Please try again", Toast.LENGTH_LONG).show());
            } finally {
                runOnUiThread(()->{
                    if(progressDialog != null) progressDialog.dismiss();
                });
            }
        }).start();

    }
    public void updateDP(String imgURL) {
        runOnUiThread(()->{
            progressDialog = Helper.getInstance().progressDialog(UpdateProfileActivity.this, "Uploading profile picture.");
            progressDialog.show();
        });

        new Thread(()->{
            Map<String, Object> params = new HashMap<>();
            params.put(Prefs.IMG_KEY, imgURL);
            VolleyHttp volleyHttp = new VolleyHttp("", params, "user-patch", UpdateProfileActivity.this);
            String json = volleyHttp.getResponseBody(true);

            try {
                JSONObject reader = new JSONObject(json);
                int responseStatus = reader.getInt("status");
                if (responseStatus == 200) {
                    Prefs.getInstance().setUser(UpdateProfileActivity.this,Prefs.IMG_KEY,imgURL);
                    runOnUiThread(()-> Picasso.get().load(imgURL).into(imgAdminProfile));
                    runOnUiThread(()->Toast.makeText(this, "Profile picture updated successfully.", Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(()->Toast.makeText(this, "Failed to update image. Please try again.", Toast.LENGTH_LONG).show());
                }
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                runOnUiThread(()->Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_LONG).show());
            } finally {
                runOnUiThread(()->{
                    if(null != progressDialog) progressDialog.dismiss();
                });
            }
        }).start();

    }
    @Override
    protected void onResume() {
        super.onResume();
        txEmergencyCount.setText(
                Helper.getInstance().getTempEmergencySet().size() == 0 ? "No Contact Added":
                        String.valueOf(Helper.getInstance().getTempEmergencySet().size()).concat(" Contact(s) Added")
        );
    }
}