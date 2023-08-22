package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class frmAccount extends AppCompatActivity {
    String[] gender = {"-Please select a gender-", "Male", "Female"};
    String photoURL = "";
    ArrayAdapter<String> aaGender;
    EditText etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateBirthdate, etCreateHeight, etCreateWeight;
    Spinner etCreateGender;
    Button btnUpdateAccount,btnCancelAccount;
    boolean inputValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_account);

        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnCancelAccount = findViewById(R.id.btnCancelAccount);

        etCreateEmail = findViewById(R.id.txAddEventTitle);
        etCreateFirstName = findViewById(R.id.etCreateFirstName);
        etCreateLastName = findViewById(R.id.etCreateLastName);
        etCreateContact = findViewById(R.id.etCreateContact);
        etCreateBirthdate = findViewById(R.id.etCreateBirthdate);
        etCreateHeight = findViewById(R.id.etCreateHeight);
        etCreateWeight = findViewById(R.id.etCreateWeight);
        etCreateGender = findViewById(R.id.etCreateGender);

        aaGender = new ArrayAdapter<String>(frmAccount.this, R.layout.sp_format, gender);
        aaGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCreateGender.setAdapter(aaGender);
        photoURL = getIntent().getStringExtra(Prefs.IMG_KEY);
        etCreateEmail.setText(getIntent().getStringExtra(Prefs.EMAIL_KEY));
        etCreateFirstName.setText(getIntent().getStringExtra(Prefs.FN_KEY));
        etCreateLastName.setText(getIntent().getStringExtra(Prefs.LN_KEY));
        etCreateContact.setText(getIntent().getStringExtra(Prefs.PHONE_KEY));

        etCreateBirthdate.setInputType(InputType.TYPE_NULL);
        etCreateBirthdate.setOnClickListener(v->{
            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    etCreateBirthdate.setText(year + "-" + String.format("%02d",monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));
                }
            };
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    frmAccount.this, datePickerListener,
                    mYear, mMonth, mDay);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePickerDialog.show();
        });

        btnCancelAccount.setOnClickListener(v-> finish());
        btnUpdateAccount.setOnClickListener(v->{
            EditText[] editTexts = {etCreateEmail,etCreateFirstName,etCreateLastName,etCreateContact,etCreateHeight,etCreateWeight};
            inputValid = true;
            Arrays.stream(editTexts).forEach(e->{
                if(e.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Please input a valid " + e.getTag().toString() + ".", Toast.LENGTH_SHORT).show();
                    inputValid = false;
                }
            });

            if(!inputValid) return;

            AlertDialog alertDialog = new AlertDialog.Builder(frmAccount.this).create();
            alertDialog.setTitle("Send Alert");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to send this alert?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No",(d,w)->{

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes",(d,w)->{
                registerAccount();
            });
            alertDialog.show();


        });

    }

    private void registerAccount(){

        ProgressDialog progressDialog = Helper.getInstance().progressDialog(frmAccount.this,"Processing request.");
        progressDialog.show();
        Map<String, Object> params = new HashMap<>();
        params.put(Prefs.FN_KEY,etCreateFirstName.getText().toString().trim());
        params.put(Prefs.LN_KEY,etCreateLastName.getText().toString().trim());
        params.put(Prefs.PHONE_KEY,etCreateContact.getText().toString().trim());
        params.put(Prefs.EMAIL_KEY,etCreateEmail.getText().toString().trim());
        params.put(Prefs.GENDER_KEY,etCreateGender.getSelectedItem().toString());
        params.put(Prefs.BDAY_KEY,etCreateBirthdate.getText().toString().trim());
        params.put(Prefs.HEIGHT_KEY,etCreateHeight.getText().toString().trim());
        params.put(Prefs.WEIGHT_KEY,etCreateWeight.getText().toString().trim());
        params.put(Prefs.IMG_KEY,photoURL);

        VolleyHttp volleyHttp = new VolleyHttp("", params, "user", frmAccount.this);
        String json = volleyHttp.getResponseBody(false);
        progressDialog.dismiss();
        try {

            JSONObject reader =  new JSONObject(json);
            int responseStatus = reader.getInt("status");
            if(responseStatus == 200){
                JSONObject dataObject = reader.getJSONObject("data");
                JSONObject userObject = dataObject.getJSONObject("user");

                LoggedUser.getInstance().setRefreshToken(dataObject.getString("token"));

                Prefs.getInstance().setUser(frmAccount.this,Prefs.ADMIN_KEY,userObject.getBoolean(Prefs.ADMIN_KEY));
                Prefs.getInstance().setUser(frmAccount.this,Prefs.IMG_KEY,photoURL);
                Prefs.getInstance().setUser(frmAccount.this,Prefs.FN_KEY,etCreateFirstName.getText().toString().trim());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.LN_KEY,etCreateLastName.getText().toString().trim());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.EMAIL_KEY,etCreateEmail.getText().toString().trim());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.GENDER_KEY,etCreateGender.getSelectedItem().toString());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.BDAY_KEY,etCreateBirthdate.getText().toString().trim());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.PHONE_KEY,etCreateContact.getText().toString().trim());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.WEIGHT_KEY,etCreateWeight.getText().toString().trim());
                Prefs.getInstance().setUser(frmAccount.this,Prefs.HEIGHT_KEY,etCreateHeight.getText().toString().trim());

                Intent intent;
                if(userObject.getBoolean("isAdmin")){
                    intent = new Intent(frmAccount.this, AdminMainActivity.class);
                } else{
                    intent = new Intent(frmAccount.this, frmMain.class);
                }
                startActivity(intent);
                finish();
            } else{
                Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
            progressDialog.dismiss();
            Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}