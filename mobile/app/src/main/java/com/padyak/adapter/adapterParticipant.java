package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.Participants;

import java.util.List;

public class adapterParticipant extends RecyclerView.Adapter<adapterParticipant.viewHolder>{
    List<Participants> participants;

    public adapterParticipant(List<Participants> participants) {
        this.participants = participants;
    }

    @NonNull
    @Override
    public adapterParticipant.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_participants,parent,false);
        adapterParticipant.viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterParticipant.viewHolder holder, int position) {
        holder.txRowNumber.setText(String.valueOf(position+1));
        holder.txRowName.setText(participants.get(position).getUserName());
        holder.txRowDistance.setText("");
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txRowNumber,txRowName,txRowDistance;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgParticipant);
            txRowNumber = itemView.findViewById(R.id.txRowNumber);
            txRowName = itemView.findViewById(R.id.txRowName);
            txRowDistance = itemView.findViewById(R.id.txRowDistance);
        }
    }
}
