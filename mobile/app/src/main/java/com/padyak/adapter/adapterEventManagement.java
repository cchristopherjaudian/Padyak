package com.padyak.adapter;

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
import com.padyak.dto.CalendarEvent;
import com.padyak.dto.Like;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class adapterEventManagement extends RecyclerView.Adapter<adapterEventManagement.viewHolder> {
    List<CalendarEvent> events;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
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
        holder.txEventTitle.setText(events.get(position).getEventName());
        LocalDate dateNow = LocalDate.parse(events.get(position).getEventDate());
        String startTime = Helper.getInstance().ISOtoTime(events.get(position).getEventStart()).replace("+08:00","");
        String endTime = Helper.getInstance().ISOtoTime(events.get(position).getEventEnd()).replace("+08:00","");
        holder.txEventDate.setText(dateNow.format(formatter).concat(" ").concat(startTime).concat("-").concat(endTime));
        Picasso.get().load(events.get(position).getEventImage()).into(holder.imgEventImage);
    }
    public String getChecked(){
        List<String> checkedNumbers = new ArrayList<>();
        events.forEach(c->{
            if(c.isIs_selected()) checkedNumbers.add(c.getEventId());
        });
        return String.join(",", checkedNumbers);

    }
    public void updateEventList(){
        Iterator<CalendarEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
            CalendarEvent c = iterator.next();
            if(c.isIs_selected()) iterator.remove();
        }

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return events.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txEventTitle,txEventDate;
        CheckBox chkSelect;
        ImageView imgEventImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txEventTitle = itemView.findViewById(R.id.txEventTitle);
            txEventDate = itemView.findViewById(R.id.txEventDate);
            chkSelect = itemView.findViewById(R.id.chkSelect);
            imgEventImage = itemView.findViewById(R.id.imgEventImage);

            chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    events.get(getAdapterPosition()).setIs_selected(isChecked);
                }
            });
        }
    }
}
