package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.adapter.adapterParticipant;
import com.padyak.dto.CalendarEvent;
import com.padyak.dto.Participants;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class frmEventInfo extends AppCompatActivity {

    ImageView imgEvent;
    String eventId;
    Button btnEventRegister, btnEventCancel;
    public static frmEventInfo frmEventInfo;
    CalendarEvent calendarEvent;

    boolean is_done,is_registered;
    String eventName,eventPhoto;
    TextView txEventInfoName,txEventInfoDesc,txEventInfoDate,txEventAward,textView20;
    RecyclerView rvParticipants;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterParticipant adapterParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_info);
        frmEventInfo = this;
        eventId = getIntent().getStringExtra("eventId");
        imgEvent = findViewById(R.id.imgEvent);

        rvParticipants = findViewById(R.id.rvParticipants);
        linearLayoutManager = new LinearLayoutManager(this);
        rvParticipants.setLayoutManager(linearLayoutManager);

        textView20 = findViewById(R.id.textView20);
        txEventInfoName = findViewById(R.id.txEventInfoName);
        txEventInfoDesc = findViewById(R.id.txEventInfoDesc);
        txEventInfoDate = findViewById(R.id.txEventInfoDate);
        txEventAward = findViewById(R.id.txEventAward);
        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);

        btnEventCancel.setOnClickListener(v -> finish());

        btnEventRegister.setOnClickListener((e) -> {

            if (is_done) {
                Toast.makeText(frmEventInfo, "This event has already been completed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Intent intent = new Intent(frmEventInfo.this, frmEventRegister.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",eventId);
                bundle.putString("name",eventName);
                bundle.putString("photoUrl",eventPhoto);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            runOnUiThread(this::loadEventInfo);
        }).start();
    }

    public void loadEventInfo() {
        try {
            if (eventId.isEmpty()) throw new Exception("eventId is null");

            VolleyHttp volleyHttp = new VolleyHttp("/".concat(eventId), null, "event", com.padyak.activity.frmEventInfo.this);
            JSONObject responseJSON = volleyHttp.getJsonResponse(true);
            if (responseJSON == null) throw new Exception("responseJSON is null");
            JSONObject eventObject = responseJSON.getJSONObject("data");
            is_registered = false;
            is_done = false;
            String eventDate = eventObject.getString("eventDate");
            String eventTime = eventObject.getString("startTime").concat("-").concat(eventObject.getString("endTime"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            LocalDate ldDate = LocalDate.parse(eventDate);
            eventTime = formatter.format(ldDate).concat(" ").concat(eventTime);

            JSONArray participantJSON = eventObject.optJSONArray("registeredUser");
            List<Participants> participantsList = new ArrayList<>();
            for(int i = 0; i < participantJSON.length(); i++){
                JSONObject participantObject = participantJSON.getJSONObject(i).getJSONObject("user");
                Participants participants = new Participants();
                participants.setUserImage(participantObject.getString("photoUrl"));
                participants.setUserName(participantObject.getString("firstname").concat(" ").concat(participantObject.getString("lastname")));
                participantsList.add(participants);

                if(participantObject.getString("id").equals(LoggedUser.getInstance().getUuid())) is_registered = true;
            }
            eventName = eventObject.getString("name");
            eventPhoto = eventObject.getString("photoUrl");

            is_done = eventObject.getBoolean("isDone");
            txEventInfoName.setText(eventName);
            txEventInfoDesc.setText(eventObject.getString("eventDescription"));
            txEventAward.setText(eventObject.getString("award"));
            txEventInfoDate.setText(eventTime);
            Picasso.get().load(eventPhoto).into(imgEvent);

            if(is_registered || is_done){
                textView20.setVisibility(View.VISIBLE);
                rvParticipants.setVisibility(View.VISIBLE);
                adapterParticipant = new adapterParticipant(participantsList);
                rvParticipants.setAdapter(adapterParticipant);

            } else{
                btnEventRegister.setVisibility(View.VISIBLE);
            }
        } catch (Exception err) {
            Log.d(Helper.getInstance().log_code, "loadEventInfo: " + err.getMessage());
            Toast.makeText(frmEventInfo, "Failed to load event information. Please try again", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}