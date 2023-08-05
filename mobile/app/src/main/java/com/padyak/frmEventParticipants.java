package com.padyak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;


public class frmEventParticipants extends AppCompatActivity {

    RecyclerView rvEventInfoParticipants;
    LinearLayoutManager linearLayoutManager;
    adapterParticipant adapterParticipant;

    Button btnEventCancel;
    int tempPos = 0;
    CustomViewPager viewPager;
    SmartTabLayout viewPagerTab;
    FragmentPagerItemAdapter adapter;
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
                .add("Participants",fragmentEvent.class)
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
        List<String> _name,_distance;
        _name = new ArrayList<>();
        _distance = new ArrayList<>();
        _name.add("Ricardo Madlangtuta");
        _name.add("Rastaman Yow");
        _name.add("Gorgonio Magalpoc");
        _distance.add("1,000km");
        _distance.add("14,800km");
        _distance.add("321km");
        adapterParticipant = new adapterParticipant(_name,_distance);
        rvEventInfoParticipants.setAdapter(adapterParticipant);
    }
}