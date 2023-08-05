package com.padyak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class frmEventRegister extends AppCompatActivity {
    Button btnEventRegister, btnEventCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_register);

        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        btnEventCancel.setOnClickListener(v -> finish());
        btnEventRegister.setOnClickListener(v -> {
            Intent intent = new Intent(frmEventRegister.this, frmEventParticipants.class);
            startActivity(intent);
            frmEventInfo.frmEventInfo.finish();
            finish();
        });
    }
}