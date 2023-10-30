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
import com.squareup.picasso.Picasso;

public class fragmentViewQR extends DialogFragment {
    ImageView imgUploaded;
    Button btnDoneQR;
    static String paymentQR;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_qr, container, false);
        imgUploaded = v.findViewById(R.id.imgUploaded);

        Picasso.get().load(paymentQR).into(imgUploaded);
        btnDoneQR = v.findViewById(R.id.btnDoneQR);
        btnDoneQR.setOnClickListener(c->{
            dismiss();
        });
        return v;
    }
    public static fragmentViewQR newInstance(String title, String pQR) {
        fragmentViewQR frag = new fragmentViewQR();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        paymentQR = pQR;
        return frag;
    }
}
