package com.padyak.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.activity.frmEventInfo;
import com.padyak.activity.frmParticipate;
import com.padyak.dto.CalendarEvent;
import com.padyak.utility.Constants;
import com.padyak.utility.Helper;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class adapterCurrentEvent extends RecyclerView.Adapter<adapterCurrentEvent.viewHolder> {
    List<CalendarEvent> events;
    String[] eventColors = new String[]{"#c0392b", "#2980b9", "#16a085", "#8e44ad", "#f1c40f", "#7f8c8d"};
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    public adapterCurrentEvent(List<CalendarEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public adapterCurrentEvent.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event_list, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterCurrentEvent.viewHolder holder, int position) {
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
                int adapterIndex = getAdapterPosition();
                while (adapterIndex > 5) {
                    adapterIndex -= 5;
                }
                Intent intent = new Intent(itemView.getContext(), frmParticipate.class);
                Bundle bundle = new Bundle();
                bundle.putString("eventId", events.get(getAdapterPosition()).getEventId());
                bundle.putString("eventName", events.get(getAdapterPosition()).getEventName());
                bundle.putString("eventColor", Constants.eventColors.get(adapterIndex));
                intent.putExtras(bundle);
                (itemView.getContext()).startActivity(intent);

            });
        }
    }
}
