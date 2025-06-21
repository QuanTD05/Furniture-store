package com.example.datn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.datn.R;
import com.example.datn.model.FurnitureModel;

import java.util.List;

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.FurnitureViewHolder> {

    private Context context;
    private List<FurnitureModel> furnitureList;

    // Callback xử lý khi click vào nút Sửa/Xoá/Xem
    public interface OnFurnitureClickListener {
        void onEdit(FurnitureModel furniture);
        void onDelete(FurnitureModel furniture);
        void onView(FurnitureModel furniture);
    }

    private OnFurnitureClickListener listener;

    public FurnitureAdapter(Context context, List<FurnitureModel> furnitureList, OnFurnitureClickListener listener) {
        this.context = context;
        this.furnitureList = furnitureList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FurnitureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_furniture, parent, false);
        return new FurnitureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FurnitureViewHolder holder, int position) {
        FurnitureModel item = furnitureList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvType.setText(item.getType());

        // Load ảnh bằng Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgFurniture);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
        holder.btnView.setOnClickListener(v -> listener.onView(item));
    }

    @Override
    public int getItemCount() {
        return furnitureList.size();
    }

    public static class FurnitureViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFurniture, btnEdit, btnDelete, btnView;
        TextView tvName, tvType;

        public FurnitureViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFurniture = itemView.findViewById(R.id.imgFurniture);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
