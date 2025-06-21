package com.example.datn.StaffFragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn.Adapter.OrderAdapter;
import com.example.datn.R;
import com.example.datn.model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderModel> orderList;
    private DatabaseReference ordersRef;
    private TextView btnPending, btnDone, btnCancelled;
    private View underlinePending, underlineDone, underlineCancelled;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        btnPending = view.findViewById(R.id.btn_pending);
        btnDone = view.findViewById(R.id.btn_done);
        btnCancelled = view.findViewById(R.id.btn_cancelled);

        underlinePending = view.findViewById(R.id.underline_pending);
        underlineDone = view.findViewById(R.id.underline_done);
        underlineCancelled = view.findViewById(R.id.underline_cancelled);

        recyclerView = view.findViewById(R.id.recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();

        adapter = new OrderAdapter(getContext(), orderList, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onConfirm(OrderModel order) {
                showUpdateStatusDialog(order);
            }

            @Override
            public void onCancel(OrderModel order) {
                showUpdateStatusDialog(order);
            }

            @Override
            public void onDetail(OrderModel order) {
                showUpdateStatusDialog(order);
            }
        });

        recyclerView.setAdapter(adapter);
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        btnPending.setOnClickListener(v -> {
            selectTab("pending");
            filterOrders("Đang xử lý");
        });

        btnDone.setOnClickListener(v -> {
            selectTab("done");
            filterOrders("Hoàn thành");
        });

        btnCancelled.setOnClickListener(v -> {
            selectTab("cancelled");
            filterOrders("Đã huỷ");
        });

        selectTab("pending");
        filterOrders("Đang xử lý");

        return view;
    }

    private void selectTab(String tab) {
        if (getContext() == null) return;

        btnPending.setTextColor(Color.BLACK);
        underlinePending.setBackgroundColor(Color.TRANSPARENT);

        btnDone.setTextColor(Color.BLACK);
        underlineDone.setBackgroundColor(Color.TRANSPARENT);

        btnCancelled.setTextColor(Color.BLACK);
        underlineCancelled.setBackgroundColor(Color.TRANSPARENT);

        if (tab.equals("pending")) {
            btnPending.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
            underlinePending.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink));
        } else if (tab.equals("done")) {
            btnDone.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
            underlineDone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink));
        } else if (tab.equals("cancelled")) {
            btnCancelled.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
            underlineCancelled.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink));
        }
    }

    private void filterOrders(String status) {
        if (ordersRef == null) return;

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderModel order = data.getValue(OrderModel.class);
                    if (order != null && order.getStatus() != null && order.getStatus().equalsIgnoreCase(status)) {
                        order.setId(data.getKey());
                        orderList.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrdersFragment", "Lỗi tải đơn hàng: " + error.getMessage());
            }
        });
    }

    private void showUpdateStatusDialog(OrderModel order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_status, null);
        builder.setView(dialogView);

        Spinner spinnerStatus = dialogView.findViewById(R.id.spinner_status);
        String[] statuses = {"Đang xử lý", "Đang giao", "Hoàn thành", "Đã huỷ"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statuses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapter);

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String newStatus = spinnerStatus.getSelectedItem().toString();
            order.setStatus(newStatus);
            ordersRef.child(order.getId()).setValue(order)
                    .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
