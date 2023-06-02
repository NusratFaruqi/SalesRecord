package com.example.salesrecoder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductInfoFragment extends Fragment {

    private TextView productNameTextView;
    private TextView productYearTextView;
    private TextView productPriceTextView;
    private TextView productCategoryTextView;
    private TextView productVariantTextView;

    public ProductInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productNameTextView = view.findViewById(R.id.product_name);

        productYearTextView = view.findViewById(R.id.product_year);
        productPriceTextView = view.findViewById(R.id.product_price);
        productCategoryTextView = view.findViewById(R.id.product_category);
        productVariantTextView = view.findViewById(R.id.product_variant);

        // Fetch the latest product from Firebase
        fetchLatestProduct();
    }

    private void fetchLatestProduct() {
        // Replace "Sales" with the correct Firebase table name
        FirebaseDatabase.getInstance().getReference().child("Sales").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot lastProductSnapshot = null;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        lastProductSnapshot = childSnapshot;
                    }

                    if (lastProductSnapshot != null) {
                        String productName = String.valueOf(lastProductSnapshot.child("item").getValue());
                        String productYear = String.valueOf(lastProductSnapshot.child("year").getValue());
                        String productPrice = String.valueOf(lastProductSnapshot.child("price").getValue());
                        String productCategory = String.valueOf(lastProductSnapshot.child("catagory").getValue());
                        String productVariant = String.valueOf(lastProductSnapshot.child("varient").getValue());

                        // Update the TextViews with the product information
                        productNameTextView.setText("Name: " + productName.toUpperCase());
                        productYearTextView.setText("Year: " + productYear);
                        productPriceTextView.setText("Price: " + productPrice+" BDT");
                        productCategoryTextView.setText("Category: " + productCategory);
                        productVariantTextView.setText("Variant: " + productVariant);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });
    }

}
