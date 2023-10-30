package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class frmEventCrud extends AppCompatActivity {

    CardView clJanuary, clFebruary, clMarch, clApril, clMay, clJune, clJuly, clAugust, clSeptember, clOctober, clNovember, clDecember;
    TextView txJanuary, txFebruary, txMarch, txApril, txMay, txJune, txJuly, txAugust, txSeptember, txOctober, txNovember, txDecember;
    Intent intent;
    List<CardView> cvMonth;
    List<TextView> txMonth;
    ProgressDialog progressDialog;
    ImageView imgDP;
    CardView cardView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_crud);
        cardView4 = findViewById(R.id.cardView4);
        imgDP = findViewById(R.id.imgDP);
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgDP);
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

        txJanuary = findViewById(R.id.txJanuary);
        txFebruary = findViewById(R.id.txFebruary);
        txMarch = findViewById(R.id.txMarch);
        txApril = findViewById(R.id.txApril);
        txMay = findViewById(R.id.txMay);
        txJune = findViewById(R.id.txJune);
        txJuly = findViewById(R.id.txJuly);
        txAugust = findViewById(R.id.txAugust);
        txSeptember = findViewById(R.id.txSeptember);
        txOctober = findViewById(R.id.txOctober);
        txNovember = findViewById(R.id.txNovember);
        txDecember = findViewById(R.id.txDecember);

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

        txMonth = new ArrayList<>();
        txMonth.add(txJanuary);
        txMonth.add(txFebruary);
        txMonth.add(txMarch);
        txMonth.add(txApril);
        txMonth.add(txMay);
        txMonth.add(txJune);
        txMonth.add(txJuly);
        txMonth.add(txAugust);
        txMonth.add(txSeptember);
        txMonth.add(txOctober);
        txMonth.add(txNovember);
        txMonth.add(txDecember);
        cardView4.setOnClickListener(v->{
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        });
        for (int i = 0; i < cvMonth.size(); i++) {
            final int monthCounter = i + 1;
            cvMonth.get(i).setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("month", monthCounter);
                intent = new Intent(frmEventCrud.this, frmEventManagement.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCalendar();

    }

    private void loadCalendar() {
        progressDialog = Helper.getInstance().progressDialog(frmEventCrud.this, "Retrieving events.");
        progressDialog.show();

        new Thread(() -> {
            VolleyHttp volleyHttp = new VolleyHttp("/count?year=2023", null, "event", frmEventCrud.this);
            String response = volleyHttp.getResponseBody(true);
            runOnUiThread(()->{
                try {
                    progressDialog.dismiss();
                    JSONObject responseJSON = new JSONObject(response.toUpperCase());
                    int responseCode = responseJSON.getInt("STATUS");
                    if (responseCode != 200) throw new JSONException("Response Code: " + responseCode);
                    int eventNumber = 0;
                    JSONArray eventArray = responseJSON.optJSONArray("DATA");
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject monthObject = eventArray.getJSONObject(i);
                        eventNumber = monthObject.getInt(Month.of(i + 1).toString());
                        if (eventNumber == 0) {
                            txMonth.get(i).setText("No event registered");
                            txMonth.get(i).setTextColor(Color.BLACK);
                        } else {
                            txMonth.get(i).setText(String.valueOf(eventNumber) + " " + ((eventNumber > 1) ? " events" : " event") + " registered");
                        }
                    }
                } catch (JSONException e) {
                    Log.d(Helper.getInstance().log_code, "loadCalendar: " + e.getMessage());
                    Toast.makeText(this, "Failed to retrieve event calendar. Please try again.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }).start();


    }
}