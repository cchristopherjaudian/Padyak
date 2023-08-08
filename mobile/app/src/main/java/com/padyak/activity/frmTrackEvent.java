package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.padyak.R;

public class frmTrackEvent extends AppCompatActivity {

    Button btnTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_track_event);
        btnTrack = findViewById(R.id.btnTrack);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmTrackEvent.this, frmStartTrack.class );
                startActivity(intent);
                finish();
            }
        });
    }
}