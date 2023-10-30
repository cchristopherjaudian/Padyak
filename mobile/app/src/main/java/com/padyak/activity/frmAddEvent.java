package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.padyak.R;
import com.padyak.utility.CustomTimePicker;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class frmAddEvent extends AppCompatActivity {

    Button btnEventRegister, btnEventCancel;
    EditText txAddEventDate, txAddEventStart, txAddEventEnd, txAddEventAward, txAddEventDescription, txAddEventTitle;
    ImageView imgAddEvent;
    String selectedYear, selectedMonth;
    Bitmap bitmapEvent;
    byte[] data;
    FirebaseStorage storage;
    StorageReference storageRef, eventRef;
    UploadTask uploadTask;
    ProgressDialog progressDialog;
    boolean data_inserted;
    String startTime,endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_add_event);

        selectedYear = getIntent().getStringExtra("Year");
        selectedMonth = getIntent().getStringExtra("Month");

        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        txAddEventDate = findViewById(R.id.txAddEventDate);
        txAddEventStart = findViewById(R.id.txAddEventStart);
        txAddEventEnd = findViewById(R.id.txAddEventEnd);
        txAddEventTitle = findViewById(R.id.txAddEventTitle);
        txAddEventAward = findViewById(R.id.txAddEventAward);
        txAddEventDescription = findViewById(R.id.txAddEventDescription);


        imgAddEvent = findViewById(R.id.imgAddEvent);

        btnEventCancel.setOnClickListener(v -> finish());
        txAddEventDate.setInputType(InputType.TYPE_NULL);
        txAddEventStart.setInputType(InputType.TYPE_NULL);
        txAddEventEnd.setInputType(InputType.TYPE_NULL);

        imgAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 814);
        });
        txAddEventDate.setOnClickListener(v -> {
            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    txAddEventDate.setText(year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));
                }
            };
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    frmAddEvent.this, datePickerListener,
                    mYear, mMonth, mDay);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            c.add(Calendar.MONTH, +1);
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePicker.setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });
        txAddEventStart.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int mMonth = mcurrentTime.get(Calendar.MONTH);
            int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentTime.get(Calendar.YEAR);
            String catDate = String.valueOf(mYear) + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDay);
            CustomTimePicker mTimePicker;
            mTimePicker = new CustomTimePicker(frmAddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    startTime = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + ":00";
                    String timeSuffix = "";
                    timeSuffix = (selectedHour >= 12) ? "PM" : "AM";
                    selectedHour = (selectedHour > 12) ? selectedHour - 12 : selectedHour;
                    txAddEventStart.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + timeSuffix);
//                    if (catDate.equals(txAddEventStart.getText().toString().trim())) {
//                        if (selectedHour < hour) {
//                            Toast.makeText(frmAddEvent.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if (selectedHour == hour && selectedMinute <= minute) {
//                            Toast.makeText(frmAddEvent.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                    String timeSuffix = "";
//                    timeSuffix = (selectedHour >= 12) ? "PM" : "AM";
//                    selectedHour = (selectedHour > 12) ? selectedHour - 12 : selectedHour;
//                    txAddEventStart.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + timeSuffix);

                }
            }, hour, minute, false);
            mTimePicker.show();
        });
        txAddEventEnd.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int mMonth = mcurrentTime.get(Calendar.MONTH);
            int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentTime.get(Calendar.YEAR);
            String catDate = String.valueOf(mYear) + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDay);
            CustomTimePicker mTimePicker;
            mTimePicker = new CustomTimePicker(frmAddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    endTime = String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + ":00";
                    String timeSuffix = "";
                    timeSuffix = (selectedHour >= 12) ? "PM" : "AM";
                    selectedHour = (selectedHour > 12) ? selectedHour - 12 : selectedHour;
                    txAddEventEnd.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute) + timeSuffix);
                }
            }, hour, minute, false);
            mTimePicker.show();
        });

        btnEventRegister.setOnClickListener(v -> {
            if (txAddEventTitle.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please input an event title", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txAddEventDate.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please input an event date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txAddEventAward.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please input an event award", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txAddEventDescription.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please input an event description", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txAddEventEnd.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please input an event end date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txAddEventStart.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please input an event start date", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog alertDialog = new AlertDialog.Builder(frmAddEvent.this).create();
            alertDialog.setTitle("Event Registration");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to register this event?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    (d, w) -> {
                        startTime = txAddEventDate.getText().toString().trim().concat("T").concat(startTime).concat("+08:00");
                        endTime = txAddEventDate.getText().toString().trim().concat("T").concat(endTime).concat("+08:00");
                        progressDialog = Helper.getInstance().progressDialog(frmAddEvent.this, "Registering event.");
                        progressDialog.show();
                        new Thread(() -> {
                            if (bitmapEvent != null) {
                                String ref = "events/" + LoggedUser.getInstance().getUuid() + "/" + LocalDateTime.now().toString() + ".jpg";
                                FirebaseApp.initializeApp(this);
                                storage = FirebaseStorage.getInstance();
                                storageRef = storage.getReference();
                                eventRef = storageRef.child(ref);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmapEvent.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                data = baos.toByteArray();
                                uploadTask = eventRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        saveEvent("");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                saveEvent(uri.toString());
                                            }
                                        });

                                    }
                                });
                            } else {
                                saveEvent("n/a");
                            }
                        }).start();
                    });
            alertDialog.show();
        });
    }

    private void saveEvent(String imgURL) {
        try {
            if (imgURL.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "Failed to upload image. Please try again.", Toast.LENGTH_SHORT).show());
                return;
            }
            String etDate = txAddEventDate.getText().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(etDate, formatter);

            String monthName = Helper.getInstance().toTitleCase(dateTime.getMonth().name());
            String yearName = String.valueOf(dateTime.getYear());

            String eventName = txAddEventTitle.getText().toString().trim();
            String eventDescription = txAddEventDescription.getText().toString().trim();
            String eventAward = txAddEventAward.getText().toString().trim();

            String eventStart = startTime; //txAddEventStart.getText().toString().trim();
            String eventEnd = endTime; //txAddEventEnd.getText().toString().trim();

            Map<String, Object> payload = new HashMap<>();
            payload.put("month", monthName);
            payload.put("year", yearName);
            payload.put("eventDate", etDate);
            payload.put("name", eventName);
            payload.put("startTime", eventStart);
            payload.put("endTime", eventEnd);
            payload.put("photoUrl", imgURL);
            payload.put("eventDescription", eventDescription);
            payload.put("award", eventAward);

            VolleyHttp volleyHttp = new VolleyHttp("", payload, "event", frmAddEvent.this);
            String response = volleyHttp.getResponseBody(true);
            data_inserted = false;
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
                    Toast.makeText(this, "Event registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to register event. Please try again", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception err) {
            Log.d(Helper.getInstance().log_code, "saveEvent: " + err.getMessage());
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Failed to register event. Please try again", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 814 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                bitmapEvent = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imgAddEvent.setImageBitmap(bitmapEvent);
                imgAddEvent.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (IOException e) {
                imgAddEvent.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image. Please try again", Toast.LENGTH_SHORT).show();
            }
        }

    }
}