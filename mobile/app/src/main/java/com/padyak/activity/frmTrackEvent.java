package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.utility.LoggedUser;
import com.squareup.picasso.Picasso;

public class frmTrackEvent extends AppCompatActivity {

    Button btnTrack;
    ImageView imgDP,imgTrackEvent;
    TextView textView19;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_track_event);
        btnTrack = findViewById(R.id.btnTrack);
        imgDP = findViewById(R.id.imgDP);
        imgTrackEvent = findViewById(R.id.imgTrackEvent);
        textView19 = findViewById(R.id.textView19);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmTrackEvent.this, frmStartTrack.class );
                startActivity(intent);
                finish();
            }
        });
        new Thread(()->{
            runOnUiThread(this::loadCurrentEvent);
        }).start();
    }

    public void loadCurrentEvent(){

    }
}