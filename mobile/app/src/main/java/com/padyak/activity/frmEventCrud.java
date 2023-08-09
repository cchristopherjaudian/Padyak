package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.padyak.R;

import java.util.ArrayList;
import java.util.List;

public class frmEventCrud extends AppCompatActivity {

    CardView clJanuary, clFebruary, clMarch, clApril, clMay, clJune, clJuly, clAugust, clSeptember, clOctober, clNovember, clDecember;
    Intent intent;
    List<CardView> cvMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_crud);
        clJanuary = findViewById(R.id.clJanuary);
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

        cvMonth = new ArrayList<>();
        cvMonth.add(clJanuary);
        cvMonth.add(clFebruary);
        cvMonth.add(clMarch);
        cvMonth.add(clApril);
        cvMonth.add(clMay);
        cvMonth.add(clJune);
        cvMonth.add(clJuly);
        cvMonth.add(clAugust);
        cvMonth.add(clSeptember);
        cvMonth.add(clOctober);
        cvMonth.add(clNovember);
        cvMonth.add(clDecember);

        for(int i = 0; i < cvMonth.size();i++){
            final int monthCounter = i+1;
            cvMonth.get(i).setOnClickListener(v->{
                Bundle bundle = new Bundle();
                bundle.putInt("month", monthCounter);
                intent = new Intent(frmEventCrud.this, frmEventManagement.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }

    }
}