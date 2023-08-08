package com.padyak.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padyak.R;

import java.util.List;

public class adapterNewsfeed extends RecyclerView.Adapter<adapterNewsfeed.viewHolder> {

    List<String> _nfname,_nfstart,_nfend,_nfdistance,_nftimestart,_nftimeend;

    public adapterNewsfeed(List<String> _nfname, List<String> _nfstart, List<String> _nfend, List<String> _nfdistance, List<String> _nftimestart, List<String> _nftimeend) {
        this._nfname = _nfname;
        this._nfstart = _nfstart;
        this._nfend = _nfend;
        this._nfdistance = _nfdistance;
        this._nftimestart = _nftimestart;
        this._nftimeend = _nftimeend;
    }

    @NonNull
    @Override
    public adapterNewsfeed.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_newsfeed,parent,false);
        viewHolder viewHolder = new viewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterNewsfeed.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return _nfname.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
