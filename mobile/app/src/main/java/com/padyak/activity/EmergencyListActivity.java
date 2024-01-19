package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.adapter.adapterEmergencyContact;
import com.padyak.dto.EmergencyContact;
import com.padyak.utility.Helper;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EmergencyListActivity extends AppCompatActivity {
    public static EmergencyListActivity emergencyListActivity;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvEmergencyList;
    Button btnEmergencyAdd;
    ImageView imgEmpty;
    TextView txEmpty;
    com.padyak.adapter.adapterEmergencyContact adapterEmergencyContact;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_list);
        emergencyListActivity = this;
        rvEmergencyList = findViewById(R.id.rvEmergencyList);
        btnEmergencyAdd = findViewById(R.id.btnEmergencyAdd);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEmergencyList.setLayoutManager(linearLayoutManager);

        btnEmergencyAdd.setOnClickListener(v->{
            Intent intent = new Intent(EmergencyListActivity.this, AddEmergencyActivity.class);
            startActivity(intent);
        });
    }
    public void removeEmergencyContact(String phoneNumber){
        progressDialog = Helper.getInstance().progressDialog(EmergencyListActivity.this, "Removing contact.");
        progressDialog.show();
        new Thread(()->{
            boolean data_removed = false;
            Map<String, Object> payload = new HashMap<>();
            payload.put("contact", phoneNumber);
            VolleyHttp volleyHttp = new VolleyHttp("/remove", payload, "contacts-patch", EmergencyListActivity.this);
            String response = volleyHttp.getResponseBody(true);
            try {
                JSONObject responseJSON = new JSONObject(response);
                int responseCode = responseJSON.getInt("status");
                if (responseCode == 200) data_removed = true;
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onCreate: JSON " + e.getMessage());
            } finally {
                runOnUiThread(()-> progressDialog.dismiss());
            }

            if(data_removed){
                runOnUiThread(()->{
                    Helper.getInstance().removeTempEmergencyContact(phoneNumber);
                    Gson gson = new Gson();
                    String updatedEmergency = gson.toJson(Helper.getInstance().getTempEmergencySet());
                    Prefs.getInstance().setUser(EmergencyListActivity.this,Prefs.EMERGENCY,updatedEmergency);
                    Toast.makeText(emergencyListActivity, "Emergency contact removed", Toast.LENGTH_LONG).show();
                    updateEmergencyList();
                });
            } else{
                runOnUiThread(()->{
                    Toast.makeText(this, "Failed to remove emergency contact. Please try again.", Toast.LENGTH_LONG).show();
                });
            }
        }).start();

    }
    void updateEmergencyList(){
        Set<EmergencyContact> emergencyContactSet = Helper.getInstance().getTempEmergencySet();
        adapterEmergencyContact = new adapterEmergencyContact(new ArrayList<>(emergencyContactSet));
        rvEmergencyList.setAdapter(adapterEmergencyContact);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateEmergencyList();
    }
}