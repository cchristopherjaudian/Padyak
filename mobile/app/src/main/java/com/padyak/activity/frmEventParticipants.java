package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.padyak.R;
import com.padyak.adapter.adapterParticipant;
import com.padyak.adapter.adapterPaymentValidation;
import com.padyak.dto.Participants;
import com.padyak.dto.UserValidation;
import com.padyak.fragment.fragmentEvent;
import com.padyak.fragment.fragmentUserUploaded;
import com.padyak.utility.CustomViewPager;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class frmEventParticipants extends AppCompatActivity {
    public static frmEventParticipants me;
    com.padyak.adapter.adapterPaymentValidation adapterPaymentValidation;
    RecyclerView rvEventInfoParticipants;
    LinearLayoutManager linearLayoutManager;
    com.padyak.adapter.adapterParticipant adapterParticipant;
    TextView txEventName, txParticipantDescription;
    ImageView imgEventParticipant;
    Button btnEventCancel;
    CustomViewPager viewPager;
    SmartTabLayout viewPagerTab;
    FragmentPagerItemAdapter adapter;
    List<Participants> participantsList;
    static String eventId,eventName,eventImg;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_participants);
        me = this;
        txParticipantDescription = findViewById(R.id.txParticipantDescription);
        txEventName = findViewById(R.id.txEventName);
        imgEventParticipant = findViewById(R.id.imgEventParticipant);
        eventId = getIntent().getStringExtra("id");
        eventName = getIntent().getStringExtra("name");
        eventImg = getIntent().getStringExtra("img");

        txEventName.setText(eventName);
        Picasso.get().load(eventImg).into(imgEventParticipant);

        rvEventInfoParticipants = findViewById(R.id.rvEventInfoParticipants);
        btnEventCancel = findViewById(R.id.btnEventCancel);
        viewPager = findViewById(R.id.customviewpager);
        viewPagerTab = findViewById(R.id.viewpagertab);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvEventInfoParticipants.setLayoutManager(linearLayoutManager);
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Participants", fragmentEvent.class)
                .create());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.disableScroll(true);

        btnEventCancel.setOnClickListener(v -> finish());
        loadParticipants();

    }

    public void loadParticipants() {
        progressDialog = Helper.getInstance().progressDialog(frmEventParticipants.this,"Retrieving participants.");
        progressDialog.show();
        new Thread(()->{
            try {
                List<UserValidation> participants = new ArrayList<>();
                VolleyHttp volleyHttp = new VolleyHttp("/".concat(eventId), null, "event", frmEventParticipants.this);
                JSONObject responseJSON = volleyHttp.getJsonResponse(true);
                if (responseJSON == null) throw new Exception("responseJSON is null");
                JSONObject eventObject = responseJSON.getJSONObject("data");
                JSONArray participantJSON = eventObject.optJSONArray("registeredUser");

                for (int i = 0; i < participantJSON.length(); i++) {
                    String paymentStatus = participantJSON.getJSONObject(i).getString("status");
                    String paymentURL = participantJSON.getJSONObject(i).getString("paymentUrl");
                    JSONObject participantObject = participantJSON.getJSONObject(i).getJSONObject("user");
                    participants.add(new UserValidation(participantObject.getString("id"),participantObject.getString("firstname").concat(" ").concat(participantObject.getString("lastname")),participantObject.getString("photoUrl"),paymentStatus,paymentURL));
                }
                runOnUiThread(()->{
                    adapterPaymentValidation = new adapterPaymentValidation(participants);
                    rvEventInfoParticipants.setAdapter(adapterPaymentValidation);
                });

            } catch (Exception e) {
                Log.d(Helper.getInstance().log_code, "loadParticipants: " + e.getMessage());
                runOnUiThread(()-> Toast.makeText(this, "Failed to load list of participants.", Toast.LENGTH_LONG).show());
            } finally {
                runOnUiThread(()-> progressDialog.dismiss());
            }
        }).start();
    }
    public static void showUserUploaded(String username, String photoUrl, String userId, String paymentURL){
        FragmentManager fm = me.getSupportFragmentManager();
        fragmentUserUploaded editNameDialogFragment = fragmentUserUploaded.newInstance("UserUpload",username,photoUrl,userId,eventId,paymentURL);
        editNameDialogFragment.show(fm, "UserUpload");
    }
    @Override
    public void onBackPressed() {

    }
}