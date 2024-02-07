package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.utility.CyclistHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class ProfileActivity extends AppCompatActivity {
    ConstraintLayout clUserInfo;
    ImageView imgLoggedProfile;
    TextView txSignOut,txLoggedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgLoggedProfile = findViewById(R.id.imgLoggedProfile);
        txLoggedUser = findViewById(R.id.txLoggedUser);
        clUserInfo = findViewById(R.id.clUserInfo);
        txSignOut = findViewById(R.id.txSignOut);

        txSignOut.setOnClickListener(v->{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            Prefs.getInstance().setUser(ProfileActivity.this, Prefs.EMAIL_KEY, "");
            Prefs.getInstance().setUser(ProfileActivity.this, Prefs.AUTH, "");
            if(null != com.padyak.activity.frmMain.frmMain) com.padyak.activity.frmMain.frmMain.finish();
            if(null != AdminMainActivity.adminMainActivity) AdminMainActivity.adminMainActivity.finish();
            Intent intent = new Intent(ProfileActivity.this,SsoLoginActivity.class);
            startActivity(intent);
            finish();
        });
        clUserInfo.setOnClickListener(v->{
            Intent intent = new Intent(ProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        });


    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d(Helper.getInstance().log_code, "FCM_Message: " + message);
            try {
                CyclistHelper.getInstance().showMessageAlert(getSupportFragmentManager(),message);
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
        txLoggedUser.setText(LoggedUser.getInstance().getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()));
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgLoggedProfile);
    }
}