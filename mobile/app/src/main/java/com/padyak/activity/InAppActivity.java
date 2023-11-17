package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.padyak.R;
import com.padyak.utility.Helper;

public class InAppActivity extends AppCompatActivity {
    public static InAppActivity inAppActivity;
    EditText txMobileNumber,txNewPassword,txConfirmPassword;
    Button btnCreateInApp;
    boolean is_forgot;
    TextView textView3;
    ImageView imgShowPassword,imgShowPassword2;
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
        imgShowPassword = findViewById(R.id.imgShowPassword);
        imgShowPassword2 = findViewById(R.id.imgShowPassword2);
        textView3.setText(is_forgot ? "Forgot Password" : "Create New Account");
        btnCreateInApp.setText(is_forgot ? "Reset" : "Create");

        btnCreateInApp.setOnClickListener(v->{
            if(!Helper.getInstance().validateMobileNumber(txMobileNumber.getText().toString().trim())){
                Toast.makeText(inAppActivity, "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
                return;
            }
            if(!txNewPassword.getText().toString().trim().equals(txConfirmPassword.getText().toString().trim())){
                Toast.makeText(inAppActivity, "New Password and Confirmed Password are not equal.", Toast.LENGTH_LONG).show();
                return;
            }
            if(!Helper.getInstance().checkString(txNewPassword.getText().toString().trim())){
                AlertDialog alertDialog = new AlertDialog.Builder(InAppActivity.this).create();
                alertDialog.setTitle("Password Confirmation");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Password should contain these criterias:\nAtleast 8 Characters\nContains atleast 1 special character\nContains atleast 1 numeric value\nShould start with a capital letter");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (d, w) -> {

                        });

                alertDialog.show();
                return;
            }
            Intent intent = new Intent(InAppActivity.this,VerificationActivity.class);
            Bundle b = new Bundle();

            b.putBoolean("registration", !is_forgot);
            b.putString("authSource","IN_APP");
            b.putString("mobileNumber",txMobileNumber.getText().toString().trim());
            b.putString("password",txNewPassword.getText().toString().trim());

            intent.putExtras(b);
            startActivity(intent);
        });

        imgShowPassword.setOnClickListener(v->{
            if(txNewPassword.getInputType() == 129){
                txNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else{
                txNewPassword.setInputType(129);
            }
        });
        imgShowPassword2.setOnClickListener(v->{
            if(txConfirmPassword.getInputType() == 129){
                txConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else{
                txConfirmPassword.setInputType(129);
            }
        });
    }
}