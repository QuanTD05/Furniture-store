package com.example.datn.AdminFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import com.example.datn.StaffListActivity;

import com.example.datn.R;

// Thêm import các activity khác nếu có

public class ProfileFragment extends Fragment {

    private LinearLayout menuStaff, menuUser, menuFood, menuReview, menuRevenue;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_admin, container, false);

        // Ánh xạ layout
        menuStaff = view.findViewById(R.id.menuList);
        menuUser = view.findViewById(R.id.menuUser);
        menuFood = view.findViewById(R.id.menuFood);
        menuReview = view.findViewById(R.id.menuReview);
        menuRevenue = view.findViewById(R.id.menuRevenue);

        // Xử lý chuyển màn hình
        menuStaff.setOnClickListener(v -> {
            startActivity(new Intent(getContext(),StaffListActivity.class ));
        });

        menuUser.setOnClickListener(v -> {
            // startActivity(new Intent(getContext(), UserManagerActivity.class));
        });

        menuFood.setOnClickListener(v -> {
            // startActivity(new Intent(getContext(), FoodManagerActivity.class));
        });

        menuReview.setOnClickListener(v -> {
            // startActivity(new Intent(getContext(), ReviewManagerActivity.class));
        });

        menuRevenue.setOnClickListener(v -> {
            // startActivity(new Intent(getContext(), RevenueStatisticsActivity.class));
        });

        return view;
    }
}
