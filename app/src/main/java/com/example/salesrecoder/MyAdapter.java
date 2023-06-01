package com.example.salesrecoder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<SalesData> salesDetail = new ArrayList<>();
    private DatabaseReference salesYearRef;

    public static class SalesData {
        String itemName;
        int yearDisplay;
        double yearlyProfit;

        public SalesData(String itemName, int yearDisplay, double yearlyProfit) {
            this.itemName = itemName;
            this.yearDisplay = yearDisplay;
            this.yearlyProfit = yearlyProfit;
        }
    }

    public MyAdapter(Context context) {
        this.context = context;
        salesYearRef = FirebaseDatabase.getInstance().getReference("SalesYear");
        salesYearRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                    String itemYearKey = yearSnapshot.getKey();
                    if (itemYearKey != null) {
                        String[] keyParts = itemYearKey.split("_");
                        if (keyParts.length == 2) {
                            String itemName = keyParts[0];
                            int year = Integer.parseInt(keyParts[1]);
                            double yearlyProfit = yearSnapshot.child("totalSalesPriceYearWise").getValue(Double.class);
                            SalesData salesData = new SalesData(itemName, year, yearlyProfit);
                            salesDetail.add(salesData);
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.recyclerview_display_support, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesData salesData = salesDetail.get(position);

        holder.item.setText(salesData.itemName);
        holder.year.setText(String.valueOf(salesData.yearDisplay));
        holder.total.setText(String.valueOf(salesData.yearlyProfit));
    }

    @Override
    public int getItemCount() {
        return salesDetail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item, year, total;
        RelativeLayout recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_name);
            year = itemView.findViewById(R.id.year_name);
            total = itemView.findViewById(R.id.price_name);
            recyclerView = itemView.findViewById(R.id.mylayout);
        }
    }
}
