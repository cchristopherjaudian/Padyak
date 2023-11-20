package com.padyak.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.Prefs;
import com.padyak.utility.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class frmAccount extends AppCompatActivity {
    String authSource;
    String password;
    String[] gender = {"-Please select a gender-", "Male", "Female"};
    String[] heightUnit = {"-", "cm", "in"};
    String photoURL = "";
    ImageView imgShowPassword, imgShowPassword2;
    EditText txNewPassword, txConfirmPassword;
    ArrayAdapter<String> aaGender, aaHeightUnit;
    ConstraintLayout linearLayoutCompat2;
    EditText etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateBirthdate, etCreateHeight, etCreateWeight;
    Spinner etCreateGender, etHeightUnit;
    Button btnUpdateAccount, btnCancelAccount;
    String mobileNumber, emailAddress;
    boolean is_registration;
    boolean inputValid;
    public static frmAccount frmAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_account);
        Log.d(Helper.getInstance().log_code, "onCreate: " + LoggedUser.getInstance().getRefreshToken());
        frmAccount = this;
        authSource = getIntent().getStringExtra("source");
        is_registration = getIntent().getBooleanExtra("register", false);
        //mobileNumber = getIntent().getStringExtra("mobileNumber");
        emailAddress = getIntent().getStringExtra("emailAddress");
        //password = getIntent().getStringExtra("password");
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnCancelAccount = findViewById(R.id.btnCancelAccount);
        linearLayoutCompat2 = findViewById(R.id.linearLayoutCompat2);
        etHeightUnit = findViewById(R.id.etHeightUnit);
        etCreateEmail = findViewById(R.id.txAddEventTitle);
        etCreateFirstName = findViewById(R.id.etCreateFirstName);
        etCreateLastName = findViewById(R.id.etCreateLastName);
        etCreateContact = findViewById(R.id.etCreateContact);
        etCreateBirthdate = findViewById(R.id.etCreateBirthdate);
        etCreateHeight = findViewById(R.id.etCreateHeight);
        etCreateWeight = findViewById(R.id.etCreateWeight);
        etCreateGender = findViewById(R.id.etCreateGender);

        txNewPassword = findViewById(R.id.txNewPassword);
        txConfirmPassword = findViewById(R.id.txConfirmPassword);

        imgShowPassword = findViewById(R.id.imgShowPassword);
        imgShowPassword2 = findViewById(R.id.imgShowPassword2);
        imgShowPassword.setOnClickListener(v -> {
            if (txNewPassword.getInputType() == 129) {
                txNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                txNewPassword.setInputType(129);
            }
        });
        imgShowPassword2.setOnClickListener(v -> {
            if (txConfirmPassword.getInputType() == 129) {
                txConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                txConfirmPassword.setInputType(129);
            }
        });
        aaHeightUnit = new ArrayAdapter<String>(frmAccount.this, R.layout.sp_format, heightUnit);
        aaHeightUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etHeightUnit.setAdapter(aaHeightUnit);
        aaGender = new ArrayAdapter<String>(frmAccount.this, R.layout.sp_format, gender);
        aaGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCreateGender.setAdapter(aaGender);
        if (authSource.equals("SSO")) {
            linearLayoutCompat2.setVisibility(View.GONE);
            photoURL = getIntent().getStringExtra(Prefs.IMG_KEY);
            etCreateEmail.setText(getIntent().getStringExtra(Prefs.EMAIL_KEY));
            etCreateFirstName.setText(getIntent().getStringExtra(Prefs.FN_KEY));
            etCreateLastName.setText(getIntent().getStringExtra(Prefs.LN_KEY));
            etCreateContact.setText(getIntent().getStringExtra(Prefs.PHONE_KEY));
            etCreateEmail.setEnabled(false);

        } else {
            photoURL = getResources().getString(R.string.imgPlaceholder);
            etCreateContact.setText(mobileNumber);
            etCreateEmail.setEnabled(true);

        }


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
                    frmAccount.this, datePickerListener,
                    mYear, mMonth, mDay);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            long oneMonthAhead = c.getTimeInMillis();
            datePicker.setMaxDate(oneMonthAhead);
            datePickerDialog.show();
        });

        btnCancelAccount.setOnClickListener(v -> finish());
        btnUpdateAccount.setOnClickListener(v -> {
            EditText[] editTexts = {etCreateEmail, etCreateFirstName, etCreateLastName, etCreateContact, etCreateHeight, etCreateWeight};
            inputValid = true;
            Arrays.stream(editTexts).forEach(e -> {
                if (e.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please input a valid " + e.getTag().toString() + ".", Toast.LENGTH_LONG).show();
                    inputValid = false;
                }
            });
            if (etCreateGender.getSelectedItemPosition() < 1) inputValid = false;
            if (etHeightUnit.getSelectedItemPosition() < 1) inputValid = false;
            if (!Helper.getInstance().validateMobileNumber(etCreateContact.getText().toString().trim())) {
                Toast.makeText(frmAccount.this, "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
                return;
            }
            if (authSource.equals("IN_APP")) {
                if (!txNewPassword.getText().toString().trim().equals(txConfirmPassword.getText().toString().trim())) {
                    Toast.makeText(frmAccount.this, "New Password and Confirmed Password are not equal.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Helper.getInstance().checkString(txNewPassword.getText().toString().trim())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(frmAccount.this).create();
                    alertDialog.setTitle("Password Confirmation");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Password should contain these criterias:\nAtleast 8 Characters\nContains atleast 1 special character\nContains atleast 1 numeric value\nShould start with a capital letter");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            (d, w) -> {

                            });

                    alertDialog.show();
                    return;
                }
            }

            if (!inputValid){
                Toast.makeText(frmAccount, "Please validate all fields to continue", Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog alertDialog = new AlertDialog.Builder(frmAccount.this).create();
            alertDialog.setTitle("Create Account");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to create this account?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (d, w) -> {

            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (d, w) -> {
                Intent intent = new Intent(frmAccount.this, TermsActivity.class);
                Bundle b = new Bundle();
                if (authSource.equals("IN_APP")) {
                    b.putString("mobileNumber", etCreateContact.getText().toString().trim());
                    b.putString("source", "IN_APP");
                } else {
                    b.putString("source", "SSO");
                }
                intent.putExtras(b);
                startActivity(intent);

            });
            alertDialog.show();


        });

    }

    public void registerAccount() {
        ProgressDialog progressDialog = Helper.getInstance().progressDialog(frmAccount.this, "Processing request.");
        progressDialog.show();
        new Thread(() -> {
                Map<String, Object> paramsCreate = new HashMap<>();
                paramsCreate.put("contactNumber", etCreateContact.getText().toString());
                paramsCreate.put("password", authSource.equals("SSO") ? "Padyak123*" : txNewPassword.getText().toString());
                paramsCreate.put(Prefs.FN_KEY, etCreateFirstName.getText().toString().trim());
                paramsCreate.put(Prefs.LN_KEY, etCreateLastName.getText().toString().trim());
                paramsCreate.put(Prefs.EMAIL_KEY, etCreateEmail.getText().toString().trim());
                paramsCreate.put(Prefs.GENDER_KEY, etCreateGender.getSelectedItem().toString());
                paramsCreate.put(Prefs.BDAY_KEY, etCreateBirthdate.getText().toString().trim());
                paramsCreate.put(Prefs.HEIGHT_KEY, etCreateHeight.getText().toString().trim() + "@" + etHeightUnit.getSelectedItem().toString());
                paramsCreate.put(Prefs.WEIGHT_KEY, etCreateWeight.getText().toString().trim());
                paramsCreate.put(Prefs.IMG_KEY, photoURL);
                paramsCreate.put("source", authSource);
                VolleyHttp volleyHttpCreate = new VolleyHttp("/inapp/profile", paramsCreate, "user", frmAccount.this);
                String json = volleyHttpCreate.getResponseBody(false);
                try {
                    JSONObject reader = new JSONObject(json);
                    int responseStatus = reader.getInt("status");
                    if (responseStatus == 200) {
                        if (!authSource.equals("SSO"))
                            Prefs.getInstance().setUser(frmAccount.this, Prefs.PASSWORD_KEY, txNewPassword.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.IMG_KEY, photoURL);
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.FN_KEY, etCreateFirstName.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.LN_KEY, etCreateLastName.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.EMAIL_KEY, etCreateEmail.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.GENDER_KEY, etCreateGender.getSelectedItem().toString());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.BDAY_KEY, etCreateBirthdate.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.PHONE_KEY, etCreateContact.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.WEIGHT_KEY, etCreateWeight.getText().toString().trim());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.HEIGHT_KEY, etCreateHeight.getText().toString().trim() + "@" + etHeightUnit.getSelectedItem().toString());
                        Prefs.getInstance().setUser(frmAccount.this, Prefs.AUTH, authSource);

                        runOnUiThread(() -> {
                            progressDialog.dismiss();

                            if (null != VerificationActivity.verificationActivity)
                                VerificationActivity.verificationActivity.finish();
                            if (null != InAppActivity.inAppActivity)
                                InAppActivity.inAppActivity.finish();

                            Intent intent = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });

                    } else {
                        throw new Exception("Invalid patch response code " + responseStatus);
                    }

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(frmAccount, "Failed to register account, Please try again.", Toast.LENGTH_LONG).show();
                    });
                }

        }).start();
    }
}