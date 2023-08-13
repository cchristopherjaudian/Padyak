package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.padyak.R;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class frmSplash extends AppCompatActivity {
    ProgressBar spinner;
    Intent intent;
    SignInButton btnGAuth;
    TextView textView2;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_splash);
        textView2 = findViewById(R.id.textView2);
        spinner = findViewById(R.id.progressBar);
        btnGAuth = findViewById(R.id.btnGAuth);
        spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getResources().getString(R.string.webauth))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();

        btnGAuth.setOnClickListener(v -> {
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(frmSplash.this, new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            try {

                                startIntentSenderForResult(
                                        result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                        null, 0, 0, 0);
                            } catch (IntentSender.SendIntentException e) {
                                System.out.println(e.getMessage());
                                Toast.makeText(frmSplash.this, "Failed to authenticate account. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(frmSplash.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(frmSplash.this, "Failed to authenticate account. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        Prefs.getInstance().getUser(this);
        Log.d("Log_Padyak", "SP LoggedUser: " + LoggedUser.getInstance().toString());
        if (LoggedUser.getInstance().getEmail().equals("")) {
            spinner.setVisibility(View.INVISIBLE);
            textView2.setText("Please Sign-in with your Google Account");
            btnGAuth.setVisibility(View.VISIBLE);
        } else {
            spinner.setVisibility(View.VISIBLE);
            textView2.setText("Connecting. Please wait...");
            btnGAuth.setVisibility(View.GONE);
            validateLogin(LoggedUser.getInstance().getEmail());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String reqParam = "/email?emailAddress=".concat(credential.getId());
                    VolleyHttp volleyHttp = new VolleyHttp(reqParam, null, "user", frmSplash.this);
                    int resCode = volleyHttp.getResponseStatus();
                    if (resCode == 200) {
                        validateLogin(credential.getId());
                    } else if (resCode == 404) {
                        Bundle b = new Bundle();
                        b.putString(Prefs.IMG_KEY,credential.getProfilePictureUri().toString());
                        b.putString(Prefs.EMAIL_KEY, credential.getId());
                        b.putString(Prefs.FN_KEY, credential.getGivenName());
                        b.putString(Prefs.LN_KEY, credential.getFamilyName());
                        b.putString(Prefs.PHONE_KEY, credential.getPhoneNumber());
                        intent = new Intent(frmSplash.this, frmAccount.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to retrieve information. Please try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (ApiException e) {
                    Toast.makeText(frmSplash.this, "Failed to authenticate account. Please try again", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void validateLogin(String emailAdd){
        String reqParam = "/email?emailAddress=".concat(emailAdd);
        VolleyHttp volleyHttp = new VolleyHttp(reqParam, null, "user", frmSplash.this);
        String response = volleyHttp.getResponseBody();
        JSONObject reader = null;
        try {
            reader = new JSONObject(response);
            int responseStatus = reader.getInt("status");
            if (responseStatus == 200) {
                JSONObject userObject = reader.getJSONObject("data");
                Prefs.getInstance().setUser(frmSplash.this,Prefs.FN_KEY,userObject.getString(Prefs.FN_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.LN_KEY,userObject.getString(Prefs.LN_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.IMG_KEY,userObject.getString(Prefs.IMG_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.ADMIN_KEY,userObject.getBoolean(Prefs.ADMIN_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.BDAY_KEY,userObject.getString(Prefs.BDAY_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.WEIGHT_KEY,userObject.getString(Prefs.WEIGHT_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.HEIGHT_KEY,userObject.getString(Prefs.HEIGHT_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.PHONE_KEY,userObject.getString(Prefs.PHONE_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.ID_KEY,userObject.getString(Prefs.ID_KEY));
                Prefs.getInstance().setUser(frmSplash.this,Prefs.GENDER_KEY,userObject.getString(Prefs.GENDER_KEY));

                Map<String, Object> tokenMap = new HashMap<>();
                tokenMap.put(Prefs.FN_KEY,userObject.getString(Prefs.FN_KEY));
                tokenMap.put(Prefs.LN_KEY,userObject.getString(Prefs.LN_KEY));
                tokenMap.put(Prefs.IMG_KEY,userObject.getString(Prefs.IMG_KEY));
                tokenMap.put(Prefs.BDAY_KEY,userObject.getString(Prefs.BDAY_KEY));
                tokenMap.put(Prefs.WEIGHT_KEY,userObject.getString(Prefs.WEIGHT_KEY));
                tokenMap.put(Prefs.HEIGHT_KEY,userObject.getString(Prefs.HEIGHT_KEY));
                tokenMap.put(Prefs.PHONE_KEY,userObject.getString(Prefs.PHONE_KEY));
                tokenMap.put(Prefs.GENDER_KEY,userObject.getString(Prefs.GENDER_KEY));
                tokenMap.put(Prefs.EMAIL_KEY,userObject.getString(Prefs.EMAIL_KEY));

                VolleyHttp tokenHttp = new VolleyHttp("", tokenMap, "user", frmSplash.this);
                String responseToken = tokenHttp.getResponseBody();
                reader = new JSONObject(responseToken);
                JSONObject dataObject = reader.getJSONObject("data");
                String refToken = dataObject.getString("token");
                LoggedUser.getInstance().setRefreshToken(refToken);
                if(userObject.getBoolean("isAdmin") == false){
                    intent = new Intent(frmSplash.this, frmMain.class);
                } else{
                    intent = new Intent(frmSplash.this, AdminMainActivity.class);
                }
                startActivity(intent);
                finish();
            } else{
                throw new Exception("");
            }
        } catch (Exception e) {
            Log.d("Log_Padyak", "onCreate: " + e.getMessage());
            Toast.makeText(this, "Failed to authenticate account. Please try again", Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.INVISIBLE);
            textView2.setText("Please Sign-in with your Google Account");
            btnGAuth.setVisibility(View.VISIBLE);
        }
    }
}