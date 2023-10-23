package com.padyak.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.padyak.R;
import com.padyak.activity.SsoLoginActivity;
import com.padyak.activity.VerificationActivity;
import com.padyak.activity.frmRide;
import com.padyak.activity.frmSaveRide;
import com.padyak.utility.Helper;
import com.padyak.utility.VolleyHttp;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragmentUserUploaded extends DialogFragment {
    ImageView imgUploaded, imgUser;
    TextView txUserNameUploaded;
    Button btnUploadConfirm, btnUploadDelete;
    AlertDialog alertDialog;
    static String userDisplayName, userUrl, paymentUrl, userID, eventID;
    View parentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_uploaded, container, false);
        imgUploaded = v.findViewById(R.id.imgUploaded);
        imgUser = v.findViewById(R.id.imgUser);
        txUserNameUploaded = v.findViewById(R.id.txUserNameUploaded);
        btnUploadConfirm = v.findViewById(R.id.btnUploadConfirm);
        btnUploadDelete = v.findViewById(R.id.btnUploadDelete);
        alertDialog = new AlertDialog.Builder(v.getContext()).create();
        alertDialog.setCancelable(false);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(userUrl).into(imgUser);
        //Picasso.get().load(paymentUrl).into(imgUploaded);
        txUserNameUploaded.setText(userDisplayName);

        btnUploadConfirm.setOnClickListener(c -> {
            alertDialog.setTitle("Payment Validation");
            alertDialog.setMessage("Are you sure you want to set this payment as Confirmed?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    (d, w) -> {

                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.show();
        });
        btnUploadDelete.setOnClickListener(c -> {
            dismiss();
        });
        parentView = v;
        return v;
    }

    public void updatePayment(String status) {
        btnUploadConfirm.setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("userId", userID);
        VolleyHttp volleyHttp = new VolleyHttp("/payment/".concat(eventID), params, "event-patch", parentView.getContext());
        String json = volleyHttp.getResponseBody(true);
        btnUploadConfirm.setEnabled(true);
        try {
            JSONObject reader = new JSONObject(json);
            int responseStatus = reader.getInt("status");
            if (responseStatus == 200) {
                Toast.makeText(parentView.getContext(), "Payment updated successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(parentView.getContext(), "Failed to update payment. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.d(Helper.getInstance().log_code, "onCreate: " + e.getMessage());
            Toast.makeText(parentView.getContext(), "Failed to communicate with server. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public static fragmentUserUploaded newInstance(String title, String username, String photoUrl, String userId, String eventId) {
        fragmentUserUploaded frag = new fragmentUserUploaded();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        userUrl = photoUrl;
        userDisplayName = username;
        userID = userId;
        eventID = eventId;
        return frag;
    }
}
