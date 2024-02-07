package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.adapter.adapterEventManagement;
import com.padyak.adapter.adapterEventValidation;
import com.padyak.dto.CalendarEvent;
import com.padyak.utility.AdminHelper;
import com.padyak.utility.Helper;
import com.padyak.utility.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventPaymentActivity extends AppCompatActivity {
    RecyclerView rvValidation;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterEventValidation adapterEventValidation;
    List<CalendarEvent> calendarEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_payment);

        rvValidation = findViewById(R.id.rvValidation);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvValidation.setLayoutManager(linearLayoutManager);

        loadEvents();
    }
    public void loadEvents(){
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        String year_value = String.valueOf(cal.get(Calendar.YEAR));

        ProgressDialog progressDialog = Helper.getInstance().progressDialog(EventPaymentActivity.this,"Retrieving events.");
        progressDialog.show();

        new Thread(()->{
            String params = "/?year=" + LocalDate.now().getYear() + "&month=" + month_name;
            VolleyHttp volleyHttp = new VolleyHttp(params,null,"event",EventPaymentActivity.this);
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
                        adapterEventValidation = new com.padyak.adapter.adapterEventValidation(calendarEvents);
                        rvValidation.setAdapter(adapterEventValidation);
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
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            try {
                AdminHelper.getInstance().showMessageAlert(getSupportFragmentManager(),message);
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "onReceive: " + e.getMessage());
                Log.d(Helper.getInstance().log_code, "onReceive: " + message);
            }
        }
    };
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("FCMIntentService"));
    }
}