package com.padyak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class frmAlertSend extends AppCompatActivity {
    TextView txAlertLevel,txAlertDescription;
    Button btnAlertGroup,btnSendContact;
    String alertLevel,alertDescription;
    public static frmAlertSend frmAlertSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_alert_send);
        frmAlertSend = this;
        Bundle bundle = getIntent().getExtras();
        btnSendContact = findViewById(R.id.btnSendContact);
        btnAlertGroup = findViewById(R.id.btnAlertGroup);
        txAlertLevel = findViewById(R.id.txAlertLevel);
        txAlertDescription = findViewById(R.id.txAlertDescription);
        alertLevel = bundle.getString("Level");
        alertDescription = bundle.getString("Description");

        txAlertLevel.setText(alertLevel);
        txAlertDescription.setText(alertDescription);

        btnAlertGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmAlertSend.this, frmAlertGroup.class);
                startActivity(intent);
            }
        });
        btnSendContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frmAlertInfo.frmAlertInfo.finish();
                finish();
            }
        });
    }
}