package com.example.datn.StaffFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn.Adapter.FurnitureAdapter;
import com.example.datn.R;
import com.example.datn.model.FurnitureModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;

public class FurnitureFragment extends Fragment {

    private RecyclerView recyclerFurniture;
    private FurnitureAdapter adapter;
    private List<FurnitureModel> furnitureList;
    private List<FurnitureModel> allFurnitureList = new ArrayList<>();
    private DatabaseReference furnitureRef;
    private EditText editSearch;
    private FloatingActionButton fabAdd;
    private Spinner spinnerFilter;
    private Uri selectedImageUri = null;
    private final int REQUEST_IMAGE_PICK = 1001;
    private ImageView currentImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_furniture, container, false);

        recyclerFurniture = view.findViewById(R.id.recycler_furniture);
        editSearch = view.findViewById(R.id.edit_search);
        fabAdd = view.findViewById(R.id.fab_add);
        spinnerFilter = view.findViewById(R.id.spinner_filter);

        recyclerFurniture.setLayoutManager(new GridLayoutManager(getContext(), 2));
        furnitureList = new ArrayList<>();

        adapter = new FurnitureAdapter(getContext(), furnitureList, new FurnitureAdapter.OnFurnitureClickListener() {
            @Override
            public void onEdit(FurnitureModel furniture) {
                showAddOrEditDialog(furniture);
            }

            @Override
            public void onDelete(FurnitureModel furniture) {
                deleteFurniture(furniture);
            }

            @Override
            public void onView(FurnitureModel furniture) {
                showFurnitureDetailsDialog(furniture);
            }
        });

        recyclerFurniture.setAdapter(adapter);

        furnitureRef = FirebaseDatabase.getInstance().getReference("furniture");

        String[] types = {"Tất cả", "Bàn", "Ghế", "Tủ", "Giường"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadFurnitureData();

        fabAdd.setOnClickListener(v -> {
            selectedImageUri = null;
            showAddOrEditDialog(null);
        });

        return view;
    }

    private void loadFurnitureData() {
        furnitureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                furnitureList.clear();
                allFurnitureList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FurnitureModel item = data.getValue(FurnitureModel.class);
                    if (item != null) {
                        item.setId(data.getKey());
                        furnitureList.add(item);
                        allFurnitureList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FurnitureFragment", "Lỗi tải dữ liệu: " + error.getMessage());
            }
        });
    }

    private void filterByType(String type) {
        furnitureList.clear();
        if (type.equals("Tất cả")) {
            furnitureList.addAll(allFurnitureList);
        } else {
            for (FurnitureModel item : allFurnitureList) {
                if (item.getType().equalsIgnoreCase(type)) {
                    furnitureList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterByName(String keyword) {
        String selectedType = spinnerFilter.getSelectedItem().toString();
        List<FurnitureModel> filtered = new ArrayList<>();

        for (FurnitureModel item : allFurnitureList) {
            boolean matchType = selectedType.equals("Tất cả") || item.getType().equalsIgnoreCase(selectedType);
            boolean matchName = item.getName().toLowerCase().contains(keyword.toLowerCase());

            if (matchType && matchName) {
                filtered.add(item);
            }
        }

        furnitureList.clear();
        furnitureList.addAll(filtered);
        adapter.notifyDataSetChanged();
    }

    private void deleteFurniture(FurnitureModel furniture) {
        if (furniture.getId() != null) {
            furnitureRef.child(furniture.getId()).removeValue()
                    .addOnSuccessListener(unused -> Log.d("Firebase", "Đã xoá sản phẩm"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Xoá thất bại: " + e.getMessage()));
        }
    }

    private void showAddOrEditDialog(@Nullable FurnitureModel furnitureToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_furniture, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        ImageView imgSelected = dialogView.findViewById(R.id.img_selected);
        currentImageView = imgSelected;

        imgSelected.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        Spinner spinnerType = dialogView.findViewById(R.id.spinner_type);
        EditText edtName = dialogView.findViewById(R.id.edt_name);
        EditText edtDescription = dialogView.findViewById(R.id.edt_description);
        EditText edtPrice = dialogView.findViewById(R.id.edt_price);
        EditText edtRating = dialogView.findViewById(R.id.edt_rating);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        if (furnitureToEdit != null) {
            tvTitle.setText("Cập nhật nội thất");
            edtName.setText(furnitureToEdit.getName());
            edtDescription.setText(furnitureToEdit.getDescription());
            edtPrice.setText(String.valueOf(furnitureToEdit.getPrice()));
            edtRating.setText(String.valueOf(furnitureToEdit.getRating()));
            Glide.with(getContext()).load(furnitureToEdit.getImageUrl()).into(imgSelected);
        }

        String[] types = {"Bàn", "Ghế", "Tủ", "Giường"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerAdapter);

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String ratingStr = edtRating.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || ratingStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            float rating = (float) Double.parseDouble(ratingStr);

            FurnitureModel furniture = furnitureToEdit != null ? furnitureToEdit : new FurnitureModel();
            furniture.setName(name);
            furniture.setDescription(description);
            furniture.setType(type);
            furniture.setPrice(price);
            furniture.setRating(rating);

            if (selectedImageUri != null) {
                String fileName = UUID.randomUUID().toString();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("furniture_images").child(fileName);
                storageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            furniture.setImageUrl(uri.toString());
                            saveFurnitureToFirebase(furniture);
                            dialog.dismiss();
                        }))
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Tải ảnh thất bại", Toast.LENGTH_SHORT).show());
            } else {
                saveFurnitureToFirebase(furniture);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveFurnitureToFirebase(FurnitureModel furniture) {
        if (furniture.getId() == null) {
            String id = furnitureRef.push().getKey();
            furniture.setId(id);
        }
        furnitureRef.child(furniture.getId()).setValue(furniture);
    }

    private void showFurnitureDetailsDialog(FurnitureModel furniture) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_furniture_detail, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView tvName = view.findViewById(R.id.tv_detail_name);
        TextView tvDescription = view.findViewById(R.id.tv_detail_description);
        TextView tvType = view.findViewById(R.id.tv_detail_type);
        TextView tvPrice = view.findViewById(R.id.tv_detail_price);
        TextView tvRating = view.findViewById(R.id.tv_detail_rating);
        ImageView imgView = view.findViewById(R.id.img_detail);

        tvName.setText(furniture.getName());
        tvDescription.setText(furniture.getDescription());
        tvType.setText("Loại: " + furniture.getType());
        tvPrice.setText("Giá: " + furniture.getPrice() + " đ");
        tvRating.setText("Đánh giá: " + furniture.getRating() + " sao");

        if (furniture.getImageUrl() != null && !furniture.getImageUrl().isEmpty()) {
            Glide.with(requireContext())
                    .load(furniture.getImageUrl())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(imgView);
        }

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            if (currentImageView != null) {
                currentImageView.setImageURI(selectedImageUri);
            }
        }
    }
}
