package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn.UserFragment.ProfileFragment;

public class OrderCanceledActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabHistory, tabCompleted, tabCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_canceled);

        btnBack = findViewById(R.id.btnBack);
        tabHistory = findViewById(R.id.tabHistory);
        tabCompleted = findViewById(R.id.tabCompleted);
        tabCanceled = findViewById(R.id.tabCanceled);

        btnBack.setOnClickListener(v -> {

            finish();
        });


        tabHistory.setOnClickListener(v -> {
            startActivity(new Intent(OrderCanceledActivity.this, OrderHistoryActivity.class));
            finish();
        });

        tabCompleted.setOnClickListener(v -> {
            startActivity(new Intent(OrderCanceledActivity.this, OrderCompletedActivity.class));
            finish();
        });
    }
}
