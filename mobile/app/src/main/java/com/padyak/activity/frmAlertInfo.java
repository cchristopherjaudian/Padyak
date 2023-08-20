package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.padyak.R;
import com.padyak.dto.AlertLevel;

import java.util.ArrayList;
import java.util.List;

public class frmAlertInfo extends AppCompatActivity {
    ConstraintLayout cl1, cl2, cl3, cl4;
    List<AlertLevel> alertLevelList;
    Intent intent;
    Bundle bundle;
    public static frmAlertInfo frmAlertInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_info);
        frmAlertInfo = this;
        cl1 = findViewById(R.id.cl1);
        cl2 = findViewById(R.id.cl2);
        cl3 = findViewById(R.id.cl3);
        cl4 = findViewById(R.id.cl4);
        alertLevelList = new ArrayList<>();
        alertLevelList.add(new AlertLevel(1, "I am safe. I just can't maintain the pace"));
        alertLevelList.add(new AlertLevel(2, "My bike has a problem"));
        alertLevelList.add(new AlertLevel(3, "I had a bike breakdown and I don't have any tools. Please help."));
        alertLevelList.add(new AlertLevel(4, "I had an accident and I need help."));

        intent = new Intent(frmAlertInfo.this, frmAlertSend.class);
        cl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle =new Bundle();
                bundle.putInt("level",alertLevelList.get(0).getLevel());
                bundle.putString("Description",alertLevelList.get(0).getDescription());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle =new Bundle();
                bundle.putInt("level",alertLevelList.get(1).getLevel());
                bundle.putString("Description",alertLevelList.get(1).getDescription());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle =new Bundle();
                bundle.putInt("level",alertLevelList.get(2).getLevel());
                bundle.putString("Description",alertLevelList.get(2).getDescription());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putInt("level",alertLevelList.get(3).getLevel());
                bundle.putString("Description",alertLevelList.get(3).getDescription());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

