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

import java.util.Collections;
import java.util.List;

public class adapterAlertGroup extends RecyclerView.Adapter<adapterAlertGroup.viewHolder>{
    List<String> _name,_contact;
    List<Boolean> _isselected;

    public adapterAlertGroup(List<String> _name, List<String> _contact, List<Boolean> _isselected) {
        this._name = _name;
        this._contact = _contact;
        this._isselected = _isselected;
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
        holder.txRowName.setText(_name.get(position));
        holder.checkBox.setChecked(_isselected.get(position));
    }

    @Override
    public int getItemCount() {
        return _name.size();
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
        Collections.fill(_isselected,is_checked);
    }
}
