package com.example.datn;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.datn.StaffFragment.ContactFragment;
import com.example.datn.StaffFragment.HomeFragment;
import com.example.datn.StaffFragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StaffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load mặc định
        loadFragment(new com.example.datn.StaffFragment.HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home_staff) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_contact_staff) {
                selectedFragment = new ContactFragment();
            } else if (itemId == R.id.nav_profile_staff) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
}