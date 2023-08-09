package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.Newsfeed;

import java.util.List;

public class adapterNewsfeed extends RecyclerView.Adapter<adapterNewsfeed.viewHolder> {


    List<Newsfeed> newsfeeds;

    public adapterNewsfeed(List<Newsfeed> newsfeeds) {
        this.newsfeeds = newsfeeds;
    }

    @NonNull
    @Override
    public adapterNewsfeed.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_newsfeed,parent,false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterNewsfeed.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return newsfeeds.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
