package com.example.datn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn.R;
import com.example.datn.model.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<OrderModel> orders;
    private final OnOrderActionListener listener;

    // Interface để bắt sự kiện cập nhật trạng thái
    public interface OnOrderActionListener {
        void onUpdateStatus(OrderModel order);
    }

    public OrderAdapter(Context context, List<OrderModel> orders, OnOrderActionListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orders.get(position);

        holder.tvCustomer.setText(order.getCustomerName());
        holder.tvDate.setText(order.getOrderDate());
        holder.tvTotal.setText("Tổng: " + order.getTotalAmount() + " đ");
        holder.tvStatus.setText("Trạng thái: " + order.getStatus());

        holder.btnUpdate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdateStatus(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    // ViewHolder chứa các View trong mỗi item
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvDate, tvTotal, tvStatus;
        Button btnUpdate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tv_customer);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnUpdate = itemView.findViewById(R.id.btn_update_status);
        }
    }
}
