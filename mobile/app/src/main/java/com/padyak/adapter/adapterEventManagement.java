package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.CalendarEvent;

import java.util.List;

public class adapterEventManagement extends RecyclerView.Adapter<adapterEventManagement.viewHolder> {
    List<CalendarEvent> events;

    public adapterEventManagement(List<CalendarEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public adapterEventManagement.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event_management,parent,false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterEventManagement.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
