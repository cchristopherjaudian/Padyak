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

import com.padyak.R;
import com.padyak.activity.frmMemberAlertInfo;
import com.padyak.dto.MemberAlert;
import com.padyak.utility.Helper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterAdminAlert extends RecyclerView.Adapter<adapterAdminAlert.viewHolder>{
    List<MemberAlert> alerts;

    public adapterAdminAlert(List<MemberAlert> alerts) {
        this.alerts = alerts;
    }

    @NonNull
    @Override
    public adapterAdminAlert.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_admin_alert,parent,false);
        adapterAdminAlert.viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterAdminAlert.viewHolder holder, int position) {
        holder.txRowName.setText(alerts.get(position).getUserName());
        holder.txRowMessage.setText(Helper.getInstance().getAlertDescription(alerts.get(position).getAlertLevel()));
        Picasso.get().load(alerts.get(position).getUserImage()).into(holder.imgParticipant);
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txRowName,txRowMessage;
        ImageView imgParticipant;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txRowName = itemView.findViewById(R.id.txRowName);
            txRowMessage = itemView.findViewById(R.id.txRowMessage);
            imgParticipant = itemView.findViewById(R.id.imgParticipant);

            itemView.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), frmMemberAlertInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",alerts.get(getAdapterPosition()).getAlertId());
                bundle.putString("name",alerts.get(getAdapterPosition()).getUserName());
                bundle.putString("photoUrl",alerts.get(getAdapterPosition()).getUserImage());
                bundle.putDouble("latitude",alerts.get(getAdapterPosition()).getLatitude());
                bundle.putDouble("longitude",alerts.get(getAdapterPosition()).getLongitude());
                bundle.putString("location",alerts.get(getAdapterPosition()).getLocationName());
                bundle.putString("date",alerts.get(getAdapterPosition()).getCreatedAt());
                bundle.putInt("level",alerts.get(getAdapterPosition()).getAlertLevel());
                intent.putExtras(bundle);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
