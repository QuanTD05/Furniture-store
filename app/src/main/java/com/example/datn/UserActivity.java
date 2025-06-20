package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.datn.UserFragment.ContactFragment;
import com.example.datn.UserFragment.FavoriteFragment;
import com.example.datn.UserFragment.HomeFragment;
import com.example.datn.UserFragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Mặc định mở HomeFragment
        loadFragment(new HomeFragment());
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Lắng nghe sự kiện chọn menu bottom nav
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_favorite) {
                selectedFragment = new FavoriteFragment();
            } else if (itemId == R.id.nav_contact) {
                selectedFragment = new ContactFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // Kiểm tra xem có yêu cầu mở fragment cụ thể từ intent không
        handleIntent(getIntent());
    }

    // Được gọi khi có intent mới (ví dụ quay lại từ OrderHistoryActivity)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("UserActivity", "onNewIntent(): target_fragment="
                + intent.getStringExtra("target_fragment"));
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String targetFragment = intent.getStringExtra("target_fragment");
        if ("profile".equals(targetFragment)) {
            loadFragment(new ProfileFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
}
