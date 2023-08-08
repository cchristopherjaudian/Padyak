package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;

import java.util.List;

public class adapterYouMayKnow extends RecyclerView.Adapter<adapterYouMayKnow.viewHolder> {
    List<String> _name,_id;
    List<Integer> _mutual;

    public adapterYouMayKnow(List<String> _name, List<String> _id, List<Integer> _mutual) {
        this._name = _name;
        this._id = _id;
        this._mutual = _mutual;
    }

    @NonNull
    @Override
    public adapterYouMayKnow.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_youmayknow,parent,false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterYouMayKnow.viewHolder holder, int position) {
        holder.txAthleteName.setText(_name.get(position));
        holder.txMutual.setText(String.format("%d Mutual Friend(s)",_mutual.get(position)));
    }

    @Override
    public int getItemCount() {
        return _name.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txAthleteName,txMutual;
        Button btnFollow;
        ImageView imgAthleteDP;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txAthleteName = itemView.findViewById(R.id.txAthleteName);
            txMutual = itemView.findViewById(R.id.txMutual);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            imgAthleteDP = itemView.findViewById(R.id.imgAthleteDP);
        }
    }
}
