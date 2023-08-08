package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;

import java.util.List;

public class adapterCoverPhoto extends RecyclerView.Adapter<adapterCoverPhoto.viewHolder> {
    List<Integer> imageList;

    public adapterCoverPhoto(List<Integer> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public adapterCoverPhoto.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_coverphoto,parent,false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterCoverPhoto.viewHolder holder, int position) {
        holder.img.setBackgroundResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCoverPhoto);
        }
    }
}
