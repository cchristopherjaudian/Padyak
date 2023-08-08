package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.padyak.R;

public class frmEventCrud extends AppCompatActivity implements View.OnClickListener {

    CardView clJanuary, clFebruary, clMarch, clApril, clMay, clJune, clJuly, clAugust, clSeptember, clOctober, clNovember, clDecember;
    ConstraintLayout rlJanuary;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_crud);
        clJanuary = findViewById(R.id.clJanuary);
        rlJanuary = findViewById(R.id.rlJanuary);
        clFebruary = findViewById(R.id.clFebruary);
        clMarch = findViewById(R.id.clMarch);
        clApril = findViewById(R.id.clApril);
        clMay = findViewById(R.id.clMay);
        clJune = findViewById(R.id.clJune);
        clJuly = findViewById(R.id.clJuly);
        clAugust = findViewById(R.id.clAugust);
        clSeptember = findViewById(R.id.clSeptember);
        clOctober = findViewById(R.id.clOctober);
        clNovember = findViewById(R.id.clNovember);
        clDecember = findViewById(R.id.clDecember);

        clJanuary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("month", 1);
                intent = new Intent(frmEventCrud.this, frmEventManagement.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int month = 0;
        switch (v.getId()) {
            case R.id.clJanuary:
            case R.id.rlJanuary:
                month = 1;
                break;
            case R.id.clFebruary:
                month = 2;
                break;
            case R.id.clMarch:
                month = 3;
                break;
            case R.id.clApril:
                month = 4;
                break;
            case R.id.clMay:
                month = 5;
                break;
            case R.id.clJune:
                month = 6;
                break;
            case R.id.clJuly:
                month = 7;
                break;
            case R.id.clAugust:
                month = 8;
                break;
            case R.id.clSeptember:
                month = 9;
                break;
            case R.id.clOctober:
                month = 10;
                break;
            case R.id.clNovember:
                month = 11;
                break;
            case R.id.clDecember:
                month = 12;
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("month", month);
        intent = new Intent(frmEventCrud.this, frmEventManagement.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}