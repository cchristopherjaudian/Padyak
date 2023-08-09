package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.adapter.adapterEventManagement;
import com.padyak.dto.CalendarEvent;
import com.padyak.dto.MonthEvent;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class frmEventManagement extends AppCompatActivity {
    TextView frmEventMonth;
    int month;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvEventMonth;
    com.padyak.adapter.adapterEventManagement adapterEventManagement;
    List<CalendarEvent> calendarEvents;
    Button btnAddEvent, btnDeleteEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_management);
        frmEventMonth = findViewById(R.id.frmEventMonth);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        frmEventMonth = findViewById(R.id.frmEventMonth);

        rvEventMonth = findViewById(R.id.rvEventMonth);
        linearLayoutManager = new LinearLayoutManager(this);
        rvEventMonth.setLayoutManager(linearLayoutManager);

        month = getIntent().getIntExtra("month", 0);
        frmEventMonth.setText(Month.of(month).name());

        btnAddEvent.setOnClickListener(v->{
            Intent intent = new Intent(frmEventManagement.this,frmAddEvent.class);
            startActivity(intent);
        });
        loadEvents();
    }

    public void loadEvents() {
        calendarEvents = new ArrayList<>();
        calendarEvents.add(new CalendarEvent());
        calendarEvents.add(new CalendarEvent());
        calendarEvents.add(new CalendarEvent());

        adapterEventManagement = new adapterEventManagement(calendarEvents);
        rvEventMonth.setAdapter(adapterEventManagement);
    }
}