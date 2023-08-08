package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.padyak.R;
import com.padyak.adapter.adapterCoverPhoto;
import com.padyak.adapter.adapterNewsfeed;
import com.padyak.adapter.adapterYouMayKnow;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class frmMain extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    ScrollView frame_home, frame_profile;
    ConstraintLayout frame_newsfeed;
    RecyclerView rvCoverPhoto, rvYouKnow, rvNewsfeed;
    LinearLayoutManager llmCoverPhoto, llYouMayKnow, llNewsfeed;
    List<Integer> imageList;
    List<String> _name, _id;
    List<String> _nfname, _nfstart, _nfend, _nfdistance, _nftimestart, _nftimeend;
    List<Integer> _mutual;
    com.padyak.adapter.adapterYouMayKnow adapterYouMayKnow;
    com.padyak.adapter.adapterCoverPhoto adapterCoverPhoto;
    com.padyak.adapter.adapterNewsfeed adapterNewsfeed;


    RelativeLayout rlEvents, rlAlert, rlHospital, rlRepair, rlPolice, rlRiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_main);

        bottomBar = findViewById(R.id.bottomBar);
        frame_home = findViewById(R.id.frame_home);
        frame_profile = findViewById(R.id.frame_profile);
        frame_newsfeed = findViewById(R.id.frame_newsfeed);

        rvCoverPhoto = findViewById(R.id.rvCoverPhoto);
        rvYouKnow = findViewById(R.id.rvYouKnow);
        rvNewsfeed = findViewById(R.id.rvNewsfeed);

        llmCoverPhoto = new LinearLayoutManager(this);
        llYouMayKnow = new LinearLayoutManager(this);
        llNewsfeed = new LinearLayoutManager(this);
        llmCoverPhoto.setOrientation(RecyclerView.HORIZONTAL);
        llYouMayKnow.setOrientation(RecyclerView.HORIZONTAL);
        llNewsfeed.setOrientation(RecyclerView.VERTICAL);

        rvCoverPhoto.setLayoutManager(llmCoverPhoto);
        rvYouKnow.setLayoutManager(llYouMayKnow);
        rvNewsfeed.setLayoutManager(llNewsfeed);

        rlEvents = findViewById(R.id.rlEvents);
        rlAlert = findViewById(R.id.rlAlert);
        rlHospital = findViewById(R.id.rlTrack);
        rlRepair = findViewById(R.id.rlRepair);
        rlPolice = findViewById(R.id.rlPolice);
        rlRiding = findViewById(R.id.rlRiding);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvCoverPhoto);

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0) {
                    frame_home.setVisibility(View.GONE);
                    frame_profile.setVisibility(View.VISIBLE);
                    frame_newsfeed.setVisibility(View.GONE);
                } else if (i == 1) {
                    frame_home.setVisibility(View.GONE);
                    frame_profile.setVisibility(View.GONE);
                    frame_newsfeed.setVisibility(View.VISIBLE);
                } else {
                    frame_home.setVisibility(View.VISIBLE);
                    frame_profile.setVisibility(View.GONE);
                    frame_newsfeed.setVisibility(View.GONE);
                }
                return false;
            }
        });
        rlEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmMain.this, frmEventCalendar.class);
                startActivity(intent);
            }
        });
        rlAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmMain.this, frmAlertInfo.class);
                startActivity(intent);
            }
        });

        rlHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmMain.this, frmFindLocation.class);
                Bundle bundle = new Bundle();
                bundle.putString("find", "Hospital");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rlRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmMain.this, frmFindLocation.class);
                Bundle bundle = new Bundle();
                bundle.putString("find", "Repair Shop");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rlPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmMain.this, frmFindLocation.class);
                Bundle bundle = new Bundle();
                bundle.putString("find", "Police Station");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        loadYouMayKnow();
        loadCoverPhoto();
        loadNewsfeed();
    }

    public void loadCoverPhoto() {
        imageList = new ArrayList<>();
        adapterCoverPhoto = new adapterCoverPhoto(imageList);

        imageList.add(R.drawable.bike1);
        imageList.add(R.drawable.bike2);
        imageList.add(R.drawable.bike3);

        rvCoverPhoto.setAdapter(adapterCoverPhoto);
    }

    public void loadNewsfeed() {
        _nfname = new ArrayList<>();
        _nfstart = new ArrayList<>();
        _nfend = new ArrayList<>();
        _nfdistance = new ArrayList<>();
        _nftimestart = new ArrayList<>();
        _nftimeend = new ArrayList<>();
        _nfname.add("1");
        _nfname.add("1");
        _nfname.add("1");
        _nfname.add("1");
        _nfname.add("1");
        _nfname.add("1");
        _nfname.add("1");


        adapterNewsfeed = new adapterNewsfeed(_nfname, _nfstart, _nfend, _nfdistance, _nftimestart, _nftimeend);
        rvNewsfeed.setAdapter(adapterNewsfeed);
    }

    public void loadYouMayKnow() {
        _name = new ArrayList<>();
        _id = new ArrayList<>();
        _mutual = new ArrayList<>();
        adapterYouMayKnow = new adapterYouMayKnow(_name, _id, _mutual);

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