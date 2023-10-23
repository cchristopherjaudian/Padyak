package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {
    public static VerificationActivity verificationActivity;

    String authSource,mobileNumber,password;
    boolean is_registration;

    TextView txMobileNumberCode,txResend;
    EditText txOtp1,txOtp2,txOtp3,txOtp4;
    Button btnVerify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verificationActivity = this;
        authSource = getIntent().getStringExtra("authSource");
        mobileNumber = getIntent().getStringExtra("mobileNumber");
        password = getIntent().getStringExtra("password");
        is_registration = getIntent().getBooleanExtra("registration",false);

        txResend = findViewById(R.id.txResend);
        txMobileNumberCode = findViewById(R.id.txMobileNumberCode);
        btnVerify = findViewById(R.id.btnVerify);

        txOtp1 = findViewById(R.id.txOtp1);
        txOtp2 = findViewById(R.id.txOtp2);
        txOtp3 = findViewById(R.id.txOtp3);
        txOtp4 = findViewById(R.id.txOtp4);

        txMobileNumberCode.setText(mobileNumber);

        btnVerify.setOnClickListener(v->{
            if(is_registration){
                inAppRegister();
            } else{
                resetPassword();
            }

        });
    }
    private void resetPassword(){
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(VerificationActivity.this, "Processing request.");
        progressDialog.show();

        new Thread(()->{
            Map<String, Object> params = new HashMap<>();
            params.put("contactNumber",mobileNumber);
            params.put("password",password);
            VolleyHttp volleyHttp = new VolleyHttp("/forgot", params, "user-patch", VerificationActivity.this);
            String json = volleyHttp.getResponseBody(false);
            runOnUiThread(()->{
                try {
                    progressDialog.dismiss();
                    JSONObject reader = new JSONObject(json);
                    int responseStatus = reader.getInt("status");
                    if (responseStatus == 200) {
                        Toast.makeText(this, "Password updated successfully. Please try to log in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerificationActivity.this,SsoLoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();
    }
    private void inAppRegister(){
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(VerificationActivity.this, "Processing request.");
        progressDialog.show();

        new Thread(()->{
            Map<String, Object> params = new HashMap<>();
            params.put("contactNumber",mobileNumber);
            params.put("password",password);
            params.put("source","IN_APP");
            VolleyHttp volleyHttp = new VolleyHttp("/inapp/signup", params, "user", VerificationActivity.this);
            String json = volleyHttp.getResponseBody(false);
            runOnUiThread(()->{
                try {
                    progressDialog.dismiss();
                    JSONObject reader = new JSONObject(json);
                    int responseStatus = reader.getInt("status");
                    if (responseStatus == 200) {
                        JSONObject dataObject = reader.getJSONObject("data");
                        LoggedUser.getInstance().setRefreshToken(dataObject.getString("token"));
                        Intent intent = new Intent(VerificationActivity.this,frmAccount.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("source","IN_APP");
                        bundle.putString("mobileNumber",mobileNumber);
                        bundle.putString("password",password);
                        bundle.putBoolean("register",true);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();


    }
}