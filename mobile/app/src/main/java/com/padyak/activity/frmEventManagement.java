package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.adapter.adapterEventManagement;
import com.padyak.dto.CalendarEvent;
import com.padyak.dto.MonthEvent;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frmEventManagement extends AppCompatActivity {
    TextView frmEventMonth;
    ImageView imgDP;
    int month;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvEventMonth;
    com.padyak.adapter.adapterEventManagement adapterEventManagement;
    List<CalendarEvent> calendarEvents;
    Button btnAddEvent, btnDeleteEvent;
    ProgressDialog progressDialog;
    CardView cardView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_management);
        cardView4 = findViewById(R.id.cardView4);
        frmEventMonth = findViewById(R.id.frmEventMonth);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        frmEventMonth = findViewById(R.id.frmEventMonth);
        imgDP = findViewById(R.id.imgDP);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);
        rvEventMonth = findViewById(R.id.rvEventMonth);
        linearLayoutManager = new LinearLayoutManager(this);
        rvEventMonth.setLayoutManager(linearLayoutManager);

        month = getIntent().getIntExtra("month", 0);
        frmEventMonth.setText(Month.of(month).name());
        cardView4.setOnClickListener(v->{
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        });
        btnAddEvent.setOnClickListener(v->{
            Intent intent = new Intent(frmEventManagement.this,frmAddEvent.class);
            Bundle bundle = new Bundle();
            bundle.putString("Month",Month.of(month).name());
            bundle.putString("Year", String.valueOf(LocalDate.now().getYear()));
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnDeleteEvent.setOnClickListener(v->{
            String selectedUsers = adapterEventManagement.getChecked();
            if(selectedUsers.isEmpty()) return;

            AlertDialog alertDialog = new AlertDialog.Builder(frmEventManagement.this).create();
            alertDialog.setTitle("Event Management");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to delete the selected event(s)?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No",(d,w)->{

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes",(d,w)->{
                progressDialog = Helper.getInstance().progressDialog(frmEventManagement.this,"Deleting event(s)");
                progressDialog.show();
                new Thread(()->{
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("ids",selectedUsers);

                    VolleyHttp volleyHttp = new VolleyHttp("",payload,"event-delete",frmEventManagement.this);
                    JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                    runOnUiThread(()->{
                        progressDialog.dismiss();
                        if(responseJSON == null){
                            Toast.makeText(this, "Failed to delete event(s). Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        adapterEventManagement.updateEventList();
                    });
                }).start();
            });
            alertDialog.show();


        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    public void loadEvents() {
        progressDialog = Helper.getInstance().progressDialog(frmEventManagement.this,"Retrieving events.");
        progressDialog.show();
        new Thread(()->{
            String params = "/?year=" + LocalDate.now().getYear() + "&month=" + Helper.getInstance().toTitleCase(Month.of(month).name());
            VolleyHttp volleyHttp = new VolleyHttp(params,null,"event",frmEventManagement.this);
            String response = volleyHttp.getResponseBody(true);
            runOnUiThread(()->{
                try {
                    progressDialog.dismiss();
                    JSONObject responseJSON = new JSONObject(response);
                    int responseCode = responseJSON.getInt("status");
                    if(responseCode != 200) throw new JSONException("Response Code: " + responseCode);
                    JSONArray eventArray = responseJSON.optJSONArray("data");
                    List<Boolean> is_selected = new ArrayList<>();
                    calendarEvents = new ArrayList<>();
                    for(int i = 0; i < eventArray.length(); i++){
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
                        calendarEvents.add(calendarEvent);
                    }
                    runOnUiThread(()->{
                        adapterEventManagement = new adapterEventManagement(calendarEvents);
                        rvEventMonth.setAdapter(adapterEventManagement);
                    });

                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "loadEvents: " + e.getMessage());
                    runOnUiThread(()->{
                        Toast.makeText(this, "Failed to load events. Please try again", Toast.LENGTH_LONG).show();
                        finish();
                    });

                }
            });
        }).start();


    }
}