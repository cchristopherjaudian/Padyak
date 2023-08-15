package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.Comment;
import com.padyak.dto.Like;
import com.padyak.dto.Newsfeed;
import com.padyak.fragment.fragmentComments;
import com.padyak.utility.LoggedUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterComment extends RecyclerView.Adapter<adapterComment.viewHolder> {


    List<Comment> commentList;


    public adapterComment(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public adapterComment.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterComment.viewHolder holder, int position) {
        holder.txBottomCommentName.setText(commentList.get(position).getDisplayName());
        holder.txBottomCommentCaption.setText(commentList.get(position).getComment());
        Picasso.get().load(commentList.get(position).getPhotoUrl()).into(holder.imgBottomComment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView  imgBottomComment;
        TextView txBottomCommentName,  txBottomCommentCaption;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imgBottomComment = itemView.findViewById(R.id.imgBottomComment);

            txBottomCommentName = itemView.findViewById(R.id.txBottomCommentName);
            txBottomCommentCaption = itemView.findViewById(R.id.txBottomCommentCaption);

        }

    }
}
