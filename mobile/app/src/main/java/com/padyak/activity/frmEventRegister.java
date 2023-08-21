package com.padyak.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.padyak.R;
import com.padyak.utility.Helper;
import com.padyak.utility.LoggedUser;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class frmEventRegister extends AppCompatActivity {
    Button btnEventRegister, btnEventCancel,btnRegisterPayment;
    TextView txEventName,txProofPayment;
    ImageView imgEventRegister;
    CheckBox checkBox;
    Bitmap bitmapPayment;
    byte[] data;
    FirebaseStorage storage;
    StorageReference storageRef, eventRef;
    UploadTask uploadTask;
    String eventId,eventName,eventImg;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_event_register);

        btnRegisterPayment = findViewById(R.id.btnRegisterPayment);
        btnEventRegister = findViewById(R.id.btnEventRegister);
        btnEventCancel = findViewById(R.id.btnEventCancel);
        checkBox = findViewById(R.id.checkBox);

        txProofPayment = findViewById(R.id.txProofPayment);
        txEventName = findViewById(R.id.txEventName);
        imgEventRegister = findViewById(R.id.imgEventRegister);

        eventId = getIntent().getStringExtra("id");
        eventName= getIntent().getStringExtra("name");
        eventImg= getIntent().getStringExtra("photoUrl");

        txEventName.setText(eventName);
        Picasso.get().load(eventImg).into(imgEventRegister);

        btnEventCancel.setOnClickListener(v -> finish());
        btnRegisterPayment.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 814);
        });
        btnEventRegister.setOnClickListener(v -> {
            if(!checkBox.isChecked()){
                Toast.makeText(this, "Please tick the waiver agreement to proceed.", Toast.LENGTH_LONG).show();
                return;
            }
            if(bitmapPayment == null){
                Toast.makeText(this, "Please attach a screenshot of payment proof to proceed.", Toast.LENGTH_LONG).show();
                return;
            }
            new Thread(()->{
                runOnUiThread(this::processRegistration);
            }).start();
           
        });
    }


    public void processRegistration(){
        progressDialog = Helper.getInstance().progressDialog(frmEventRegister.this,"Processing registration.");
        progressDialog.show();
        new Thread(()->{
            String ref = "payment/" + LoggedUser.getInstance().getUuid() + "/" + LocalDateTime.now().toString() + ".jpg";
            FirebaseApp.initializeApp(this);
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            eventRef = storageRef.child(ref);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapPayment.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
            uploadTask = eventRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    savePayment("");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            savePayment(uri.toString());
                        }
                    });

                }
            });
        }).start();
    }
    private void savePayment(String imgURI){
        Map<String, Object> payload = new HashMap<>();
        payload.put("paymentUrl", imgURI);

        VolleyHttp volleyHttp = new VolleyHttp("/cyclist/".concat(eventId),payload,"event-patch",frmEventRegister.this);
        JSONObject responseJSON = volleyHttp.getJsonResponse(true);
        runOnUiThread(()->{
            progressDialog.dismiss();
            if(responseJSON == null){
                Toast.makeText(this, "Failed to process request. Please try again.", Toast.LENGTH_SHORT).show();
            } else{
                Intent intent = new Intent(frmEventRegister.this, frmEventParticipants.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",eventId);
                bundle.putString("name",eventName);
                bundle.putString("img",eventImg);
                intent.putExtras(bundle);
                startActivity(intent);
                frmEventInfo.frmEventInfo.finish();
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 814 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                bitmapPayment = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                txProofPayment.setText("Image Attached Successfully");
            } catch (IOException e) {
                bitmapPayment = null;
                Toast.makeText(this, "Failed to retrieve image. Please try again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {

    }
}