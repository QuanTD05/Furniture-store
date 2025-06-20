package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn.UserFragment.ProfileFragment;

public class OrderHistoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabCompleted, tabCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        btnBack = findViewById(R.id.btnBack);
        tabCompleted = findViewById(R.id.tabCompleted);
        tabCanceled = findViewById(R.id.tabCanceled);

        // Quay lại ProfileFragment trong UserActivity
        btnBack.setOnClickListener(v -> {

            finish();
        });




        tabCompleted.setOnClickListener(v -> {
            startActivity(new Intent(OrderHistoryActivity.this, OrderCompletedActivity.class));
            finish(); // nên finish() để không stack nhiều Activity
        });

        tabCanceled.setOnClickListener(v -> {
            startActivity(new Intent(OrderHistoryActivity.this, OrderCanceledActivity.class));
            finish(); // nên finish() để gọn ngăn xếp Activity
        });
    }
}
