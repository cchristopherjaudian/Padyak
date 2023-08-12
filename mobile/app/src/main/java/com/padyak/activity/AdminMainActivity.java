package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.padyak.R;
import com.padyak.utility.LoggedUser;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AdminMainActivity extends AppCompatActivity {

    RelativeLayout rlEvents,rlAlert,rlTrack;
    TextView txMainProfileName,txProfileDay;
    ImageView imgAdminProfile;

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
        String dayToday = dateNow.getDayOfWeek().getDisplayName(TextStyle.FULL , Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        txProfileDay.setText(dayToday.toUpperCase().concat("|").concat(dateNow.format(formatter)));

        rlEvents.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this,frmEventCrud.class);
            startActivity(intent);
        });
        rlAlert.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this,frmAdminAlert.class);
            startActivity(intent);
        });
        rlTrack.setOnClickListener(v -> {
            intent = new Intent(AdminMainActivity.this,frmTrackEvent.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

    }
}