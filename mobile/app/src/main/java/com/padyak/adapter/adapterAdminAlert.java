package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;

import java.util.List;

public class adapterAdminAlert extends RecyclerView.Adapter<adapterAdminAlert.viewHolder>{
    List<String> _name, _message;

    public adapterAdminAlert(List<String> _name, List<String> _message) {
        this._name = _name;
        this._message = _message;
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
        holder.txRowName.setText(_name.get(position));
        holder.txRowMessage.setText(_message.get(position));
    }

    @Override
    public int getItemCount() {
        return _name.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txRowName,txRowMessage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txRowName = itemView.findViewById(R.id.txRowName);
            txRowMessage = itemView.findViewById(R.id.txRowMessage);
        }
    }
}
