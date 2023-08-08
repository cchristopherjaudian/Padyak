package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.padyak.R;

public class frmAddEvent extends AppCompatActivity {

    Button btnEventRegister, btnEventCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_add_event);
        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        btnEventCancel.setOnClickListener(v-> finish());
    }
}