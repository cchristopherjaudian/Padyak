package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.padyak.R;

public class frmStarted extends AppCompatActivity {
    Button btnGetStarted,btnAgree;
    FrameLayout frameGetStarted,frameTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_started);

        btnGetStarted = findViewById(R.id.btnGetStarted);
        frameGetStarted = findViewById(R.id.frameGetStarted);

        btnAgree = findViewById(R.id.btnAgree);
        frameTerms = findViewById(R.id.frameTerms);

        btnGetStarted.setOnClickListener(v->{
            frameGetStarted.setVisibility(View.GONE);
            frameTerms.setVisibility(View.VISIBLE);
        });

    }
}