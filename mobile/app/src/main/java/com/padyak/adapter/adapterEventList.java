package com.padyak.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.activity.frmEventCalendar;
import com.padyak.activity.frmEventInfo;
import com.padyak.dto.CalendarEvent;
import com.padyak.utility.Helper;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class adapterEventList extends RecyclerView.Adapter<adapterEventList.viewHolder> {
    List<CalendarEvent> events;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    public adapterEventList(List<CalendarEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public adapterEventList.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event_list, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterEventList.viewHolder holder, int position) {
        holder.txEventTitle.setText(events.get(position).getEventName());
        LocalDate dateNow = LocalDate.parse(events.get(position).getEventDate());
        String startTime = Helper.getInstance().ISOtoTime(events.get(position).getEventStart()).replace("+08:00", "");
        String endTime = Helper.getInstance().ISOtoTime(events.get(position).getEventEnd()).replace("+08:00", "");
        holder.txEventDate.setText(dateNow.format(formatter).concat(" ").concat(startTime).concat("-").concat(endTime));
        Picasso.get().load(events.get(position).getEventImage()).into(holder.imgEventImage);
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txEventTitle, txEventDate;
        ImageView imgEventImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txEventTitle = itemView.findViewById(R.id.txEventTitle);
            txEventDate = itemView.findViewById(R.id.txEventDate);
            imgEventImage = itemView.findViewById(R.id.imgEventImage);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), frmEventInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("eventId", events.get(getAdapterPosition()).getEventId());
                intent.putExtras(bundle);
                (itemView.getContext()).startActivity(intent);
            });
        }
    }
}
