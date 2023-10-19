package com.padyak.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.padyak.R;
import com.padyak.activity.frmRide;
import com.padyak.activity.frmSaveRide;
import com.squareup.picasso.Picasso;

public class fragmentUserUploaded extends DialogFragment {
    ImageView imgUploaded;
    Button btnUploadConfirm,btnUploadDelete;
    AlertDialog alertDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_uploaded, container, false);
        imgUploaded = v.findViewById(R.id.imgUploaded);
        btnUploadConfirm = v.findViewById(R.id.btnUploadConfirm);
        btnUploadDelete = v.findViewById(R.id.btnUploadDelete);
        alertDialog = new AlertDialog.Builder(v.getContext()).create();
        alertDialog.setCancelable(false);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load("https://placehold.co/200").into(imgUploaded);

        btnUploadConfirm.setOnClickListener(c->{
            alertDialog.setTitle("Payment Validation");
            alertDialog.setMessage("Are you sure you want to set this payment as Confirmed?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    (d,w)->{

                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.show();
        });
        btnUploadDelete.setOnClickListener(c->{
            alertDialog.setTitle("Payment Validation");
            alertDialog.setMessage("Are you sure you want to delete this payment?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    (d,w)->{

                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {

            });
            alertDialog.show();
        });
        return v;
    }
    public static fragmentUserUploaded newInstance(String title) {
        fragmentUserUploaded frag = new fragmentUserUploaded();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
}
