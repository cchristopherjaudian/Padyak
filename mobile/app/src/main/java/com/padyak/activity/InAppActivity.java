package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.padyak.R;

public class InAppActivity extends AppCompatActivity {
    public static InAppActivity inAppActivity;
    EditText txMobileNumber,txNewPassword,txConfirmPassword;
    Button btnCreateInApp;
    boolean is_forgot;
    TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app);
        inAppActivity = this;
        btnCreateInApp = findViewById(R.id.btnCreateInApp);
        is_forgot = getIntent().getBooleanExtra("forgot",false);
        txMobileNumber = findViewById(R.id.txMobileNumber);
        txNewPassword = findViewById(R.id.txNewPassword);
        txConfirmPassword = findViewById(R.id.txConfirmPassword);
        textView3 = findViewById(R.id.textView3);

        textView3.setText(is_forgot ? "Forgot Password" : "Create New Account");
        btnCreateInApp.setText(is_forgot ? "Reset" : "Create");

        txMobileNumber.setText("09272636223");
        txNewPassword.setText("Jopots001*");
        txConfirmPassword.setText("Jopots001*");
        btnCreateInApp.setOnClickListener(v->{
            Intent intent = new Intent(InAppActivity.this,VerificationActivity.class);
            Bundle b = new Bundle();

            b.putBoolean("registration", !is_forgot);
            b.putString("authSource","IN_APP");
            b.putString("mobileNumber",txMobileNumber.getText().toString().trim());
            b.putString("password",txNewPassword.getText().toString().trim());

            intent.putExtras(b);
            startActivity(intent);
        });
    }
}