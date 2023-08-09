package com.padyak.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.activity.frmMemberAlertInfo;
import com.padyak.dto.MemberAlert;

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
        holder.txRowMessage.setText(alerts.get(position).getAlertDescription());
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txRowName,txRowMessage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txRowName = itemView.findViewById(R.id.txRowName);
            txRowMessage = itemView.findViewById(R.id.txRowMessage);

            itemView.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), frmMemberAlertInfo.class);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
