package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, phoneEditText, emailEditText;
    private TextInputEditText passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView txtLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        fullNameEditText = findViewById(R.id.fullName);
        phoneEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txt_login);

        registerButton.setOnClickListener(v -> registerUser());

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";
        String confirmPassword = confirmPasswordEditText.getText() != null ? confirmPasswordEditText.getText().toString().trim() : "";

        String role = "user";

        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Email phải có đuôi @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^\\d{10}$")) {
            Toast.makeText(this, "Số điện thoại phải gồm đúng 10 chữ số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ mạnh của mật khẩu
        if (password.length() < 6 ||
                !password.matches(".*[A-Z].*") ||        // ít nhất 1 chữ in hoa
                !password.matches(".*[0-9].*") ||        // ít nhất 1 chữ số
                !password.matches(".*[!@#$%^&*+=?.].*")) // ít nhất 1 ký tự đặc biệt
        {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự, bao gồm 1 chữ hoa, 1 số và 1 ký tự đặc biệt", Toast.LENGTH_LONG).show();
            return;
        }

        usersRef.get().addOnSuccessListener(snapshot -> {
            boolean emailExists = false;
            boolean phoneExists = false;

            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                String existingEmail = userSnapshot.child("email").getValue(String.class);
                String existingPhone = userSnapshot.child("phone").getValue(String.class);

                if (existingEmail != null && existingEmail.equals(email)) {
                    emailExists = true;
                }

                if (existingPhone != null && existingPhone.equals(phone)) {
                    phoneExists = true;
                }
            }

            if (emailExists) {
                Toast.makeText(this, "Email đã được sử dụng.", Toast.LENGTH_SHORT).show();
            } else if (phoneExists) {
                Toast.makeText(this, "Số điện thoại đã được sử dụng.", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("fullName", fullName);
                                    userMap.put("phone", phone);
                                    userMap.put("email", email);
                                    userMap.put("role", role);

                                    usersRef.child(user.getUid())
                                            .setValue(userMap)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Lỗi khi lưu người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Toast.makeText(this, "Lỗi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("RegisterActivity", "Đăng ký thất bại", task.getException());
                            }
                        });
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}
