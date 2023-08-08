package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.padyak.R;
import com.padyak.adapter.adapterAlertGroup;

import java.util.ArrayList;
import java.util.List;

public class frmAlertGroup extends AppCompatActivity {
    CheckBox chkSelectAll;
    RecyclerView rvAlertGroup;
    Button btnSendGroup;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterAlertGroup adapterAlertGroup;
    List<String> _name, _contact;
    List<Boolean> _isselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_group);
        chkSelectAll = findViewById(R.id.chkSelectAll);
        btnSendGroup = findViewById(R.id.btnSendGroup);
        rvAlertGroup = findViewById(R.id.rvAlertGroup);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvAlertGroup.setLayoutManager(linearLayoutManager);

        btnSendGroup.setOnClickListener(v -> {
            frmAlertInfo.frmAlertInfo.finish();
            frmAlertSend.frmAlertSend.finish();
            finish();
        });

        chkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapterAlertGroup.setCheck(isChecked);
                adapterAlertGroup.notifyDataSetChanged();
            }
        });
        loadContacts();
    }

    public void loadContacts() {
        _name = new ArrayList<>();
        _contact = new ArrayList<>();
        _isselected = new ArrayList<>();

        _name.add("Rasta Man");
        _name.add("Elmo Porsyento");
        _name.add("Ricardo Madlangtuta");

        _contact.add("Rasta Man");
        _contact.add("Elmo Porsyento");
        _contact.add("Ricardo Madlangtuta");

        _isselected.add(false);
        _isselected.add(false);
        _isselected.add(false);

        adapterAlertGroup = new adapterAlertGroup(_name, _contact, _isselected);
        rvAlertGroup.setAdapter(adapterAlertGroup);
    }
}