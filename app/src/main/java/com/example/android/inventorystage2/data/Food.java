package com.example.android.inventorystage2.data;

import android.provider.BaseColumns;

/**
 * Created by Emoke Hajdu on 7/26/2018.
 */

public class Food {
    public final class Contract {
        private Contract() {}

        public final static String TABLE_NAME = "food";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME ="name";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_PICTURE = "picture";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }

    public Food(String name, int quantity, double price, String picture, String supplierName, String supplierPhoneNumber) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.picture = picture;
        this.supplierName = supplierName;
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getPicture() { return picture; }

    public String getSupplierName() { return supplierName; }

    public String getSupplierPhoneNumber() { return supplierPhoneNumber; }

    private int id;
    private String name;
    private int quantity;
    private double price;
    private String picture;
    private String supplierName;
    private String supplierPhoneNumber;
}
