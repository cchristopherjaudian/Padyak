package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;

import java.util.concurrent.TimeUnit;

public class frmStarted extends AppCompatActivity {
    Button btnGetStarted,btnAgree;
    FrameLayout frameGetStarted,frameTerms;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_started);
        intent = new Intent(frmStarted.this,SsoLoginActivity.class);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        frameGetStarted = findViewById(R.id.frameGetStarted);

        btnAgree = findViewById(R.id.btnAgree);
        frameTerms = findViewById(R.id.frameTerms);

        btnGetStarted.setOnClickListener(v->{
            frameGetStarted.setVisibility(View.GONE);
            frameTerms.setVisibility(View.VISIBLE);
        });
        btnAgree.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else{
                Prefs.getInstance().setUser(frmStarted.this, Prefs.AUTH, "G");
                startActivity(intent);
                finish();
            }

        });

        Prefs.getInstance().getUser(this);
        Log.d(Helper.getInstance().log_code, "onCreate: " + LoggedUser.getLoggedUser().toString());
        if(!LoggedUser.getInstance().getAuth().equals("")){
            startActivity(intent);
            finish();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(Helper.getInstance().log_code, "onRequestPermissionsResult: 1");
        if(requestCode == 1){
            Log.d(Helper.getInstance().log_code, "onRequestPermissionsResult: 2");
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_DENIED) {
                Prefs.getInstance().setUser(frmStarted.this, Prefs.AUTH, "G");
                startActivity(intent);
            }
            finish();
        }

    }
    @Override
    public void onBackPressed() {

    }
}