package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderCompletedActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabHistory, tabCompleted, tabCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);

        btnBack = findViewById(R.id.btnBack);
        tabHistory = findViewById(R.id.tabHistory);
        tabCompleted = findViewById(R.id.tabCompleted);
        tabCanceled = findViewById(R.id.tabCanceled);

        btnBack.setOnClickListener(v -> {

            finish();
        });



        tabHistory.setOnClickListener(v -> {
            startActivity(new Intent(OrderCompletedActivity.this, OrderHistoryActivity.class));
            finish();
        });

        tabCanceled.setOnClickListener(v -> {
            startActivity(new Intent(OrderCompletedActivity.this, OrderCanceledActivity.class));
            finish();
        });
    }
}
