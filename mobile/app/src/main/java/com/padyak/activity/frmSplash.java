package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
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

        btnGAuth.setOnClickListener(v->{
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(frmSplash.this, new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            try {

                                startIntentSenderForResult(
                                        result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                        null, 0, 0, 0);
                            } catch (IntentSender.SendIntentException e) {
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
        if(LoggedUser.getInstance().getFirstName().equals("")){
            spinner.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            btnGAuth.setVisibility(View.VISIBLE);
        } else{
            spinner.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            btnGAuth.setVisibility(View.GONE);
            intent = new Intent(frmSplash.this,frmMain.class);
            startActivity(intent);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    System.out.println(credential);
                    String idToken = credential.getGoogleIdToken();
                    Prefs.getInstance().setUser(this,"uuid",credential.getId());
                    Prefs.getInstance().setUser(this,"imgUrl",credential.getProfilePictureUri().toString());
                    Prefs.getInstance().setUser(this,"firstName",credential.getDisplayName());
                    Prefs.getInstance().setUser(this,"phoneNumber",credential.getPhoneNumber());
                    Prefs.getInstance().getUser(this);
                    intent = new Intent(frmSplash.this,frmMain.class);
                    startActivity(intent);

                } catch (ApiException e) {
                    Toast.makeText(frmSplash.this, "Failed to authenticate account. Please try again", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}