package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.Prefs;

import java.util.Arrays;
import java.util.Calendar;

public class InappRegistrationActivity extends AppCompatActivity {
    String[] gender = {"-Please select a gender-", "Male", "Female"};
    String[] heightUnit = {"-", "cm", "in"};
    String photoURL = "";
    ArrayAdapter<String> aaGender, aaHeightUnit;
    EditText etPassword, etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateBirthdate, etCreateHeight, etCreateWeight;
    Spinner etCreateGender, etHeightUnit;
    Button btnUpdateAccount, btnCancelAccount;
    boolean inputValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_registration);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnCancelAccount = findViewById(R.id.btnCancelAccount);

        etHeightUnit = findViewById(R.id.etHeightUnit);
        etPassword = findViewById(R.id.etPassword);
        etCreateEmail = findViewById(R.id.txAddEventTitle);
        etCreateFirstName = findViewById(R.id.etCreateFirstName);
        etCreateLastName = findViewById(R.id.etCreateLastName);
        etCreateContact = findViewById(R.id.etCreateContact);
        etCreateBirthdate = findViewById(R.id.etCreateBirthdate);
        etCreateHeight = findViewById(R.id.etCreateHeight);
        etCreateWeight = findViewById(R.id.etCreateWeight);
        etCreateGender = findViewById(R.id.etCreateGender);

        aaGender = new ArrayAdapter<String>(InappRegistrationActivity.this, R.layout.sp_format, gender);
        aaGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCreateGender.setAdapter(aaGender);
        aaHeightUnit = new ArrayAdapter<String>(InappRegistrationActivity.this, R.layout.sp_format, heightUnit);
        aaHeightUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etHeightUnit.setAdapter(aaHeightUnit);

        photoURL = getIntent().getStringExtra(Prefs.IMG_KEY);
        etCreateEmail.setText(getIntent().getStringExtra(Prefs.EMAIL_KEY));
        etCreateFirstName.setText(getIntent().getStringExtra(Prefs.FN_KEY));
        etCreateLastName.setText(getIntent().getStringExtra(Prefs.LN_KEY));
        etCreateContact.setText(getIntent().getStringExtra(Prefs.PHONE_KEY));

        etCreateBirthdate.setInputType(InputType.TYPE_NULL);
        etCreateBirthdate.setOnClickListener(v -> {
            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    etCreateBirthdate.setText(year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));
                }
            };
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    InappRegistrationActivity.this, datePickerListener,
                    mYear, mMonth, mDay);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePickerDialog.show();
        });

        btnCancelAccount.setOnClickListener(v -> finish());
        btnUpdateAccount.setOnClickListener(v -> {
            EditText[] editTexts = {etPassword, etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateHeight, etCreateWeight};
            inputValid = true;
            Arrays.stream(editTexts).forEach(e -> {
                if (e.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please input a valid " + e.getTag().toString() + ".", Toast.LENGTH_LONG).show();
                    inputValid = false;
                }
            });
            if (etCreateGender.getSelectedItemPosition() < 1) inputValid = false;
            if (etHeightUnit.getSelectedItemPosition() < 1) inputValid = false;
            if (!inputValid) return;

            AlertDialog alertDialog = new AlertDialog.Builder(InappRegistrationActivity.this).create();
            alertDialog.setTitle("Create Account");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to create this account?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (d, w) -> {

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (d, w) -> {

            });
            alertDialog.show();


        });
    }

}