package com.padyak.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.padyak.R;
import com.padyak.activity.AddEmergencyActivity;
import com.padyak.activity.EmergencyListActivity;
import com.padyak.activity.frmMemberAlertInfo;
import com.padyak.dto.EmergencyContact;
import com.padyak.dto.MemberAlert;
import com.padyak.utility.Helper;
import com.padyak.utility.Prefs;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterEmergencyContact extends RecyclerView.Adapter<adapterEmergencyContact.viewHolder>{
    List<EmergencyContact> emergencyContactList;

    public adapterEmergencyContact(List<EmergencyContact> emergencyContactList) {
        this.emergencyContactList = emergencyContactList;
    }

    @NonNull
    @Override
    public adapterEmergencyContact.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emergency_add,parent,false);
        adapterEmergencyContact.viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterEmergencyContact.viewHolder holder, int position) {
        holder.txRowName.setText(emergencyContactList.get(position).getFirstname().concat(" ").concat(emergencyContactList.get(position).getLastname()));
        holder.txRowMessage.setText(emergencyContactList.get(position).getContact());
    }

    @Override
    public int getItemCount() {
        return emergencyContactList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txRowName,txRowMessage;
        ImageView imgDelete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txRowName = itemView.findViewById(R.id.txRowName);
            txRowMessage = itemView.findViewById(R.id.txRowMessage);
            imgDelete = itemView.findViewById(R.id.imgDeleteContact);

            imgDelete.setOnClickListener(v->{
                EmergencyListActivity.emergencyListActivity.removeEmergencyContact(
                        emergencyContactList.get(getAdapterPosition()).getContact()
                );
            });
        }
    }
}
