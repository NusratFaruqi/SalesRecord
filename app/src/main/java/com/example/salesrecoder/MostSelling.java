package com.example.salesrecoder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MostSelling extends AppCompatActivity {
    private Button previous_button;
    private Button toggle_button;
    private FragmentManager fragmentManager;
    private ProductInfoFragment productInfoFragment;
    private boolean isInfoVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_selling);

        previous_button = findViewById(R.id.previous_button2);
        toggle_button = findViewById(R.id.toggle_button);
        fragmentManager = getSupportFragmentManager();
        productInfoFragment = new ProductInfoFragment();
        isInfoVisible = false;

        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previousintent = new Intent(MostSelling.this, SalesInfoActivity.class);
                startActivity(previousintent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        });

        toggle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleProductInfo();
            }
        });
    }

    private void toggleProductInfo() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (isInfoVisible) {
            // Hide the product info fragment
            transaction.remove(productInfoFragment);
            isInfoVisible = false;
        } else {
            // Show the product info fragment
            transaction.add(R.id.fragment_container, productInfoFragment);
            isInfoVisible = true;
        }

        transaction.commit();
    }
}
