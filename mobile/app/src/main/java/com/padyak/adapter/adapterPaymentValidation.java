package com.padyak.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.activity.PaymentValidationActivity;
import com.padyak.activity.frmMemberAlertInfo;
import com.padyak.dto.MemberAlert;
import com.padyak.dto.Participants;
import com.padyak.dto.UserValidation;
import com.padyak.fragment.fragmentUserUploaded;
import com.padyak.utility.Helper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterPaymentValidation extends RecyclerView.Adapter<adapterPaymentValidation.viewHolder>{
    List<UserValidation> participants;

    public adapterPaymentValidation(List<UserValidation> participants) {
        this.participants = participants;
    }

    @NonNull
    @Override
    public adapterPaymentValidation.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_validation,parent,false);
        adapterPaymentValidation.viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPaymentValidation.viewHolder holder, int position) {
        holder.txRowName.setText(participants.get(position).getUserName());
        //Picasso.get().load(participants.get(position).getUserImage()).into(holder.imgParticipant);
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txRowName;
        ImageView imgParticipant;
        private FragmentManager fm;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txRowName = itemView.findViewById(R.id.txRowName);
            imgParticipant = itemView.findViewById(R.id.imgParticipant);

            itemView.setOnClickListener(v->{
                PaymentValidationActivity paymentValidationActivity = PaymentValidationActivity.me;
                paymentValidationActivity.showUserUploaded();
            });
        }
    }
}
