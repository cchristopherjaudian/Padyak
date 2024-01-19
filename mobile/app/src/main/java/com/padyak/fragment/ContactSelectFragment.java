package com.padyak.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.padyak.R;
import com.padyak.activity.AddEmergencyActivity;
import com.padyak.activity.EmergencyListActivity;

public class ContactSelectFragment extends DialogFragment {

    public ContactSelectFragment() {

    }

    public static ContactSelectFragment newInstance(String title) {
        ContactSelectFragment frag = new ContactSelectFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }

    Button btnSelectContact,txAddManually;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_select, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        btnSelectContact = v.findViewById(R.id.btnSelectContact);
        txAddManually = v.findViewById(R.id.txAddManually);

        btnSelectContact.setOnClickListener(l->{
            EmergencyListActivity.emergencyListActivity.browseContacts();
            dismiss();
        });
        txAddManually.setOnClickListener(l->{
            Intent intent = new Intent(v.getContext(), AddEmergencyActivity.class);
            startActivity(intent);
            dismiss();
        });
        return v;
    }
}