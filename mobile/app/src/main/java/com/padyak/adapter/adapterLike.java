package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.Comment;
import com.padyak.dto.Like;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterLike extends RecyclerView.Adapter<adapterLike.viewHolder> {


    List<Like> likeList;

    public adapterLike(List<Like> likeList) {
        this.likeList = likeList;
    }

    @NonNull
    @Override
    public adapterLike.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_like, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterLike.viewHolder holder, int position) {
        holder.txBottomLikeName.setText(likeList.get(position).getDisplayName());
        Picasso.get().load(likeList.get(position).getPhotoUrl()).into(holder.imgBottomLike);
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imgBottomLike;
        TextView txBottomLikeName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imgBottomLike = itemView.findViewById(R.id.imgBottomLike);
            txBottomLikeName = itemView.findViewById(R.id.txBottomLikeName);
        }

    }
}
