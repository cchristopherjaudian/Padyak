package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.adapter.adapterAdminPaymentValidation;
import com.padyak.adapter.adapterPaymentValidation;
import com.padyak.dto.UserValidation;
import com.padyak.fragment.fragmentUserUploaded;
import com.padyak.utility.AdminHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentValidationActivity extends AppCompatActivity {
    public static PaymentValidationActivity me;
    RecyclerView rvValidation;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterAdminPaymentValidation adapterAdminPaymentValidation;
    String eventId;
    Button btnEventCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validation);
        me = this;
        eventId = getIntent().getStringExtra("eventId");
        rvValidation = findViewById(R.id.rvValidation);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        btnEventCancel.setOnClickListener(v-> finish());
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvValidation.setLayoutManager(linearLayoutManager);
        loadValidations();
    }

    public void loadValidations(){
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(PaymentValidationActivity.this, "Retrieving participants.");
        progressDialog.show();
        try {

            VolleyHttp volleyHttp = new VolleyHttp("/".concat(eventId), null, "event", PaymentValidationActivity.this);
            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
            if (responseJSON == null) throw new Exception("responseJSON is null");
            JSONObject eventObject = responseJSON.getJSONObject("data");
            JSONArray participantJSON = eventObject.optJSONArray("registeredUser");
            List<UserValidation> participants = new ArrayList<>();
            for (int i = 0; i < participantJSON.length(); i++) {
                String paymentStatus = participantJSON.getJSONObject(i).getString("status");
                String paymentURL = participantJSON.getJSONObject(i).getString("paymentUrl");
                JSONObject participantObject = participantJSON.getJSONObject(i).getJSONObject("user");
                participants.add(new UserValidation(participantObject.getString("id"),participantObject.getString("firstname").concat(" ").concat(participantObject.getString("lastname")),participantObject.getString("photoUrl"),paymentStatus,paymentURL));
            }
            adapterAdminPaymentValidation = new adapterAdminPaymentValidation(participants);
            rvValidation.setAdapter(adapterAdminPaymentValidation);
        } catch (Exception err){
            Log.d(Helper.getInstance().log_code, "loadValidations: " + err.getMessage());
        } finally {
            progressDialog.dismiss();
        }
    }
    public void showUserUploaded(String username, String photoUrl,String userId, String paymentURL){
        FragmentManager fm = getSupportFragmentManager();
        fragmentUserUploaded editNameDialogFragment = fragmentUserUploaded.newInstance("UserUpload",username,photoUrl,userId,eventId,paymentURL);
        editNameDialogFragment.show(fm, "UserUpload");
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
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("FCMIntentService"));
    }
}