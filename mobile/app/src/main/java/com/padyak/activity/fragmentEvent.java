package com.padyak.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class fragmentEvent extends Fragment {
    public fragmentEvent() {
    }
    public static fragmentEvent newInstance() {
        fragmentEvent fragment = new fragmentEvent();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
