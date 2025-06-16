package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button btnContinue;
    private CheckBox checkboxAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnContinue = findViewById(R.id.btnContinue);
        checkboxAgreement = findViewById(R.id.checkboxAgreement);

        // Ban đầu: disable nút & chuyển sang màu xám
        btnContinue.setEnabled(false);
        btnContinue.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));

        // Bắt sự kiện check/uncheck checkbox
        checkboxAgreement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnContinue.setEnabled(isChecked);
            if (isChecked) {
                // Nếu được tick -> màu xanh
                btnContinue.setBackgroundTintList(getResources().getColorStateList(R.color.teal_700));
            } else {
                // Nếu bỏ tick -> màu xám
                btnContinue.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
            }
        });

        // Xử lý khi ấn nút "Bắt đầu"
        btnContinue.setOnClickListener(v -> {
            // Chuyển sang màn hình LoginActivity
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        });
    }
}
