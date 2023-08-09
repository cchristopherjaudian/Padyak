package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.padyak.R;
import com.padyak.adapter.adapterAdminAlert;
import com.padyak.dto.MemberAlert;

import java.util.ArrayList;
import java.util.List;

public class frmAdminAlert extends AppCompatActivity {
    RecyclerView rvAlertList;
    LinearLayoutManager linearLayoutManager;
    List<MemberAlert> memberAlertList;
    com.padyak.adapter.adapterAdminAlert adapterAdminAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_admin_alert);
        rvAlertList = findViewById(R.id.rvAlertList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAlertList.setLayoutManager(linearLayoutManager);
        loadAlerts();
    }
    public void loadAlerts(){
        memberAlertList = new ArrayList<>();
        memberAlertList.add(new MemberAlert("1","1","Ricardo Madlangtuta Jr,","","Awit natumba ako",0d,0d,1));
        memberAlertList.add(new MemberAlert("1","1","Ricardo Madlangtuta Jr,","","Awit natumba ako",0d,0d,1));
        memberAlertList.add(new MemberAlert("1","1","Ricardo Madlangtuta Jr,","","Awit natumba ako",0d,0d,1));
        memberAlertList.add(new MemberAlert("1","1","Ricardo Madlangtuta Jr,","","Awit natumba ako",0d,0d,1));
        adapterAdminAlert = new adapterAdminAlert(memberAlertList);
        rvAlertList.setAdapter(adapterAdminAlert);
    }
}