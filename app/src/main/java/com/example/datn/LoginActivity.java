package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView txtSignup, txtForgot;
    private RadioGroup roleGroup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        txtSignup = findViewById(R.id.txt_signup);
        txtForgot = findViewById(R.id.txtForgot);
        roleGroup = findViewById(R.id.roleGroup);

        // Xử lý đăng nhập
        loginButton.setOnClickListener(v -> loginUser());

        // Chuyển sang màn hình Đăng ký
        txtSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // TODO: xử lý Quên mật khẩu nếu muốn
        txtForgot.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // TODO: Chuyển sang MainActivity hoặc activity phù hợp
                        // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        // finish();
                    } else {
                        Toast.makeText(this, "Tài khoản không tồn tại hoặc sai mật khẩu", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
