package com.example.datn.UserFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datn.R;
import com.example.datn.OrderHistoryActivity;
import com.example.datn.MyReviewsActivity;
import com.example.datn.AddressListActivity;
import com.example.datn.BankAccountActivity;
import com.example.datn.SettingsActivity;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lịch sử đặt hàng
        view.findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(getContext(), OrderHistoryActivity.class))
        );

        // Đánh giá của tôi
        view.findViewById(R.id.btnMyReviews).setOnClickListener(v ->
                startActivity(new Intent(getContext(), MyReviewsActivity.class))
        );

        // Địa chỉ nhận hàng
        view.findViewById(R.id.btnAddresses).setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddressListActivity.class))
        );

        // Tài khoản ngân hàng
        view.findViewById(R.id.btnBank).setOnClickListener(v ->
                startActivity(new Intent(getContext(), BankAccountActivity.class))
        );

        // Cài đặt
        view.findViewById(R.id.btnSettings).setOnClickListener(v ->
                startActivity(new Intent(getContext(), SettingsActivity.class))
        );

        // Logout (hiện toast)
        ImageButton logoutButton = view.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(v ->
                Toast.makeText(getContext(), "Đăng xuất", Toast.LENGTH_SHORT).show()
        );
    }
}
