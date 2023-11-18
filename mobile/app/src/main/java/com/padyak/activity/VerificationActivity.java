package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    public static VerificationActivity verificationActivity;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String authSource,mobileNumber,password;
    boolean is_registration;
    private String verificationCode = "";
    TextView txMobileNumberCode,txResend,textView55;
    EditText txOtp1;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
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

        textView55 = findViewById(R.id.textView55);
        txResend = findViewById(R.id.txResend);
        txMobileNumberCode = findViewById(R.id.txMobileNumberCode);
        btnVerify = findViewById(R.id.btnVerify);

        txOtp1 = findViewById(R.id.txOtp1);


                txMobileNumberCode.setText(mobileNumber);

        btnVerify.setOnClickListener(v->{
            String otp = txOtp1.getText().toString();
            if(otp.trim().equals("")) return;
            btnVerify.setEnabled(false);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
            SigninWithPhone(credential);

        });
        sendOtp();
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
                        Toast.makeText(this, "Password updated successfully. Please try to log in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(VerificationActivity.this,SsoLoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(this, "Failed to register information. Please try again.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to communicate with server. Please try again.", Toast.LENGTH_LONG).show();
                }
            });

        }).start();


    }

    @Override
    public void onBackPressed() {

    }

    private void sendOtp() {
        textView55.setVisibility(View.INVISIBLE);
        txResend.setVisibility(View.INVISIBLE);
        String verifyMobile = "";
        if(mobileNumber.startsWith("0")){
            verifyMobile = mobileNumber.replaceFirst("0","+63");
        }

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(Helper.getInstance().log_code, "onVerificationFailed: " + e.getMessage());
                Toast.makeText(VerificationActivity.this, "OTP Sent failed, Please click on Resend OTP.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Log.d(Helper.getInstance().log_code, "onCodeSent: " + s);
                Toast.makeText(VerificationActivity.this, "OTP has been sent to " + mobileNumber, Toast.LENGTH_LONG).show();
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(verifyMobile)
                        .setTimeout(50L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView55.setVisibility(View.VISIBLE);
                txResend.setVisibility(View.VISIBLE);
            }
        }, 60000);
    }
    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        btnVerify.setEnabled(true);
                        if (task.isSuccessful()) {
                            if(is_registration){
                                frmAccount f = frmAccount.frmAccount;
                                f.registerAccount();
                                finish();
                            } else{
                                resetPassword();
                            }
                        } else {
                            Toast.makeText(VerificationActivity.this, "Invalid OTP. Please try again", Toast.LENGTH_LONG).show();
                            Log.d(Helper.getInstance().log_code, "onComplete: " + task.getException().getMessage());
                        }
                    }
                });
    }
}