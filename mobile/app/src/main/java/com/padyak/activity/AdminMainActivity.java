package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AdminMainActivity extends AppCompatActivity {

    RelativeLayout rlEvents, rlAlert, rlTrack;
    TextView txMainProfileName, txProfileDay;
    ImageView imgAdminProfile;
    ProgressDialog progressDialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        rlEvents = findViewById(R.id.rlEvents);
        rlAlert = findViewById(R.id.rlAlert);
        rlTrack = findViewById(R.id.rlTrack);

        txMainProfileName = findViewById(R.id.txMainProfileName);
        txProfileDay = findViewById(R.id.txProfileDay);
        imgAdminProfile = findViewById(R.id.imgAdminProfile);

        txMainProfileName.setText("Hey ".concat(LoggedUser.getInstance().getFirstName()));
        Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgAdminProfile);
        LocalDate dateNow = LocalDate.now();
        String dayToday = dateNow.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        txProfileDay.setText(dayToday.toUpperCase().concat("|").concat(dateNow.format(formatter)));

        rlEvents.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this, frmEventCrud.class);
            startActivity(intent);
        });
        rlAlert.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this, frmAdminAlert.class);
            startActivity(intent);
        });
        rlTrack.setOnClickListener(v -> {
            progressDialog = Helper.getInstance().progressDialog(AdminMainActivity.this, "Retrieving current event.");
            progressDialog.show();
            new Thread(() -> {
                VolleyHttp volleyHttp = new VolleyHttp("/now", null, "event", AdminMainActivity.this);
                JSONObject jsonObject = volleyHttp.getJsonResponse(true);
                runOnUiThread(() -> {
                    if (jsonObject == null) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Failed to retrieve current event. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        progressDialog.dismiss();
                        JSONObject eventObject = jsonObject.optJSONArray("data").getJSONObject(0);
                        String eventId = eventObject.getString("id");
                        String eventName = eventObject.getString("name");
                        String eventPhoto = eventObject.getString("photoUrl");
                        Bundle b = new Bundle();
                        b.putString("id", eventId);
                        b.putString("name", eventName);
                        b.putString("photoUrl", eventPhoto);
                        intent = new Intent(AdminMainActivity.this, frmTrackEvent.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    } catch (JSONException e) {

                        Toast.makeText(this, "Failed to retrieve current event. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
                    }
                });

            }).start();


        });
    }

    @Override
    public void onBackPressed() {

    }
}