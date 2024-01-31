package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.adapter.adapterCurrentEvent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.dto.CalendarEvent;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class frmTrackEvent extends AppCompatActivity {
    RecyclerView rvEventsNow;
    ImageView imgDP;
    TextView textView19;
    String eventId, eventName, eventPhoto;
    LinearLayoutManager linearLayoutManager;
    adapterCurrentEvent adapterCurrentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_track_event);
        imgDP = findViewById(R.id.imgDP);
        rvEventsNow = findViewById(R.id.rvEventsNow);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEventsNow.setLayoutManager(linearLayoutManager);
        textView19 = findViewById(R.id.textView19);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);
        textView19.setText(eventName);
        loadEventsNow();
    }

    public void loadEventsNow() {
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(frmTrackEvent.this, "Loading current events.");
        progressDialog.show();
        new Thread(() -> {
            VolleyHttp volleyHttp = new VolleyHttp("/now", null, "event", frmTrackEvent.this);
            String response = volleyHttp.getResponseBody(true);
            runOnUiThread(() -> {
                List<CalendarEvent> calendarEventList = new ArrayList<>();
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    int responseCode = responseJSON.getInt("status");
                    if (responseCode != 200)
                        throw new JSONException("Response Code: " + responseCode);
                    JSONArray eventArray = responseJSON.optJSONArray("data");
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject eventObject = eventArray.getJSONObject(i);
                        CalendarEvent calendarEvent = new CalendarEvent();
                        calendarEvent.setEventId(eventObject.getString("id"));
                        calendarEvent.setEventName(eventObject.getString("name"));
                        calendarEvent.setEventAward(eventObject.getString("award"));
                        calendarEvent.setEventDate(eventObject.getString("eventDate"));
                        calendarEvent.setEventImage(eventObject.getString("photoUrl"));
                        calendarEvent.setEventDescription(eventObject.getString("eventDescription"));
                        calendarEvent.setEventStart(eventObject.getString("startTime"));
                        calendarEvent.setEventEnd(eventObject.getString("endTime"));
                        calendarEvent.setIs_done(false);
                        calendarEvent.setEventRegistrar(null);
                        calendarEvent.setIs_selected(false);
                        calendarEventList.add(calendarEvent);
                    }
                    adapterCurrentEvent = new adapterCurrentEvent(calendarEventList);
                    rvEventsNow.setAdapter(adapterCurrentEvent);
                } catch (JSONException e) {
                    Toast.makeText(this, "Failed to retrieve current events. Please try again.", Toast.LENGTH_LONG).show();
                    Log.d(Helper.getInstance().log_code, "loadEventsNow: " + e.getMessage());
                } finally {
                    progressDialog.dismiss();
                }


            });
        }).start();


    }

}