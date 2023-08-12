package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_account);

        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnCancelAccount = findViewById(R.id.btnCancelAccount);

        etCreateEmail = findViewById(R.id.etCreateEmail);
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
        photoURL = getIntent().getStringExtra("photoURL");
        etCreateEmail.setText(getIntent().getStringExtra("email"));
        etCreateFirstName.setText(getIntent().getStringExtra("firstname"));
        etCreateLastName.setText(getIntent().getStringExtra("lastname"));
        etCreateContact.setText(getIntent().getStringExtra("contact"));

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
            c.add(Calendar.MONTH, +1);
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePicker.setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        btnCancelAccount.setOnClickListener(v-> finish());
        btnUpdateAccount.setOnClickListener(v->{
            Map<String, Object> params = new HashMap<>();
            params.put("firstname",etCreateFirstName.getText().toString().trim());
            params.put("lastname",etCreateLastName.getText().toString().trim());
            params.put("contactNumber",etCreateContact.getText().toString().trim());
            params.put("emailAddress",etCreateEmail.getText().toString().trim());
            params.put("gender",etCreateGender.getSelectedItem().toString());
            params.put("birthday",etCreateBirthdate.getText().toString().trim());
            params.put("height",etCreateHeight.getText().toString().trim());
            params.put("weight",etCreateWeight.getText().toString().trim());
            params.put("photoUrl",photoURL);

            VolleyHttp volleyHttp = new VolleyHttp("", params, "user", frmAccount.this);
            String json = volleyHttp.getResponseBody();

            try {

                JSONObject reader =  new JSONObject(json);
                int responseStatus = reader.getInt("status");
                if(responseStatus == 200){
                    JSONObject dataObject = reader.getJSONObject("data");
                    JSONObject userObject = dataObject.getJSONObject("user");

                    LoggedUser.getInstance().setRefreshToken(dataObject.getString("token"));

                    Prefs.getInstance().setUser(frmAccount.this,"is_admin",userObject.getBoolean("isAdmin"));
                    Prefs.getInstance().setUser(frmAccount.this,"imgUrl",photoURL);
                    Prefs.getInstance().setUser(frmAccount.this,"firstName",etCreateFirstName.getText().toString().trim());
                    Prefs.getInstance().setUser(frmAccount.this,"lastName",etCreateLastName.getText().toString().trim());
                    Prefs.getInstance().setUser(frmAccount.this,"email",etCreateEmail.getText().toString().trim());
                    Prefs.getInstance().setUser(frmAccount.this,"gender",etCreateGender.getSelectedItem().toString());
                    Prefs.getInstance().setUser(frmAccount.this,"birthdate",etCreateBirthdate.getText().toString().trim());
                    Prefs.getInstance().setUser(frmAccount.this,"phoneNumber",etCreateContact.getText().toString().trim());
                    Prefs.getInstance().setUser(frmAccount.this,"weight",etCreateWeight.getText().toString().trim());
                    Prefs.getInstance().setUser(frmAccount.this,"height",etCreateHeight.getText().toString().trim());

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
                Log.d("Log_Padyak", "onCreate: " + e.getMessage());
                Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_SHORT).show();
            }

        });

    }
}