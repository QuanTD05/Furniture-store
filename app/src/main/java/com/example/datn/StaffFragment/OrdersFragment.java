package com.example.datn.StaffFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = view.findViewById(R.id.recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();

        adapter = new OrderAdapter(getContext(), orderList, this::showUpdateStatusDialog);
        recyclerView.setAdapter(adapter);

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        loadOrders();

        return view;
    }

    private void loadOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    OrderModel order = data.getValue(OrderModel.class);
                    if (order != null) {
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
