package com.example.salesrecoder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBHelper {
    public static final String DATABASE_NAME = "SalesInfo";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Sales";
    public static final String ITEM_NAME = "Item_Name";
    public static final String YEAR = "Year";
    public static final String DATE = "Date";
    public static final String SALES_PRICE = "SalesPrice";

    public static final String TABLE_NAME1 = "SalesYear";
    public static final String TOTAL_SALE_PRICE_YEAR_WISE = "TotalSalesPriceYearWise";

    private DatabaseReference salesRef;
    private DatabaseReference salesYearRef;

    public DBHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        salesRef = database.getReference(TABLE_NAME);
        salesYearRef = database.getReference(TABLE_NAME1);
    }

    public void insertData(String item, int year, double price, String date,String catagory,String varient) {
        String key = salesRef.push().getKey();
        if (key != null) {
           System.out.println(catagory+varient);
            SalesInfo salesInfo = new SalesInfo(item, year, price, date,catagory,varient);
            salesRef.child(key).setValue(salesInfo);

            salesYearRef.child(item + "_" + year)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double totalSales = 0;
                            if (dataSnapshot.exists()) {
                                SalesYearInfo salesYearInfo = dataSnapshot.getValue(SalesYearInfo.class);
                                if (salesYearInfo != null) {
                                    totalSales = salesYearInfo.getTotalSalesPriceYearWise();
                                }
                            }
                            totalSales += price;
                            salesYearRef.child(item + "_" + year).setValue(new SalesYearInfo(totalSales));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
        }
    }

    public static class SalesInfo {
        private String item;
        private int year;
        private double price;
        private String date;

        public String catagory;

        public String varient;

        public SalesInfo() {
            // Default constructor required for Firebase
        }

        public SalesInfo(String item, int year, double price, String date,String cat,String var) {
            this.item = item;
            this.year = year;
            this.price = price;
            this.date = date;
            this.catagory=cat;
            this.varient=var;
        }

        public String getItem() {
            return item;
        }

        public int getYear() {
            return year;
        }

        public double getPrice() {
            return price;
        }

        public String getDate() {
            return date;
        }
    }

    public static class SalesYearInfo {
        private double totalSalesPriceYearWise;

        public SalesYearInfo() {
            // Default constructor required for Firebase
        }

        public SalesYearInfo(double totalSalesPriceYearWise) {
            this.totalSalesPriceYearWise = totalSalesPriceYearWise;
        }

        public double getTotalSalesPriceYearWise() {
            return totalSalesPriceYearWise;
        }
    }
}
