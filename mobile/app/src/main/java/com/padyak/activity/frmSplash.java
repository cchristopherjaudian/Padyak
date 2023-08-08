package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import com.padyak.R;
import com.padyak.utility.LoggedUser;

public class frmSplash extends AppCompatActivity {
    ProgressBar spinner;
    Intent intent;
    Button btnAdmin, btnUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_splash);
        LoggedUser.is_admin = true;
        spinner = findViewById(R.id.progressBar);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnUser = findViewById(R.id.btnUser);
        spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
//        new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                runOnUiThread(() -> {
//                            intent = (LoggedUser.is_admin) ? new Intent(frmSplash.this, AdminMainActivity.class) : new Intent(frmSplash.this, frmMain.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                );
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        }).start();

        btnAdmin.setOnClickListener(v->{
            LoggedUser.is_admin = true;
            intent = (LoggedUser.is_admin) ? new Intent(frmSplash.this, AdminMainActivity.class) : new Intent(frmSplash.this, frmMain.class);
            startActivity(intent);
            finish();
        });

        btnUser.setOnClickListener(v->{
            LoggedUser.is_admin = false;
            intent = (LoggedUser.is_admin) ? new Intent(frmSplash.this, AdminMainActivity.class) : new Intent(frmSplash.this, frmMain.class);
            startActivity(intent);
            finish();
        });
    }
}