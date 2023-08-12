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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.adapter.adapterCoverPhoto;
import com.padyak.adapter.adapterNewsfeed;
import com.padyak.dto.CoverPhoto;
import com.padyak.dto.Newsfeed;
import com.padyak.utility.LoggedUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class frmMain extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    ScrollView frame_home, frame_profile;
    ConstraintLayout frame_newsfeed;
    RecyclerView rvCoverPhoto, rvNewsfeed;
    LinearLayoutManager llmCoverPhoto, llNewsfeed;
    List<Newsfeed> newsfeedList;
    List<CoverPhoto> coverPhotoList;
    com.padyak.adapter.adapterCoverPhoto adapterCoverPhoto;
    com.padyak.adapter.adapterNewsfeed adapterNewsfeed;
    TextView txMainProfileName,txProfileName;
    ImageView imgMainProfileDP,imgProfileDP;

    RelativeLayout rlEvents, rlAlert, rlHospital, rlRepair, rlPolice, rlRiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_main);

        imgProfileDP = findViewById(R.id.imgProfileDP);
        imgMainProfileDP = findViewById(R.id.imgMainProfileDP);

        txMainProfileName = findViewById(R.id.txMainProfileName);
        txProfileName = findViewById(R.id.txProfileName);

        bottomBar = findViewById(R.id.bottomBar);
        frame_home = findViewById(R.id.frame_home);
        frame_profile = findViewById(R.id.frame_profile);
        frame_newsfeed = findViewById(R.id.frame_newsfeed);

        rvCoverPhoto = findViewById(R.id.rvCoverPhoto);
        rvNewsfeed = findViewById(R.id.rvNewsfeed);

        llmCoverPhoto = new LinearLayoutManager(this);
        llNewsfeed = new LinearLayoutManager(this);
        llmCoverPhoto.setOrientation(RecyclerView.HORIZONTAL);
        llNewsfeed.setOrientation(RecyclerView.VERTICAL);

        rvCoverPhoto.setLayoutManager(llmCoverPhoto);
        rvNewsfeed.setLayoutManager(llNewsfeed);

        rlEvents = findViewById(R.id.rlEvents);
        rlAlert = findViewById(R.id.rlAlert);
        rlHospital = findViewById(R.id.rlTrack);
        rlRepair = findViewById(R.id.rlRepair);
        rlPolice = findViewById(R.id.rlPolice);
        rlRiding = findViewById(R.id.rlRiding);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvCoverPhoto);


        txMainProfileName.setText("Hey " + LoggedUser.getInstance().getFirstName());
        txProfileName.setText(LoggedUser.getInstance().getFirstName());
        try {
            Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgProfileDP);
            Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgMainProfileDP);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


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
        loadCoverPhoto();
        loadNewsfeed();
    }

    public void loadCoverPhoto() {
        coverPhotoList = new ArrayList<>();
        coverPhotoList.add(new CoverPhoto("","",R.drawable.bike1));
        coverPhotoList.add(new CoverPhoto("","",R.drawable.bike2));
        coverPhotoList.add(new CoverPhoto("","",R.drawable.bike3));
        adapterCoverPhoto = new adapterCoverPhoto(coverPhotoList);
        rvCoverPhoto.setAdapter(adapterCoverPhoto);
    }

    public void loadNewsfeed() {
        newsfeedList = new ArrayList<>();
        newsfeedList.add(new Newsfeed());
        newsfeedList.add(new Newsfeed());
        newsfeedList.add(new Newsfeed());
        newsfeedList.add(new Newsfeed());

        adapterNewsfeed = new adapterNewsfeed(newsfeedList);
        rvNewsfeed.setAdapter(adapterNewsfeed);
    }


    @Override
    public void onBackPressed() {

    }
}