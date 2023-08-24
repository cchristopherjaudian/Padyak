package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class frmTrackEvent extends AppCompatActivity {

    Button btnTrack;
    ImageView imgDP,imgTrackEvent;
    TextView textView19;
    String eventId,eventName,eventPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_track_event);
        btnTrack = findViewById(R.id.btnTrack);
        imgDP = findViewById(R.id.imgDP);
        imgTrackEvent = findViewById(R.id.imgTrackEvent);
        textView19 = findViewById(R.id.textView19);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);

        eventId = getIntent().getStringExtra("id");
        eventName = getIntent().getStringExtra("name");
        eventPhoto = getIntent().getStringExtra("photoUrl");

        textView19.setText(eventName);
        Picasso.get().load(eventPhoto).into(imgTrackEvent);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventId.isEmpty()) return;
                Bundle b = new Bundle();
                b.putString("eventId",eventId);
                b.putString("eventName",eventName);
                Intent intent = new Intent(frmTrackEvent.this, frmParticipate.class);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

    }


}