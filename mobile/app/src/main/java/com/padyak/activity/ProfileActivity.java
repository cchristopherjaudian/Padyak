package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onResume() {
        super.onResume();
        txLoggedUser.setText(LoggedUser.getInstance().getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()));
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgLoggedProfile);
    }
}