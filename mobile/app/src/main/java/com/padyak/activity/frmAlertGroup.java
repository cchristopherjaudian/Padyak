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
import com.padyak.dto.GroupContact;

import java.util.ArrayList;
import java.util.List;

public class frmAlertGroup extends AppCompatActivity {
    CheckBox chkSelectAll;
    RecyclerView rvAlertGroup;
    Button btnSendGroup;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterAlertGroup adapterAlertGroup;
    List<GroupContact> groupContacts;

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
        groupContacts = new ArrayList<>();
        groupContacts.add(new GroupContact("Koya Elmo","","123",false));
        groupContacts.add(new GroupContact("Koya Elmo","","123",false));
        groupContacts.add(new GroupContact("Koya Elmo","","123",false));
        groupContacts.add(new GroupContact("Koya Elmo","","123",false));
        groupContacts.add(new GroupContact("Koya Elmo","","123",false));
        groupContacts.add(new GroupContact("Koya Elmo","","123",false));

        adapterAlertGroup = new adapterAlertGroup(groupContacts);
        rvAlertGroup.setAdapter(adapterAlertGroup);
    }
}