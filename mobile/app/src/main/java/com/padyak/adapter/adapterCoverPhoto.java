package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.CoverPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterCoverPhoto extends RecyclerView.Adapter<adapterCoverPhoto.viewHolder> {
    List<CoverPhoto> imageList;

    public adapterCoverPhoto(List<CoverPhoto> imageList) {
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
        if(imageList.get(position).getImageURL().trim().equals("n/a")){
            holder.img.setBackgroundResource(R.drawable.bike1);
        } else{
            Picasso.get().load(imageList.get(position).getImageURL().trim()).into(holder.img);
        }

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
