package com.padyak.activity;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.adapter.adapterEmergencyContact;
import com.padyak.dto.EmergencyContact;
import com.padyak.fragment.ContactSelectFragment;
import com.padyak.fragment.fragmentViewQR;
import com.padyak.utility.Helper;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    String phonebookName,phonebookNumber;
    private static final int REQUEST_SELECT_CONTACT = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_list);
        emergencyListActivity = this;
        rvEmergencyList = findViewById(R.id.rvEmergencyList);
        btnEmergencyAdd = findViewById(R.id.btnEmergencyAdd);
        imgEmpty = findViewById(R.id.imgEmpty);
        txEmpty = findViewById(R.id.txEmpty);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEmergencyList.setLayoutManager(linearLayoutManager);

        btnEmergencyAdd.setOnClickListener(v->{
            FragmentManager fm = getSupportFragmentManager();
            ContactSelectFragment contactSelectFragment = ContactSelectFragment.newInstance("ContactSelectFragment");
            contactSelectFragment.setCancelable(false);
            contactSelectFragment.show(fm, "ContactSelectFragment");
        });
    }
    public void browseContacts(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else{
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }


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

        if(emergencyContactSet.isEmpty()){
            txEmpty.setVisibility(View.VISIBLE);
            imgEmpty.setVisibility(View.VISIBLE);
            rvEmergencyList.setVisibility(View.GONE);
        } else{
            txEmpty.setVisibility(View.GONE);
            imgEmpty.setVisibility(View.GONE);
            rvEmergencyList.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateEmergencyList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Helper.getInstance().log_code, "onActivityResult: ReqCode " + requestCode);
        Log.d(Helper.getInstance().log_code, "onActivityResult: ResCode " + resultCode);
        if (requestCode == REQUEST_SELECT_CONTACT) {
            Uri contactUri = data.getData();
            String id = contactUri.getLastPathSegment();

            Cursor nameCursor = getContentResolver().query(contactUri, null, null, null, null);
            String name = "";
            if (nameCursor != null && nameCursor.moveToFirst()) {
                int nameIndex = nameCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                if(nameIndex != -1) name = nameCursor.getString(nameIndex);
                nameCursor.close();
            }
            Cursor numberCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id}, null);

            String number = "";
            if (numberCursor != null && numberCursor.moveToFirst()) {
                int numberIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if(numberIndex != -1) number = numberCursor.getString(numberIndex);
                numberCursor.close();
            }
            if(number.equals("") || name.equals("")){
                Toast.makeText(emergencyListActivity, "Failed to retrieve contact. Please try again", Toast.LENGTH_SHORT).show();
            } else{
                number = number.startsWith("+63") ? number.replace("+63","0") : number;
                if(Helper.getInstance().checkTempContact(number)){
                    Toast.makeText(emergencyListActivity, "Phone Number already exists in your emergency contact list", Toast.LENGTH_SHORT).show();
                } else{
                    phonebookNumber = number;
                    phonebookName = name;
                    progressDialog = Helper.getInstance().progressDialog(EmergencyListActivity.this, "Adding emergency contact.");
                    progressDialog.show();
                    new Thread(()->{
                        boolean data_inserted = false;
                        Map<String, Object> payload = new HashMap<>();
                        payload.put("firstname", phonebookName);
                        payload.put("contact", phonebookNumber);
                        VolleyHttp volleyHttp = new VolleyHttp("", payload, "contacts-patch", EmergencyListActivity.this);
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
                                EmergencyContact emergencyContact = new EmergencyContact(phonebookName,"",phonebookNumber);
                                Helper.getInstance().addTempEmergencyContact(emergencyContact);
                                Gson gson = new Gson();
                                String updatedEmergency = gson.toJson(Helper.getInstance().getTempEmergencySet());
                                Prefs.getInstance().setUser(EmergencyListActivity.this,Prefs.EMERGENCY,updatedEmergency);
                                Toast.makeText(this, "Emergency Contact Added", Toast.LENGTH_LONG).show();
                                updateEmergencyList();
                            });
                        } else{
                            runOnUiThread(()->{
                                Toast.makeText(this, "Failed to add emergency contact. Please try again.", Toast.LENGTH_LONG).show();
                            });
                        }
                    }).start();
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                browseContacts();
            }
        }
    }
}