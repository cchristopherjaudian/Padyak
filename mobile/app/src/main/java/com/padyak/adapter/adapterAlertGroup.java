package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;
import com.padyak.dto.GroupContact;

import java.util.Collections;
import java.util.List;

public class adapterAlertGroup extends RecyclerView.Adapter<adapterAlertGroup.viewHolder>{
    List<GroupContact> contact;

    public adapterAlertGroup(List<GroupContact> contact) {
        this.contact = contact;
    }

    @NonNull
    @Override
    public adapterAlertGroup.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group_contact,parent,false);
        adapterAlertGroup.viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterAlertGroup.viewHolder holder, int position) {
        //holder.txRowName.setText(contact.get(position).getUserName());
        holder.txRowName.setText("Angelo Carlos");
        holder.checkBox.setChecked(contact.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return contact.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txRowName;
        CheckBox checkBox;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgParticipant);
            txRowName = itemView.findViewById(R.id.txRowName);
            checkBox = itemView.findViewById(R.id.checkBox2);
        }
    }
    public void setCheck(boolean is_checked){
        contact.forEach((c)-> c.setSelected(is_checked));
    }
}
