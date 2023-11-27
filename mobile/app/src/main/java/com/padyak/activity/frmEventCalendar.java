package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.padyak.R;
import com.padyak.dto.CalendarEvent;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class frmEventCalendar extends AppCompatActivity {
    ProgressDialog progressDialog;
    CardView cardProfile;
    CalendarView calendarView;
    Button btnViewEvent;
    TextView txSelectedDate, txCalendarEventTitle;
    ImageView imgDP;
    String appColor;
    List<EventDay> events;
    List<Calendar> calendars;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, CalendarEvent> calendarMap;
    String selectedCalendarDate;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_calendar);
        cardProfile = findViewById(R.id.cardProfile);
        calendarView = findViewById(R.id.calendarView);
        btnViewEvent = findViewById(R.id.btnViewEvent);
        txSelectedDate = findViewById(R.id.txSelectedDate);
        txCalendarEventTitle = findViewById(R.id.txCalendarEventTitle);
        appColor = getString(R.color.app_color);
        imgDP = findViewById(R.id.imgDP);


        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        txSelectedDate.setText(dateNow.format(formatter).toUpperCase());

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
        btnViewEvent.setOnClickListener((e) -> {
            if (selectedCalendarDate.isEmpty()) return;
            if (calendarMap.get(selectedCalendarDate) == null) return;

            Intent intent = new Intent(frmEventCalendar.this, frmEventInfo.class);
            Bundle bundle = new Bundle();
            bundle.putString("eventId", calendarMap.get(selectedCalendarDate).getEventId());
            intent.putExtras(bundle);
            startActivity(intent);
        });


        calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();
            String finalDate = Helper.getInstance().dateFormat(clickedDayCalendar.getTime());
            txSelectedDate.setText(finalDate);
            loadEvent(clickedDayCalendar.getTime());
        });
        calendarView.setOnPreviousPageChangeListener(this::loadCalendar);
        calendarView.setOnForwardPageChangeListener(this::loadCalendar);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);
        loadCalendar();

    }

    private void loadEvent(Date d) {
        Log.d("Log_Padyak", "Starting loadEvent");
        selectedCalendarDate = simpleDateFormat.format(d);
        if (calendarMap.containsKey(selectedCalendarDate)) {
            CalendarEvent findEvent = calendarMap.get(selectedCalendarDate);
            txCalendarEventTitle.setText(findEvent.getEventName());
        } else {
            txCalendarEventTitle.setText("No event registered");
        }
        Log.d("Log_Padyak", "Ending loadEvent");
    }

    private void loadCalendar() {
        Log.d("Log_Padyak", "Starting loadCalendar");
        progressDialog = Helper.getInstance().progressDialog(frmEventCalendar.this, "Retrieving events.");
        progressDialog.show();
        calendarView.setOnForwardPageChangeListener(null);
        calendarView.setOnPreviousPageChangeListener(null);
        calendarView.setEnabled(false);
        new Thread(() -> {
            events = new ArrayList<>();
            calendars = new ArrayList<>();
            calendarMap = new HashMap<>();

            String selectedYear = String.valueOf(calendarView.getCurrentPageDate().get(Calendar.YEAR));
            String monthName = Helper.getInstance().toTitleCase(Month.of(calendarView.getCurrentPageDate().get(Calendar.MONTH) + 1).toString().toLowerCase());
            String params = "?year=" + selectedYear + "&month=" + monthName;
            VolleyHttp volleyHttp = new VolleyHttp(params, null, "event", frmEventCalendar.this);
            String response = volleyHttp.getResponseBody(true);
            try {
                JSONObject responseJSON = new JSONObject(response);
                int statusCode = responseJSON.getInt("status");
                if (statusCode != 200) throw new JSONException("Status code" + statusCode);
                JSONArray eventArray = responseJSON.optJSONArray("data");
                for (int i = 0; i < eventArray.length(); i++) {
                    JSONObject eventObject = eventArray.getJSONObject(i);
                    Calendar calendar = Helper.getInstance().toCalendar(eventObject.getString("eventDate"));
                    calendars.add(calendar);
                    events.add(new EventDay(calendar, R.drawable.menuicon, Color.parseColor(appColor)));
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
                    calendarMap.put(eventObject.getString("eventDate"), calendarEvent);
                }
                runOnUiThread(() -> {
                    calendarView.setHighlightedDays(calendars);
                    calendarView.setEvents(events);
                    loadEvent(calendarView.getSelectedDates().get(0).getTime());
                    Log.d("Log_Padyak", "Ending loadCalendar");
                    if(progressDialog != null) progressDialog.dismiss();
                    calendarView.setEnabled(true);
                    calendarView.setOnForwardPageChangeListener(this::loadCalendar);
                    calendarView.setOnPreviousPageChangeListener(this::loadCalendar);
                });
            } catch (JSONException e) {
                Log.d(Helper.getInstance().log_code, "loadCalendar: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Failed to retrieve list of events", Toast.LENGTH_LONG).show());
                runOnUiThread(() -> progressDialog.dismiss());

            }
        }).start();
    }
}