package com.padyak.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.Comment;
import com.padyak.dto.Like;
import com.padyak.dto.Newsfeed;
import com.padyak.fragment.fragmentComments;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class adapterNewsfeed extends RecyclerView.Adapter<adapterNewsfeed.viewHolder> {


    List<Newsfeed> newsfeeds;
    FragmentManager fragmentManager;

    public adapterNewsfeed(List<Newsfeed> newsfeeds, FragmentManager fragmentManager) {
        this.newsfeeds = newsfeeds;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public adapterNewsfeed.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_newsfeed, parent, false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterNewsfeed.viewHolder holder, int position) {
        Newsfeed n = newsfeeds.get(position);
        if (n.getPhotoUrl().trim().equals("n/a")) {
            holder.imgNewsfeed.setVisibility(View.GONE);
            holder.clNewsfeedMain.setPadding(0,0,0,15);
        } else {
            Picasso.get().load(n.getPhotoUrl()).into(holder.imgNewsfeed);
        }
        Picasso.get().load(n.getPostAuthor().getPhotoUrl()).into(holder.imgParticipant);
        holder.txRowName.setText(n.getPostAuthor().getFirstname().concat(" ").concat(n.getPostAuthor().getLastname()));
        holder.textView45.setText(n.getFromLocation());
        holder.txRideDestination.setText(n.getToLocation());
        holder.textView40.setText(n.getMovingTime());
        holder.txRideDistance.setText(n.getDistance());
        holder.txPostTime.setText(n.getCreatedAt().replace("+08:00","").replace("T"," "));

        holder.txLikes.setText(String.valueOf(n.getLikeList().size()));
        holder.txComments.setText(String.valueOf(n.getCommentList().size()));

        holder.imgComments.setImageResource(R.drawable.baseline_add_comment_24);
        holder.imgLikes.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);

        for (Comment c : n.getCommentList()) {
            if (c.getUserId().equals(LoggedUser.getInstance().getUuid())) {
                holder.imgComments.setImageResource(R.drawable.baseline_mode_comment_24);
                break;
            }
        }
        for (Like l : n.getLikeList()) {
            if (l.getUserId().equals(LoggedUser.getInstance().getUuid())) {
                holder.imgLikes.setImageResource(R.drawable.baseline_thumb_up_24);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return newsfeeds.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imgNewsfeed, imgParticipant, imgComments, imgLikes;
        TextView textView45, txRideDestination, textView40, txRideDistance, txPostTime, txComments, txLikes;
        TextView txRowName;
        ConstraintLayout clNewsfeedMain;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            clNewsfeedMain= itemView.findViewById(R.id.clNewsfeedMain);
            imgNewsfeed = itemView.findViewById(R.id.imgNewsfeed);
            imgParticipant = itemView.findViewById(R.id.imgParticipant);
            imgComments = itemView.findViewById(R.id.imgComments);
            imgLikes = itemView.findViewById(R.id.imgLikes);

            txComments = itemView.findViewById(R.id.txCommentsCount);
            txLikes = itemView.findViewById(R.id.txLikesCount);
            textView45 = itemView.findViewById(R.id.textView45);
            txRideDestination = itemView.findViewById(R.id.txRideDestination);
            textView40 = itemView.findViewById(R.id.textView40);
            txRideDistance = itemView.findViewById(R.id.txRideDistance);
            txPostTime = itemView.findViewById(R.id.txPostTime);
            txRowName = itemView.findViewById(R.id.txRowName);

            imgComments.setOnClickListener(v -> {
                showComments();
            });
            txComments.setOnClickListener(v->{
                showComments();
            });
            imgLikes.setOnClickListener(v -> {
                processLikes();
            });
            txLikes.setOnClickListener(v->{
                processLikes();
            });
        }

        private void showComments() {
            Log.d("Log_Padyak", "showComments: ");
            fragmentComments comments = new fragmentComments(newsfeeds.get(getAdapterPosition()).getCommentList(), newsfeeds.get(getAdapterPosition()).getId(),newsfeeds.get(getAdapterPosition()).getLikeList(), fragmentManager);
            comments.show(fragmentManager, "BottomComment");
        }

        private void processLikes() {
            Boolean isExisting = false;
            Map<String, Object> payload = new HashMap<>();
            payload.put("postId", newsfeeds.get(getAdapterPosition()).getId());
            VolleyHttp volleyHttp = new VolleyHttp("/likes", payload, "post", itemView.getContext());
            String response = volleyHttp.getResponseBody(true);
            try {
                JSONObject responseJSON = new JSONObject(response);
                int responseCode = responseJSON.getInt("status");
                if (responseCode != 200) return;

                List<Like> likeList = newsfeeds.get(getAdapterPosition()).getLikeList();
                Iterator<Like> iterator = likeList.iterator();
                while (iterator.hasNext()) {
                    Like like = iterator.next();
                    if (like.getUserId().equals(LoggedUser.getInstance().getUuid())) {
                        isExisting = true;
                        iterator.remove();
                        break;
                    }
                }

                if(!isExisting){
                    Like like = new Like();
                    like.setDisplayName(LoggedUser.getInstance().getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()));
                    like.setUserId(LoggedUser.loggedUser.getUuid());
                    like.setPhotoUrl(LoggedUser.getInstance().getImgUrl());
                    newsfeeds.get(getAdapterPosition()).getLikeList().add(like);
                }

                notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("Log_Padyak", "viewHolder: " + e.getMessage());
            }

        }


    }
}
