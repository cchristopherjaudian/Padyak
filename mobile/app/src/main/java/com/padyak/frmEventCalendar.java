package com.padyak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class frmEventCalendar extends AppCompatActivity {

    CalendarView calendarView;
    Button btnViewEvent;

    List<EventDay> events = new ArrayList<>();
    List<Calendar> calendars = new ArrayList<>();

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_calendar);
        calendarView = findViewById(R.id.calendarView);
        btnViewEvent = findViewById(R.id.btnViewEvent);

        btnViewEvent.setOnClickListener((e)->{
            Intent intent = new Intent(frmEventCalendar.this,frmEventInfo.class);
            startActivity(intent);
        });


        for(int i = 0; i < 10; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,i);
            calendars.add(calendar);
            events.add(new EventDay(calendar, R.drawable.menuicon, Color.parseColor(getString(R.color.app_color))));
        }
        calendarView.setHighlightedDays(calendars);
        calendarView.setEvents(events);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Toast.makeText(frmEventCalendar.this, clickedDayCalendar.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}