package com.example.datn.AdminFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.datn.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Constructor rỗng bắt buộc
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout fragment_profile
        return inflater.inflate(R.layout.fragment_profil_admin, container, false);
    }
}
