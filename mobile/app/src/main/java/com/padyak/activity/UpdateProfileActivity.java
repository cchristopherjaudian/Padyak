package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    ImageView imgAdminProfile;
    TextView txSave,txCreatePassword;
    ArrayAdapter<String> aaGender;
    EditText etCreatePassword,etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateBirthdate, etCreateHeight, etCreateWeight;
    Spinner etCreateGender;
    String[] gender = {"-Please select a gender-", "Male", "Female"};
    boolean inputValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        imgAdminProfile = findViewById(R.id.imgAdminProfile);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgAdminProfile);
        txSave = findViewById(R.id.txSave);
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

        txSave.setOnClickListener(v -> {
            EditText[] editTexts = {etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateHeight, etCreateWeight};
            inputValid = true;
            Arrays.stream(editTexts).forEach(e -> {
                if (e.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please input a valid " + e.getTag().toString() + ".", Toast.LENGTH_SHORT).show();
                    inputValid = false;
                }
            });
            if (etCreateGender.getSelectedItemPosition() < 1) inputValid = false;

            if (!inputValid) return;

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
        etCreatePassword.setVisibility(View.GONE);
        etCreateEmail.setText(LoggedUser.getInstance().getEmail());
        etCreateFirstName.setText(LoggedUser.getInstance().getFirstName());
        etCreateLastName.setText(LoggedUser.getInstance().getLastName());
        etCreateContact.setText(LoggedUser.getInstance().getPhoneNumber());
        etCreateBirthdate.setText(LoggedUser.getInstance().getBirthDate());
        etCreateHeight.setText(LoggedUser.getInstance().getHeight());
        etCreateWeight.setText(LoggedUser.getInstance().getWeight());
        etCreateGender.setSelection(Arrays.asList(gender).indexOf(LoggedUser.getInstance().getGender()));
        if(LoggedUser.getInstance().getAuth().equals("IN_APP")){
            etCreatePassword.setVisibility(View.VISIBLE);
            etCreatePassword.setText(LoggedUser.getInstance().getPassword());
            etCreateContact.setEnabled(false);
        } else{
            etCreatePassword.setVisibility(View.GONE);
            txCreatePassword.setVisibility(View.GONE);
            etCreateEmail.setEnabled(false);
        }
    }
    private void updateAccount(){
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(UpdateProfileActivity.this, "Processing request.");
        progressDialog.show();
        Map<String, Object> params = new HashMap<>();
        params.put(Prefs.PASSWORD_KEY, etCreatePassword.getText().toString().trim());
        params.put(Prefs.FN_KEY, etCreateFirstName.getText().toString().trim());
        params.put(Prefs.LN_KEY, etCreateLastName.getText().toString().trim());
        params.put(Prefs.EMAIL_KEY, etCreateEmail.getText().toString().trim());
        params.put(Prefs.GENDER_KEY, etCreateGender.getSelectedItem().toString());
        params.put(Prefs.BDAY_KEY, etCreateBirthdate.getText().toString().trim());
        params.put(Prefs.HEIGHT_KEY, etCreateHeight.getText().toString().trim());
        params.put(Prefs.WEIGHT_KEY, etCreateWeight.getText().toString().trim());
        params.put(Prefs.IMG_KEY, LoggedUser.getInstance().getImgUrl());
        VolleyHttp volleyHttp = new VolleyHttp("", params, "user-patch", UpdateProfileActivity.this);
        String json = volleyHttp.getResponseBody(true);
        progressDialog.dismiss();
        try {
            JSONObject reader = new JSONObject(json);
            int responseStatus = reader.getInt("status");
            if (responseStatus == 200) {
                if(!LoggedUser.getInstance().getAuth().equals("SSO")) Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.PASSWORD_KEY, etCreatePassword.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.FN_KEY, etCreateFirstName.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.LN_KEY, etCreateLastName.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.EMAIL_KEY, etCreateEmail.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.GENDER_KEY, etCreateGender.getSelectedItem().toString());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.BDAY_KEY, etCreateBirthdate.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.PHONE_KEY, etCreateContact.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.WEIGHT_KEY, etCreateWeight.getText().toString().trim());
                Prefs.getInstance().setUser(UpdateProfileActivity.this, Prefs.HEIGHT_KEY, etCreateHeight.getText().toString().trim());

                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
            Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}