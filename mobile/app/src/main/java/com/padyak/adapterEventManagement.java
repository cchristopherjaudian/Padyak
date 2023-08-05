package com.padyak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapterEventManagement extends RecyclerView.Adapter<adapterEventManagement.viewHolder> {
    List<Integer> imageList;

    public adapterEventManagement(List<Integer> imageList) {
        this.imageList = imageList;
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
        return imageList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
