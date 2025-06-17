//package com.example.datn;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.*;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.*;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText, passwordEditText;
//    private Button loginButton;
//    private TextView txtSignup, txtForgot;
//    private RadioGroup roleGroup;
//
//    private FirebaseAuth mAuth;
//    private DatabaseReference usersRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        // Firebase init
//        mAuth = FirebaseAuth.getInstance();
//        usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // UI mapping
//        emailEditText = findViewById(R.id.email);
//        passwordEditText = findViewById(R.id.password);
//        loginButton = findViewById(R.id.login);
//        txtSignup = findViewById(R.id.txt_signup);
//        txtForgot = findViewById(R.id.txtForgot);
//        roleGroup = findViewById(R.id.roleGroup);
//
//        loginButton.setOnClickListener(v -> loginUser());
//
//        txtSignup.setOnClickListener(v ->
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
//
//        txtForgot.setOnClickListener(v ->
//                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());
//    }
//
//    private void loginUser() {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            usersRef.child(user.getUid()).child("role").addListenerForSingleValueEvent(
//                                    new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if (snapshot.exists()) {
//                                                String storedRole = snapshot.getValue(String.class);
//                                                navigateToRoleScreen(storedRole);
//                                            } else {
//                                                Toast.makeText(LoginActivity.this, "Không tìm thấy vai trò", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                            Toast.makeText(LoginActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    } else {
//                        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
//
//    private void navigateToRoleScreen(String role) {
//        Intent intent;
//        switch (role.toLowerCase()) {
//            case "admin":
//                intent = new Intent(this, AdminActivity.class);
//                break;
//            case "staff":
//                intent = new Intent(this, StaffActivity.class);
//                break;
//            case "user":
//                intent = new Intent(this, UserActivity.class);
//                break;
//            default:
//                Toast.makeText(this, "Quyền không hợp lệ", Toast.LENGTH_SHORT).show();
//                return;
//        }
//        Toast.makeText(this, "Đăng nhập thành công với vai trò: " + role, Toast.LENGTH_SHORT).show();
//        startActivity(intent);
//        finish();
//    }
//}

// fake dữ liệu
package com.example.datn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView txtSignup, txtForgot;
    private RadioGroup roleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Liên kết View
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        txtSignup = findViewById(R.id.txt_signup);
        txtForgot = findViewById(R.id.txtForgot);
        roleGroup = findViewById(R.id.roleGroup); // 🟢 ĐÃ THÊM DÒNG NÀY

        // Sự kiện đăng nhập
        loginButton.setOnClickListener(v -> loginUser());

        // Chuyển sang đăng ký
        txtSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // Thông báo khi chọn Quên mật khẩu
        txtForgot.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Vui lòng chọn vai trò", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = ((RadioButton) findViewById(selectedRoleId)).getText().toString().toLowerCase();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giả lập tài khoản (không cần Firebase)
        if (email.equals("admin@gmail.com") && password.equals("123456") && role.equals("admin")) {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        } else if (email.equals("staff@gmail.com") && password.equals("123456") && role.equals("staff")) {
            startActivity(new Intent(this, StaffActivity.class));
            finish();
        } else if (email.equals("user@gmail.com") && password.equals("123456") && role.equals("user")) {
            startActivity(new Intent(this, UserActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sai tài khoản, mật khẩu hoặc vai trò", Toast.LENGTH_SHORT).show();
        }
    }
}

