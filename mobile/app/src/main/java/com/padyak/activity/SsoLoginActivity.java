package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class SsoLoginActivity extends AppCompatActivity {
    Button btnNewLogin;
    //com.google.android.gms.common.SignInButton btnSSO;
    Button btnSSO;
    TextView txForgot, txSignup;
    EditText txMobileNumber, txMobilePassword;
    String mobileNumber, mobilePassword;
    ProgressDialog progressDialog;
    ImageView imgShowPassword;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;
    public static SsoLoginActivity sso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sso_login);
        sso = this;

        imgShowPassword = findViewById(R.id.imgShowPassword);
        btnNewLogin = findViewById(R.id.btnNewLogin);
        btnSSO = findViewById(R.id.btnSSO);

        txForgot = findViewById(R.id.txForgot);
        txSignup = findViewById(R.id.txSignup);

        txMobileNumber = findViewById(R.id.txMobileNumber);
        txMobilePassword = findViewById(R.id.txMobilePassword);

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

        btnNewLogin.setOnClickListener(v -> {
            mobileNumber = txMobileNumber.getText().toString().trim();
            mobilePassword = txMobilePassword.getText().toString().trim();
            if (mobileNumber.equals("") || mobilePassword.equals("")) {
                Toast.makeText(this, "Please enter a valid credential.", Toast.LENGTH_LONG).show();
                return;
            }
            authInApp(mobileNumber, mobilePassword);
        });
        btnSSO.setOnClickListener(v -> {

            btnSSO.setEnabled(false);
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(SsoLoginActivity.this, new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            try {

                                startIntentSenderForResult(
                                        result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                        null, 0, 0, 0);
                            } catch (IntentSender.SendIntentException e) {

                            }
                        }
                    })
                    .addOnFailureListener(SsoLoginActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            btnSSO.setEnabled(true);
                            Toast.makeText(SsoLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
        txSignup.setOnClickListener(v->{
            Intent intent = new Intent(SsoLoginActivity.this,frmAccount.class);
            Bundle b = new Bundle();
            b.putBoolean("forgot",false);
            b.putString("source","IN_APP");
            intent.putExtras(b);
            startActivity(intent);
        });
        txForgot.setOnClickListener(v->{
            Intent intent = new Intent(SsoLoginActivity.this,InAppActivity.class);
            Bundle b = new Bundle();
            b.putBoolean("forgot",true);
            intent.putExtras(b);
            startActivity(intent);
        });
        imgShowPassword.setOnClickListener(v->{
            if(txMobilePassword.getInputType() == 129){
                txMobilePassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else{
                txMobilePassword.setInputType(129);
            }
        });
        Prefs.getInstance().getUser(this);
        Log.d(Helper.getInstance().log_code, "SP LoggedUser: " + LoggedUser.getInstance().toString());
        if (LoggedUser.getInstance().getAuth().equals("SSO")) {
            validateLogin();
        } else if(LoggedUser.getInstance().getAuth().equals("IN_APP")){
            String mobileNumber = LoggedUser.getInstance().getPhoneNumber();
            String password = LoggedUser.getInstance().getPassword();
            authInApp(mobileNumber,password);
        }

    }

    public void authInApp(String username, String password) {
        progressDialog = Helper.getInstance().progressDialog(SsoLoginActivity.this, "Authenticating.");
        progressDialog.show();
        new Thread(() -> {
            Map<String, Object> payload = new HashMap<>();
            payload.put("contactNumber", username);
            payload.put("password", password);
            payload.put("source", "IN_APP");

            try {
                VolleyHttp volleyHttp = new VolleyHttp("/sso/auth?contact=".concat(URLEncoder.encode(username, "UTF-8")).concat("&password=").concat(URLEncoder.encode(password, "UTF-8")), null, "user", SsoLoginActivity.this);
                String responseToken = volleyHttp.getResponseBody(false);
                runOnUiThread(()->{
                    if(responseToken.startsWith("40")){
                        progressDialog.dismiss();
                        Toast.makeText(SsoLoginActivity.this, "Invalid username/password. Please try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                });

                JSONObject reader = new JSONObject(responseToken);
                JSONObject dataObject = reader.getJSONObject("data");

                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    if (reader == null) {
                        Toast.makeText(this, "Failed to authenticate account. Please try again", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if(reader.getInt("status") == 400){
                                Toast.makeText(SsoLoginActivity.this, "Invalid username/password. Please try again.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            JSONObject userObject = dataObject.getJSONObject("user");
                            String refToken = dataObject.getString("token");
                            LoggedUser.getInstance().setRefreshToken(refToken);
                            Prefs.getInstance().setUser(SsoLoginActivity.this,Prefs.IMG_KEY,userObject.getString("photoUrl"));
                            Intent intent;
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.PASSWORD_KEY,password);
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.FN_KEY, userObject.getString(Prefs.FN_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.LN_KEY, userObject.getString(Prefs.LN_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.IMG_KEY, userObject.getString(Prefs.IMG_KEY));

                            if(userObject.has("isAdmin")){
                                if(userObject.getBoolean("isAdmin") == false){
                                    intent = new Intent(SsoLoginActivity.this, frmMain.class);
                                } else{
                                    intent = new Intent(SsoLoginActivity.this, AdminMainActivity.class);
                                }
                                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.ADMIN_KEY, userObject.getBoolean(Prefs.ADMIN_KEY));
                            } else{
                                intent = new Intent(SsoLoginActivity.this, frmMain.class);
                            }

                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.BDAY_KEY, userObject.getString(Prefs.BDAY_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.WEIGHT_KEY, userObject.getString(Prefs.WEIGHT_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.HEIGHT_KEY, userObject.getString(Prefs.HEIGHT_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.PHONE_KEY, userObject.getString(Prefs.PHONE_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.ID_KEY, userObject.getString(Prefs.ID_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.GENDER_KEY, userObject.getString(Prefs.GENDER_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.EMAIL_KEY, userObject.getString(Prefs.EMAIL_KEY));
                            Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.AUTH, "IN_APP");
                            Log.d(Helper.getInstance().log_code, "authInApp LoggedUser: " +LoggedUser.getLoggedUser().toString());
                            Log.d(Helper.getInstance().log_code, "authInApp LoggedUser: " +LoggedUser.getLoggedUser().toString());
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Log.d(Helper.getInstance().log_code, "authInApp: " + e.getMessage());
                        }
                    }
                });
            } catch (UnsupportedEncodingException | JSONException e) {
                Log.d(Helper.getInstance().log_code, "authInApp: " + e.getMessage());
            }
        }).start();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    checkEmail(credential);
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

    private void checkEmail(SignInCredential credential) {
        try {
            Intent intent;

            String reqParam = "/email?emailAddress=".concat(credential.getId());
            VolleyHttp volleyHttp = new VolleyHttp(reqParam, null, "user", SsoLoginActivity.this);
            String response = volleyHttp.getResponseBody(false);
            if(response.startsWith("40")){
                Bundle b = new Bundle();
                b.putString(Prefs.IMG_KEY, credential.getProfilePictureUri().toString());
                b.putString(Prefs.EMAIL_KEY, credential.getId());
                b.putString(Prefs.FN_KEY, credential.getGivenName());
                b.putString(Prefs.LN_KEY, credential.getFamilyName());
                b.putString(Prefs.PHONE_KEY, credential.getPhoneNumber());
                b.putString("source", "SSO");
                intent = new Intent(SsoLoginActivity.this, frmAccount.class);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                return;
            }
            JSONObject reader = new JSONObject(response);;
            int responseStatus = reader.getInt("status");
            Log.d(Helper.getInstance().log_code, "checkEmail: " + response);
            if (responseStatus == 200) {
                JSONObject userObject = reader.getJSONObject("data");
                JSONObject dataObject = userObject.getJSONObject("user");
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.FN_KEY, dataObject.getString(Prefs.FN_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.LN_KEY, dataObject.getString(Prefs.LN_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.IMG_KEY, dataObject.getString(Prefs.IMG_KEY));
                //Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.ADMIN_KEY, dataObject.getBoolean(Prefs.ADMIN_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.BDAY_KEY, dataObject.getString(Prefs.BDAY_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.WEIGHT_KEY, dataObject.getString(Prefs.WEIGHT_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.HEIGHT_KEY, dataObject.getString(Prefs.HEIGHT_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.PHONE_KEY, dataObject.getString(Prefs.PHONE_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.ID_KEY, dataObject.getString(Prefs.ID_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.GENDER_KEY, dataObject.getString(Prefs.GENDER_KEY));
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.EMAIL_KEY, credential.getId());
                Prefs.getInstance().setUser(SsoLoginActivity.this, Prefs.AUTH, "SSO");
                validateLogin();
            } else if (responseStatus == 404 || responseStatus == 400) {
                Bundle b = new Bundle();
                b.putString(Prefs.IMG_KEY, credential.getProfilePictureUri().toString());
                b.putString(Prefs.EMAIL_KEY, credential.getId());
                b.putString(Prefs.FN_KEY, credential.getGivenName());
                b.putString(Prefs.LN_KEY, credential.getFamilyName());
                b.putString(Prefs.PHONE_KEY, credential.getPhoneNumber());
                b.putString("source", "SSO");
                intent = new Intent(SsoLoginActivity.this, frmAccount.class);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.d(Helper.getInstance().log_code, "checkEmail: " + e.getMessage());
        } finally {
            btnSSO.setEnabled(true);
        }
    }

    private void validateLogin() {
        progressDialog = Helper.getInstance().progressDialog(SsoLoginActivity.this, "Authenticating.");
        progressDialog.show();
        new Thread(() -> {
            Intent intent;

            try {
                Map<String, Object> tokenMap = new HashMap<>();
                tokenMap.put(Prefs.FN_KEY, LoggedUser.getInstance().getFirstName());
                tokenMap.put(Prefs.LN_KEY, LoggedUser.getInstance().getLastName());
                tokenMap.put(Prefs.IMG_KEY, LoggedUser.getInstance().getImgUrl());
                tokenMap.put(Prefs.BDAY_KEY, LoggedUser.getInstance().getBirthDate());
                tokenMap.put(Prefs.WEIGHT_KEY, LoggedUser.getInstance().getWeight());
                tokenMap.put(Prefs.HEIGHT_KEY, LoggedUser.getInstance().getHeight());
                tokenMap.put(Prefs.PHONE_KEY, LoggedUser.getInstance().getPhoneNumber());
                tokenMap.put(Prefs.GENDER_KEY, LoggedUser.getInstance().getGender());
                tokenMap.put(Prefs.EMAIL_KEY, LoggedUser.getInstance().getEmail());
                tokenMap.put("source", "SSO");
                String requestPath = (LoggedUser.getInstance().isIs_admin()) ? "admin" : "user";

                VolleyHttp tokenHttp = new VolleyHttp("/sso/auth", tokenMap, requestPath, SsoLoginActivity.this);
                String responseToken = tokenHttp.getResponseBody(false);
                JSONObject reader = new JSONObject(responseToken);
                int responseStatus = reader.getInt("status");
                if (responseStatus == 200) {
                    JSONObject dataObject = reader.getJSONObject("data");
                    JSONObject userObject = dataObject.getJSONObject("user");
                    String refToken = dataObject.getString("token");
                    LoggedUser.getInstance().setRefreshToken(refToken);
                    Log.d(Helper.getInstance().log_code, "validateLogin: " + LoggedUser.getInstance().toString());
                    Prefs.getInstance().setUser(SsoLoginActivity.this,Prefs.IMG_KEY,userObject.getString("photoUrl"));
                    Prefs.getInstance().setUser(SsoLoginActivity.this,Prefs.ADMIN_KEY,false);
                    if(userObject.has("isAdmin")){
                        if(!userObject.getBoolean("isAdmin")){
                            intent = new Intent(SsoLoginActivity.this, frmMain.class);
                        } else{
                            if(userObject.getBoolean("isAdmin") == false){
                                intent = new Intent(SsoLoginActivity.this, frmMain.class);
                            } else{
                                intent = new Intent(SsoLoginActivity.this, AdminMainActivity.class);
                            }
                        }
                        Prefs.getInstance().setUser(SsoLoginActivity.this,Prefs.IMG_KEY, userObject.getBoolean("isAdmin"));
                    } else{
                        intent = new Intent(SsoLoginActivity.this, frmMain.class);
                    }
                    Log.d(Helper.getInstance().log_code, "validateLogin Admin: " + LoggedUser.getInstance().isIs_admin());


                    startActivity(intent);
                    finish();
                }

            } catch (Exception e) {
                Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                Toast.makeText(this, "Failed to authenticate account. Please try again", Toast.LENGTH_LONG).show();
            } finally {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                });
            }
        }).start();

    }
}