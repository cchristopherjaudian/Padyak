package com.padyak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class frmEventInfo extends AppCompatActivity {

    Button btnEventRegister, btnEventCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_info);

        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        btnEventCancel.setOnClickListener(v -> finish());

        btnEventRegister.setOnClickListener((e)->{
            Intent intent = new Intent(frmEventInfo.this, frmEventRegister.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {

    }
}