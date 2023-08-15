package com.padyak.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.padyak.R;
import com.padyak.activity.frmMain;
import com.padyak.adapter.adapterComment;
import com.padyak.dto.Comment;
import com.padyak.dto.Like;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragmentComments extends BottomSheetDialogFragment {

    List<Comment> commentList;
    List<Like> likeList;
    TextView txHeader,txLikeCount;
    RecyclerView rvBottomComments;
    LinearLayoutManager linearLayoutManager;
    EditText etAddComment;
    ImageView imgBottomAddComment,imgLikes;

    com.padyak.adapter.adapterComment adapterComment;
    String postId;
    FragmentManager fragmentManager;
    public fragmentComments(List<Comment> commentList, String postId, List<Like> likeList,FragmentManager fragmentManager) {
        this.commentList = commentList;
        this.likeList = likeList;
        this.postId = postId;
        this.fragmentManager = fragmentManager;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_comment,container, false);
        txHeader = v.findViewById(R.id.txHeader);
        txLikeCount = v.findViewById(R.id.txLikeCount);
        rvBottomComments = v.findViewById(R.id.rvBottomComments);
        etAddComment = v.findViewById(R.id.etAddComment);
        etAddComment.setHint("Comment as " + LoggedUser.getInstance().getFirstName());
        imgBottomAddComment = v.findViewById(R.id.imgBottomAddComment);
        imgLikes = v.findViewById(R.id.imgLikes);
        linearLayoutManager = new LinearLayoutManager(v.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvBottomComments.setLayoutManager(linearLayoutManager);
        adapterComment = new adapterComment(commentList);
        rvBottomComments.setAdapter(adapterComment);
        txHeader.setText(String.format("%d%s", commentList.size(), (commentList.size() > 1) ? " Comments" : " Comment"));
        txLikeCount.setText(String.format("%d%s", likeList.size(), (likeList.size() > 1) ? " Likes" : " Like"));

        txLikeCount.setOnClickListener(l->{
            fragmentLikes fragmentLikes = new fragmentLikes(likeList,fragmentManager);
            fragmentLikes.show(fragmentManager,"bottomLikes");
        });
        imgLikes.setOnClickListener(l->{
            fragmentLikes fragmentLikes = new fragmentLikes(likeList,fragmentManager);
            fragmentLikes.show(fragmentManager,"bottomLikes");
        });
        imgBottomAddComment.setOnClickListener(l->{
            String newComment = etAddComment.getText().toString().trim();
            if(newComment.equals("")) return;
            Map<String, Object> payload = new HashMap<>();
            payload.put("postId",postId);
            payload.put("comment",newComment);
            VolleyHttp volleyHttp = new VolleyHttp("/comments",payload,"post",v.getContext());
            String response = volleyHttp.getResponseBody(true);
            try {
                JSONObject responseJSON = new JSONObject(response);
                int responseCode = responseJSON.getInt("status");
                if(responseCode != 200) throw new JSONException("");
                etAddComment.setText("");
                Comment comment = new Comment();
                comment.setComment(newComment);
                comment.setPhotoUrl(LoggedUser.getInstance().getImgUrl());
                comment.setUserId(LoggedUser.loggedUser.getUuid());
                comment.setDisplayName(LoggedUser.loggedUser.getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()));
                commentList.add(comment);

                adapterComment.notifyItemInserted(commentList.size()-1);
                rvBottomComments.scrollToPosition(commentList.size()-1);
                txHeader.setText(String.format("%d%s", commentList.size(), (commentList.size() > 1) ? " comments" : " comment"));
                frmMain.frmMain.notifyNewsfeed();
            } catch (JSONException e) {
                Toast.makeText(v.getContext(), "Failed to add comment. Please try again.", Toast.LENGTH_SHORT).show();
                Log.d("Log_Padyak", "onCreateView: " + e.getMessage());
            }

        });
        return v;
    }


}
