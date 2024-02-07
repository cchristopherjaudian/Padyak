package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.adapter.adapterAdminAlert;
import com.padyak.dto.MemberAlert;
import com.padyak.utility.AdminHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class frmAdminAlert extends AppCompatActivity {
    RecyclerView rvAlertList;
    LinearLayoutManager linearLayoutManager;
    List<MemberAlert> memberAlertList;
    com.padyak.adapter.adapterAdminAlert adapterAdminAlert;
    ImageView imgDP;
    ProgressDialog progressDialog;
    CardView cardView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_admin_alert);
        cardView4 = findViewById(R.id.cardView4);
        imgDP = findViewById(R.id.imgDP);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);
        rvAlertList = findViewById(R.id.rvAlertList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAlertList.setLayoutManager(linearLayoutManager);
        cardView4.setOnClickListener(v->{
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("FCMIntentService"));
        loadAlerts();
    }

    public void loadAlerts(){
        progressDialog = Helper.getInstance().progressDialog(frmAdminAlert.this,"Fetching alerts.");
        progressDialog.show();
        new Thread(()->{
                memberAlertList = new ArrayList<>();
                VolleyHttp volleyHttp = new VolleyHttp("/?status=ACTIVE",null,"alert",frmAdminAlert.this);
                JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                runOnUiThread(()->{
                    if(responseJSON == null){
                        Toast.makeText(this, "Failed to retrieve list of alerts. Please try again", Toast.LENGTH_LONG).show();
                        finish();
                    } else{
                        JSONArray alertArray = responseJSON.optJSONArray("data");
                        for(int i = 0; i < alertArray.length(); i++){
                            try {
                                JSONObject alertObject = alertArray.getJSONObject(i);
                                JSONObject userObject = alertObject.getJSONObject("sender");
                                MemberAlert memberAlert = new MemberAlert();
                                memberAlert.setAlertId(alertObject.getString("id"));
                                memberAlert.setAlertLevel(alertObject.getInt("level"));
                                memberAlert.setUserImage(userObject.getString("photoUrl"));
                                memberAlert.setLatitude(Double.parseDouble(alertObject.getString("latitude")));
                                memberAlert.setLongitude(Double.parseDouble(alertObject.getString("longitude")));
                                memberAlert.setLocationName(alertObject.getString("location"));
                                memberAlert.setCreatedAt(alertObject.getString("createdAt"));
                                memberAlert.setUserName(userObject.getString("firstname").concat(" ").concat(userObject.getString("lastname")));
                                memberAlertList.add(memberAlert);
                            } catch (JSONException e) {
                                Log.d(Helper.getInstance().log_code, "loadAlerts: " + e.getMessage());
                            }
                        }
                        adapterAdminAlert = new adapterAdminAlert(memberAlertList);
                        rvAlertList.setAdapter(adapterAdminAlert);
                        progressDialog.dismiss();
                    }
                });
        }).start();
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            try {
                AdminHelper.getInstance().showMessageAlert(getSupportFragmentManager(),message);
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onReceive: " + e.getMessage());
                Log.d(Helper.getInstance().log_code, "onReceive: " + message);
            }
        }
    };
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}