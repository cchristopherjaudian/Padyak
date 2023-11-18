package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.padyak.R;

public class TermsActivity extends AppCompatActivity {
    Button btnAgree;
    String mobileNumber;
    String source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        mobileNumber = getIntent().getStringExtra("mobileNumber");
        source = getIntent().getStringExtra("source");
        btnAgree = findViewById(R.id.btnAgree);
        btnAgree.setOnClickListener(v->{
            if(source.equals("IN_APP")){
                Intent intent = new Intent(TermsActivity.this, VerificationActivity.class);
                Bundle b = new Bundle();
                b.putString("mobileNumber",mobileNumber);
                b.putBoolean("registration",true);
                intent.putExtras(b);
                startActivity(intent);
            } else{
                frmAccount f = frmAccount.frmAccount;
                f.registerAccount();
            }
            finish();
        });
    }
}