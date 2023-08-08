package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.padyak.R;
import com.padyak.adapter.adapterAdminAlert;

import java.util.ArrayList;
import java.util.List;

public class frmAdminAlert extends AppCompatActivity {
    RecyclerView rvAlertList;
    LinearLayoutManager linearLayoutManager;
    List<String> _name, _message;
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
        _name = new ArrayList<>();
        _message = new ArrayList<>();

        _name.add("Ricardo Madlangtuta Jr.");
        _name.add("Gorgonio Magalpoc");
        _message.add("Awit natumba ako");
        _message.add("Nakatapak ng taka dre");
        adapterAdminAlert = new adapterAdminAlert(_name,_message);
        rvAlertList.setAdapter(adapterAdminAlert);
    }
}