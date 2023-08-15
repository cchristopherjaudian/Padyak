package com.padyak.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.padyak.adapter.adapterLike;
import com.padyak.dto.Comment;
import com.padyak.dto.Like;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragmentLikes extends BottomSheetDialogFragment {

    List<Like> likeList;
    TextView txHeader;
    RecyclerView rvBottomLikes;
    LinearLayoutManager linearLayoutManager;

    com.padyak.adapter.adapterLike adapterLike;
    FragmentManager fragmentManager;
    public fragmentLikes(List<Like> likeList, FragmentManager fragmentManager) {
        this.likeList = likeList;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_like,container, false);
        txHeader = v.findViewById(R.id.txHeader);
        rvBottomLikes = v.findViewById(R.id.rvBottomLikes);
        linearLayoutManager = new LinearLayoutManager(v.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvBottomLikes.setLayoutManager(linearLayoutManager);
        adapterLike = new adapterLike(likeList);
        rvBottomLikes.setAdapter(adapterLike);
        txHeader.setText(String.format("%d%s", likeList.size(), (likeList.size() > 1) ? " Likes" : " Like"));
        return v;
    }


}
