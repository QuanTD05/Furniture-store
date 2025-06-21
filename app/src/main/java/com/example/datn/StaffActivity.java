package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.ImageButton;
import com.example.datn.StaffFragment.ContactFragment;
import com.example.datn.StaffFragment.HomeFragment;
import com.example.datn.UserFragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.example.datn.StaffFragment.OrdersFragment;
import com.example.datn.StaffFragment.UsersFragment;
import com.example.datn.StaffFragment.FurnitureFragment;
import com.example.datn.StaffFragment.ReviewsFragment;
import com.example.datn.StaffFragment.StatisticsFragment;

public class StaffActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private BottomNavigationView bottomNav;
    private ImageButton btnOpenMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        bottomNav = findViewById(R.id.bottom_navigation);
        btnOpenMenu = findViewById(R.id.btn_open_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new HomeFragment()).commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home_staff) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new HomeFragment()).commit();
                return true;
            } else if (id == R.id.nav_contact_staff) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new ContactFragment()).commit();
                return true;
            } else if (id == R.id.nav_profile_staff) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new ProfileFragment()).commit();
                return true;
            }
            return false;
        });

        bottomNav.setSelectedItemId(R.id.nav_home_staff);

        btnOpenMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // 🎯 Xử lý chọn NavigationDrawer
        navView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_orders) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new OrdersFragment()).commit();
            } else if (id == R.id.nav_users) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new UsersFragment()).commit();
            } else if (id == R.id.nav_furniture) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new FurnitureFragment()).commit();
            } else if (id == R.id.nav_reviews) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new ReviewsFragment()).commit();
            } else if (id == R.id.nav_statistics) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new StatisticsFragment()).commit();
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(StaffActivity.this, LoginActivity.class));
                finish();
            }
            // Đóng drawer sau khi chọn
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
