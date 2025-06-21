package com.example.datn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn.R;
import com.example.datn.model.OrderModel;
import com.example.datn.model.ProductInOrder;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PROCESSING = 0;
    private static final int TYPE_COMPLETED_OR_CANCELLED = 1;

    private Context context;
    private List<OrderModel> orders;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onConfirm(OrderModel order);
        void onCancel(OrderModel order);
        void onDetail(OrderModel order);
    }

    public OrderAdapter(Context context, List<OrderModel> orders, OnOrderActionListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        String status = orders.get(position).getStatus().toLowerCase();
        if (status.equals("đang xử lý")) {
            return TYPE_PROCESSING;
        } else {
            return TYPE_COMPLETED_OR_CANCELLED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROCESSING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
            return new ProcessingViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_1, parent, false);
            return new CompletedCancelledViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        OrderModel order = orders.get(position);

        if (vh instanceof ProcessingViewHolder) {
            ProcessingViewHolder holder = (ProcessingViewHolder) vh;

            holder.tvCustomer.setText(order.getCustomerName());
            holder.tvDate.setText(order.getOrderDate());
            holder.tvTotal.setText("Thành tiền: $" + String.format("%.2f", order.getTotalAmount()));

            // Danh sách sản phẩm
            StringBuilder productText = new StringBuilder();
            for (ProductInOrder product : order.getItems()) {
                productText.append(product.getProductName())
                        .append("   SL: ").append(product.getQuantity())
                        .append("   $").append(String.format("%.2f", product.getPrice()))
                        .append("\n");
            }
            holder.tvProducts.setText(productText.toString().trim());

            holder.tvStatus.setText("Trạng thái: " + order.getStatus());
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.pink));

            Glide.with(context)
                    .load(R.drawable.logo)
                    .into(holder.imgAvatar);

            holder.btnDetail.setOnClickListener(v -> listener.onDetail(order));
            holder.btnConfirm.setOnClickListener(v -> listener.onConfirm(order));
            holder.btnCancel.setOnClickListener(v -> listener.onCancel(order));

        } else if (vh instanceof CompletedCancelledViewHolder) {
            CompletedCancelledViewHolder holder = (CompletedCancelledViewHolder) vh;

            holder.tvCustomer.setText(order.getCustomerName());
            holder.tvDate.setText(order.getOrderDate());
            holder.tvTotal.setText("Thành tiền: $" + String.format("%.2f", order.getTotalAmount()));

            // Danh sách sản phẩm
            StringBuilder productText = new StringBuilder();
            for (ProductInOrder product : order.getItems()) {
                productText.append(product.getProductName())
                        .append("   SL: ").append(product.getQuantity())
                        .append("   $").append(String.format("%.2f", product.getPrice()))
                        .append("\n");
            }
            holder.tvProducts.setText(productText.toString().trim());

            holder.tvStatus.setText("Trạng thái: " + order.getStatus());

            if (order.getStatus().equalsIgnoreCase("đã hoàn thành")) {
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            } else {
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            }

            Glide.with(context)
                    .load(R.drawable.logo)
                    .into(holder.imgAvatar);

            holder.btnDetail.setOnClickListener(v -> listener.onDetail(order));
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // ViewHolder cho đơn "Đang xử lý"
    public static class ProcessingViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvDate, tvTotal, tvStatus, tvProducts;
        Button btnDetail, btnCancel, btnConfirm;
        ImageView imgAvatar;

        public ProcessingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tv_customer);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvProducts = itemView.findViewById(R.id.tv_products);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
        }
    }

    // ViewHolder cho đơn "Đã hoàn thành" và "Đã huỷ"
    public static class CompletedCancelledViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvDate, tvTotal, tvStatus, tvProducts;
        Button btnDetail;
        ImageView imgAvatar;

        public CompletedCancelledViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tv_customer);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvProducts = itemView.findViewById(R.id.tv_products);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
        }
    }
}
