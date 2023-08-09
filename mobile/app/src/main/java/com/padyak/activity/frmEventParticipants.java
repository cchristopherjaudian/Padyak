package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.padyak.R;
import com.padyak.adapter.adapterParticipant;
import com.padyak.dto.Participants;
import com.padyak.fragment.fragmentEvent;
import com.padyak.utility.CustomViewPager;

import java.util.ArrayList;
import java.util.List;


public class frmEventParticipants extends AppCompatActivity {

    RecyclerView rvEventInfoParticipants;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterParticipant adapterParticipant;

    Button btnEventCancel;
    int tempPos = 0;
    CustomViewPager viewPager;
    SmartTabLayout viewPagerTab;
    FragmentPagerItemAdapter adapter;
    List<Participants> participantsList;
    public static String selectedTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_participants);
        rvEventInfoParticipants = findViewById(R.id.rvEventInfoParticipants);
        btnEventCancel = findViewById(R.id.btnEventCancel);
        viewPager = findViewById(R.id.customviewpager);
        viewPagerTab = findViewById(R.id.viewpagertab);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvEventInfoParticipants.setLayoutManager(linearLayoutManager);
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Participants", fragmentEvent.class)
                .add("Following",fragmentEvent.class)
                .create());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.disableScroll(true);
        selectedTab = "Participants";

        btnEventCancel.setOnClickListener(v->finish());

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tempPos = position;
                selectedTab = adapter.getPageTitle(position).toString().trim();
                if(position == 0) loadParticipants();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        loadParticipants();
    }

    public void loadParticipants(){

        participantsList = new ArrayList<>();
        participantsList.add(new Participants());
        participantsList.add(new Participants());
        participantsList.add(new Participants());

        adapterParticipant = new adapterParticipant(participantsList);
        rvEventInfoParticipants.setAdapter(adapterParticipant);
    }
}