package com.padyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class frmMain extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    ScrollView frame_home,frame_profile;
    RecyclerView rvCoverPhoto,rvYouKnow;
    LinearLayoutManager llmCoverPhoto, llYouMayKnow;
    List<Integer> imageList;
    List<String> _name,_id;
    List<Integer> _mutual;
    adapterYouMayKnow adapterYouMayKnow;
    adapterCoverPhoto adapterCoverPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_main);

        bottomBar = findViewById(R.id.bottomBar);
        frame_home = findViewById(R.id.frame_home);
        frame_profile = findViewById(R.id.frame_profile);

        rvCoverPhoto = findViewById(R.id.rvCoverPhoto);
        rvYouKnow = findViewById(R.id.rvYouKnow);

        llmCoverPhoto = new LinearLayoutManager(this);
        llYouMayKnow = new LinearLayoutManager(this);
        llmCoverPhoto.setOrientation(RecyclerView.HORIZONTAL);
        llYouMayKnow.setOrientation(RecyclerView.HORIZONTAL);

        rvCoverPhoto.setLayoutManager(llmCoverPhoto);
        rvYouKnow.setLayoutManager(llYouMayKnow);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvCoverPhoto);

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if(i == 0){
                    frame_home.setVisibility(View.GONE);
                    frame_profile.setVisibility(View.VISIBLE);
                } else{
                    frame_home.setVisibility(View.VISIBLE);
                    frame_profile.setVisibility(View.GONE);
                }
                return false;
            }
        });

        loadYouMayKnow();
        loadCoverPhoto();
    }
    public void loadCoverPhoto(){
        imageList = new ArrayList<>();
        adapterCoverPhoto = new adapterCoverPhoto(imageList);

        imageList.add(R.drawable.bike1);
        imageList.add(R.drawable.bike2);
        imageList.add(R.drawable.bike3);

        rvCoverPhoto.setAdapter(adapterCoverPhoto);
    }
    public void loadYouMayKnow(){
        _name = new ArrayList<>();
        _id = new ArrayList<>();
        _mutual= new ArrayList<>();
        adapterYouMayKnow = new adapterYouMayKnow(_name,_id,_mutual);

        _name.add("Gorgonio Magalpoc");
        _id.add("Gorgonio Magalpoc");
        _mutual.add(5);

        _name.add("Koya Elmo");
        _id.add("Gorgonio Magalpoc");
        _mutual.add(10);

        _name.add("Logbi Patotie");
        _id.add("Gorgonio Magalpoc");
        _mutual.add(2);

        rvYouKnow.setAdapter(adapterYouMayKnow);
    }
    @Override
    public void onBackPressed() {

    }
}