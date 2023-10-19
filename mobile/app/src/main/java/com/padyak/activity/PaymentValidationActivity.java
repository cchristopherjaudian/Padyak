package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.padyak.R;
import com.padyak.adapter.adapterPaymentValidation;
import com.padyak.dto.UserValidation;
import com.padyak.fragment.fragmentUserUploaded;

import java.util.ArrayList;
import java.util.List;

public class PaymentValidationActivity extends AppCompatActivity {
    public static PaymentValidationActivity me;
    RecyclerView rvValidation;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterPaymentValidation adapterPaymentValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validation);
        me = this;
        rvValidation = findViewById(R.id.rvValidation);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvValidation.setLayoutManager(linearLayoutManager);
        loadValidations();
    }
    public void loadValidations(){
        List<UserValidation> participants = new ArrayList<>();
        participants.add(new UserValidation("Ricardo","","",""));
        adapterPaymentValidation = new adapterPaymentValidation(participants);

        rvValidation.setAdapter(adapterPaymentValidation);
    }
    public void showUserUploaded(){
        FragmentManager fm = getSupportFragmentManager();
        fragmentUserUploaded editNameDialogFragment = fragmentUserUploaded.newInstance("UserUpload");
        editNameDialogFragment.show(fm, "UserUpload");
    }
}