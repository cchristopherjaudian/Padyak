package com.padyak.activity;
import com.padyak.adapter.adapterEventList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.padyak.R;
import com.padyak.dto.CalendarEvent;

import java.lang.reflect.Type;
import java.util.List;

public class EventListActivity extends AppCompatActivity {
    RecyclerView rvEventList;
    LinearLayoutManager linearLayoutManager;
    adapterEventList adapterEventList;

    String filtererListJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        filtererListJson = getIntent().getStringExtra("filtererListJson");
        rvEventList = findViewById(R.id.rvEventList);
        rvEventList.setLayoutManager(linearLayoutManager);
        loadEvents();
    }
    public void loadEvents(){
        if(filtererListJson.isEmpty()) return;

        Gson gson = new Gson();
        Type type = new TypeToken<List<CalendarEvent>>(){}.getType();
        List<CalendarEvent> calendarEventList = gson.fromJson(filtererListJson, type);
        adapterEventList = new adapterEventList(calendarEventList);
        rvEventList.setAdapter(adapterEventList);

    }
}