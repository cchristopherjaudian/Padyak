package com.padyak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapterParticipant extends RecyclerView.Adapter<adapterParticipant.viewHolder>{
    List<String> _name, _distance;

    public adapterParticipant(List<String> _name, List<String> _distance) {
        this._name = _name;
        this._distance = _distance;
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
        holder.txRowName.setText(_name.get(position));
        holder.txRowDistance.setText(_distance.get(position));
    }

    @Override
    public int getItemCount() {
        return _name.size();
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
