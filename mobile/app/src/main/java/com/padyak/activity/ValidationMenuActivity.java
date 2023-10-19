package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.padyak.R;

public class ValidationMenuActivity extends AppCompatActivity {
    Button btnValidatePayment,btnUploadQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_menu);

        btnValidatePayment = findViewById(R.id.btnValidatePayment);
        btnUploadQR = findViewById(R.id.btnUploadQR);

        btnValidatePayment.setOnClickListener(e->{
            Intent intent = new Intent(ValidationMenuActivity.this,PaymentValidationActivity.class);
            startActivity(intent);
        });
    }
}