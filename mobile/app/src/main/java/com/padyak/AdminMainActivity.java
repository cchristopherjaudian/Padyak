package com.padyak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class AdminMainActivity extends AppCompatActivity {

    RelativeLayout rlEvents,rlAlert,rlTrack;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        rlEvents = findViewById(R.id.rlEvents);
        rlAlert = findViewById(R.id.rlAlert);
        rlTrack = findViewById(R.id.rlTrack);

        rlEvents.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this,frmEventCrud.class);
            startActivity(intent);
        });
        rlAlert.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this,frmAdminAlert.class);
            startActivity(intent);
        });
        rlTrack.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this,frmTrackEvent.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

    }
}