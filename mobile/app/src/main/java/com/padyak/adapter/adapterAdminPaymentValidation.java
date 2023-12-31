package com.padyak.adapter;

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
import com.padyak.activity.frmEventParticipants;
import com.padyak.dto.UserValidation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterAdminPaymentValidation extends RecyclerView.Adapter<adapterAdminPaymentValidation.viewHolder>{
    List<UserValidation> participants;

    public adapterAdminPaymentValidation(List<UserValidation> participants) {
        this.participants = participants;
    }

    @NonNull
    @Override
    public adapterAdminPaymentValidation.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_validation,parent,false);
        adapterAdminPaymentValidation.viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterAdminPaymentValidation.viewHolder holder, int position) {
        holder.txRowName.setText(participants.get(position).getUserName());
        holder.txPaymentStatus.setText(participants.get(position).getPaymentStatus());
        Picasso.get().load(participants.get(position).getUserImage()).into(holder.imgParticipant);
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txRowName,txPaymentStatus;
        ImageView imgParticipant;
        private FragmentManager fm;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txRowName = itemView.findViewById(R.id.txRowName);
            imgParticipant = itemView.findViewById(R.id.imgParticipant);
            txPaymentStatus = itemView.findViewById(R.id.txPaymentStatus);
            itemView.setOnClickListener(v->{
                PaymentValidationActivity f = PaymentValidationActivity.me;
                f.showUserUploaded(participants.get(getAdapterPosition()).getUserName(),participants.get(getAdapterPosition()).getUserImage(),participants.get(getAdapterPosition()).getUserId(),participants.get(getAdapterPosition()).getPaymentURL());
            });
        }
    }
}
