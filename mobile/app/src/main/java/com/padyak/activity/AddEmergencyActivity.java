package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.dto.EmergencyContact;
import com.padyak.utility.Helper;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddEmergencyActivity extends AppCompatActivity {

    EditText etFirstName,etLastName,etPhoneNumber;
    Button btnEmergencySave;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        btnEmergencySave = findViewById(R.id.btnEmergencySave);

        btnEmergencySave.setOnClickListener(v->{
            String lastName,firstName,phoneNumber;

            lastName = etLastName.getText().toString().trim();
            firstName = etFirstName.getText().toString().trim();
            phoneNumber = etPhoneNumber.getText().toString().trim();

            if(firstName.isEmpty()){
                Toast.makeText(this, "Please input person's first name", Toast.LENGTH_LONG).show();
                return;
            }
            if(phoneNumber.length() != 11){
                Toast.makeText(this, "Please input a valid phone number", Toast.LENGTH_LONG).show();
                return;
            }
            if(Helper.getInstance().checkTempContact(phoneNumber)){
                Toast.makeText(this, "Phone Number already exists in your emergency contact list", Toast.LENGTH_LONG).show();
                return;
            }
            progressDialog = Helper.getInstance().progressDialog(AddEmergencyActivity.this, " Saving contact.");
            progressDialog.show();
            new Thread(()->{
                boolean data_inserted = false;
                Map<String, Object> payload = new HashMap<>();
                payload.put("firstname", firstName);
                if(!lastName.isEmpty()) payload.put("lastname", lastName);
                payload.put("contact", phoneNumber);
                VolleyHttp volleyHttp = new VolleyHttp("", payload, "contacts-patch", AddEmergencyActivity.this);
                String response = volleyHttp.getResponseBody(true);
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    int responseCode = responseJSON.getInt("status");
                    if (responseCode == 200) data_inserted = true;
                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "onCreate: JSON " + e.getMessage());
                } finally {
                    runOnUiThread(()-> progressDialog.dismiss());
                }

                if(data_inserted){
                    runOnUiThread(()->{
                        EmergencyContact emergencyContact = new EmergencyContact(firstName,lastName,phoneNumber);
                        Helper.getInstance().addTempEmergencyContact(emergencyContact);
                        Gson gson = new Gson();
                        String updatedEmergency = gson.toJson(Helper.getInstance().getTempEmergencySet());
                        Prefs.getInstance().setUser(AddEmergencyActivity.this,Prefs.EMERGENCY,updatedEmergency);
                        Toast.makeText(this, "Emergency Contact Added", Toast.LENGTH_LONG).show();
                        finish();
                    });
                } else{
                    runOnUiThread(()->{
                        Toast.makeText(this, "Failed to add emergency contact. Please try again.", Toast.LENGTH_LONG).show();
                    });
                }
            }).start();


        });
    }
}