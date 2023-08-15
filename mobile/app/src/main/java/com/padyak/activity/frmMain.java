package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.padyak.R;
import com.padyak.adapter.adapterCoverPhoto;
import com.padyak.adapter.adapterNewsfeed;
import com.padyak.dto.Comment;
import com.padyak.dto.CoverPhoto;
import com.padyak.dto.Like;
import com.padyak.dto.Newsfeed;
import com.padyak.dto.PostAuthor;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class frmMain extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    ScrollView frame_home, frame_profile;
    ConstraintLayout frame_newsfeed;
    RecyclerView rvCoverPhoto, rvNewsfeed;
    LinearLayoutManager llmCoverPhoto, llNewsfeed;
    List<Newsfeed> newsfeedList;
    List<CoverPhoto> coverPhotoList;
    com.padyak.adapter.adapterCoverPhoto adapterCoverPhoto;
    com.padyak.adapter.adapterNewsfeed adapterNewsfeed;
    TextView txMainProfileName,txProfileName,txProfileDay;
    ImageView imgMainProfileDP,imgProfileDP;
    RelativeLayout rlEvents, rlAlert, rlHospital, rlRepair, rlPolice, rlRiding;
    Intent intent;

    public static frmMain frmMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_main);
        frmMain = this;
        txProfileDay = findViewById(R.id.txProfileDay);
        imgProfileDP = findViewById(R.id.imgProfileDP);
        imgMainProfileDP = findViewById(R.id.imgMainProfileDP);

        txMainProfileName = findViewById(R.id.txMainProfileName);
        txProfileName = findViewById(R.id.txProfileName);

        bottomBar = findViewById(R.id.bottomBar);
        frame_home = findViewById(R.id.frame_home);
        frame_profile = findViewById(R.id.frame_profile);
        frame_newsfeed = findViewById(R.id.frame_newsfeed);

        rvCoverPhoto = findViewById(R.id.rvCoverPhoto);
        rvNewsfeed = findViewById(R.id.rvNewsfeed);

        llmCoverPhoto = new LinearLayoutManager(this);
        llNewsfeed = new LinearLayoutManager(this);
        llmCoverPhoto.setOrientation(RecyclerView.HORIZONTAL);
        llNewsfeed.setOrientation(RecyclerView.VERTICAL);

        rvCoverPhoto.setLayoutManager(llmCoverPhoto);
        rvNewsfeed.setLayoutManager(llNewsfeed);

        rlEvents = findViewById(R.id.rlEvents);
        rlAlert = findViewById(R.id.rlAlert);
        rlHospital = findViewById(R.id.rlTrack);
        rlRepair = findViewById(R.id.rlRepair);
        rlPolice = findViewById(R.id.rlPolice);
        rlRiding = findViewById(R.id.rlRiding);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvCoverPhoto);


        txMainProfileName.setText("Hey ".concat(LoggedUser.getInstance().getFirstName()));
        txProfileName.setText(LoggedUser.getInstance().getFirstName().concat(" ").concat(LoggedUser.getInstance().getLastName()));
        LocalDate dateNow = LocalDate.now();
        String dayToday = dateNow.getDayOfWeek().getDisplayName(TextStyle.FULL , Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        txProfileDay.setText(dayToday.toUpperCase().concat("|").concat(dateNow.format(formatter)));

        try {
            Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgProfileDP);
            Picasso.get().load(LoggedUser.getInstance().getImgUrl()).into(imgMainProfileDP);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            if (i == 0) {
                loadCoverPhoto();
                frame_home.setVisibility(View.GONE);
                frame_profile.setVisibility(View.VISIBLE);
                frame_newsfeed.setVisibility(View.GONE);
            } else if (i == 1) {
                new Thread(()->{
                    runOnUiThread(this::loadNewsfeed);
                }).start();

                frame_home.setVisibility(View.GONE);
                frame_profile.setVisibility(View.GONE);
                frame_newsfeed.setVisibility(View.VISIBLE);
            } else {
                frame_home.setVisibility(View.VISIBLE);
                frame_profile.setVisibility(View.GONE);
                frame_newsfeed.setVisibility(View.GONE);
            }
            return false;
        });
        rlEvents.setOnClickListener(v -> {
            intent = new Intent(frmMain.this, frmEventCalendar.class);
            startActivity(intent);
        });
        rlAlert.setOnClickListener(v -> {
            intent = new Intent(frmMain.this, frmAlertInfo.class);
            startActivity(intent);
        });

        rlHospital.setOnClickListener(v -> {
            intent = new Intent(frmMain.this, frmFindLocation.class);
            Bundle bundle = new Bundle();
            bundle.putString("find", "Hospital");
            intent.putExtras(bundle);
            startActivity(intent);
        });
        rlRepair.setOnClickListener(v -> {
            intent = new Intent(frmMain.this, frmFindLocation.class);
            Bundle bundle = new Bundle();
            bundle.putString("find", "Repair Shop");
            intent.putExtras(bundle);
            startActivity(intent);
        });
        rlPolice.setOnClickListener(v -> {
            intent = new Intent(frmMain.this, frmFindLocation.class);
            Bundle bundle = new Bundle();
            bundle.putString("find", "Police Station");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        rlRiding.setOnClickListener(v->{
            intent = new Intent(frmMain.this, frmRide.class);
            startActivity(intent);
        });
        loadCoverPhoto();

    }
    public void notifyNewsfeed(){
        adapterNewsfeed.notifyDataSetChanged();
    }
    public void loadCoverPhoto() {
        coverPhotoList = new ArrayList<>();
        coverPhotoList.add(new CoverPhoto("","",R.drawable.bike1));
        coverPhotoList.add(new CoverPhoto("","",R.drawable.bike2));
        coverPhotoList.add(new CoverPhoto("","",R.drawable.bike3));
        adapterCoverPhoto = new adapterCoverPhoto(coverPhotoList);
        rvCoverPhoto.setAdapter(adapterCoverPhoto);
    }

    public void loadNewsfeed() {
        try {
            VolleyHttp volleyHttp = new VolleyHttp("",null,"post",frmMain.this);
            String json = volleyHttp.getResponseBody(true);
            JSONObject reader = new JSONObject(json);
            int responseStatus = reader.getInt("status");
            if(responseStatus == 200){
                newsfeedList = new ArrayList<>();

                JSONArray postArray = reader.optJSONArray("data");
                for(int i=0; i < postArray.length(); i++){
                    try {
                        List<Comment> commentList = new ArrayList<>();
                        List<Like> likeList = new ArrayList<>();
                        JSONObject postObject = postArray.getJSONObject(i);
                        if(!postObject.has("id")) throw new Exception("");
                        JSONObject authorObject = postArray.getJSONObject(i).getJSONObject("author");
                        Newsfeed newsfeed = new Newsfeed();
                        PostAuthor postAuthor = new PostAuthor();
                        postAuthor.setFirstname(authorObject.getString("firstname"));
                        postAuthor.setLastname(authorObject.getString("lastname"));
                        postAuthor.setFirstname(authorObject.getString("firstname"));
                        postAuthor.setPhotoUrl(authorObject.getString("photoUrl"));

                        newsfeed.setPostAuthor(postAuthor);
                        newsfeed.setCaption(postObject.getString("caption"));
                        newsfeed.setCreatedAt(postObject.getString("createdAt"));
                        newsfeed.setDistance(postObject.getString("distance").concat("km"));
                        newsfeed.setFromLat(postObject.getString("fromLat"));
                        newsfeed.setFromLocation(postObject.getString("fromLocation"));
                        newsfeed.setFromLong(postObject.getString("fromLong"));
                        newsfeed.setMovingTime(postObject.getString("movingTime"));
                        newsfeed.setPhotoUrl(postObject.getString("photoUrl"));
                        newsfeed.setPost(postObject.getString("post"));
                        newsfeed.setToLat(postObject.getString("toLat"));
                        newsfeed.setToLocation(postObject.getString("toLocation"));
                        newsfeed.setToLong(postObject.getString("toLong"));
                        newsfeed.setId(postObject.getString("id"));

                        JSONArray commentArray = postObject.optJSONArray("comments");
                        JSONArray likeArray = postObject.optJSONArray("likes");

                        for(int j = 0; j < commentArray.length(); j++){

                            JSONObject commentObject = commentArray.getJSONObject(j);
                            Comment c = new Comment();
                            c.setComment(commentObject.getString("comment"));
                            c.setDisplayName(commentObject.getString("displayName"));
                            c.setUserId(commentObject.getString("userId"));
                            c.setPhotoUrl(commentObject.getString("photoUrl"));
                            commentList.add(c);
                        }
                        for(int j = 0; j < likeArray.length(); j++){
                            JSONObject likeObject = likeArray.getJSONObject(j);
                            Like l = new Like();
                            l.setDisplayName(likeObject.getString("displayName"));
                            l.setUserId(likeObject.getString("userId"));
                            l.setPhotoUrl(likeObject.getString("photoUrl"));
                            likeList.add(l);
                        }
                        newsfeed.setLikeList(likeList);
                        newsfeed.setCommentList(commentList);

                        newsfeedList.add(newsfeed);
                    } catch (Exception jsonErr){
                        Log.d("Log_Padyak", "loadNewsfeed: " + jsonErr.getMessage());
                    }
                }
                adapterNewsfeed = new adapterNewsfeed(newsfeedList,getSupportFragmentManager());
                rvNewsfeed.setAdapter(adapterNewsfeed);
            } else{
                Toast.makeText(this, "Failed to retrieve newsfeed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("Log_Padyak", "loadNewsfeed: " + e.getMessage());
            Toast.makeText(this, "Failed to retrieve newsfeed. Please try again.", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onBackPressed() {

    }
}