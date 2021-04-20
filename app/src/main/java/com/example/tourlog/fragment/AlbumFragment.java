package com.example.tourlog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tourlog.R;

/**
 * 相册
 */
public class AlbumFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, null);
        iniview(view);
        initdata();
        initevnet();
        return view;
    }

    private void iniview(View view) {

    }

    private void initdata() {
        
    }

    private void initevnet() {
        
    }

}
