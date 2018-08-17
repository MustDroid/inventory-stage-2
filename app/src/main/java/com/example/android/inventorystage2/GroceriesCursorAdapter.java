package com.example.android.inventorystage2;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventorystage2.data.Food;
import com.example.android.inventorystage2.data.FoodDbHelper;

/**
 * Created by Emoke Hajdu on 7/30/2018.
 */

public class GroceriesCursorAdapter extends CursorAdapter {
    private final Context context;
    private FoodDbHelper foodDbHelper;

    public GroceriesCursorAdapter(Context context, Cursor c, FoodDbHelper dbHelper) {
        super(context, c, 0);
        this.context = context;
        this.foodDbHelper = dbHelper;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.food_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ImageView imgProduct = (ImageView)view.findViewById(R.id.imgProduct);
        ImageView imgEdit = (ImageView)view.findViewById(R.id.imgEdit);
        TextView txtProductName = (TextView) view.findViewById(R.id.txtProductName);
        final TextView txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        TextView txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        Button btnSell = (Button)view.findViewById(R.id.btnSell);

        final int ID = cursor.getInt(cursor.getColumnIndex(Food.Contract._ID));
        final String name = cursor.getString(cursor.getColumnIndex(Food.Contract.COLUMN_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(Food.Contract.COLUMN_QUANTITY));
        final double price = cursor.getDouble(cursor.getColumnIndex(Food.Contract.COLUMN_PRICE));
        final String picture = cursor.getString(cursor.getColumnIndex(Food.Contract.COLUMN_PICTURE));
        final String supplierName = cursor.getString(cursor.getColumnIndex(Food.Contract.COLUMN_SUPPLIER_NAME));
        final String supplierPhoneNumber = cursor.getString(cursor.getColumnIndex(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER));

        Resources res = context.getResources();
        int resID = res.getIdentifier(picture, "drawable", context.getPackageName());
        if(resID == 0)
        {
            imgProduct.setImageResource(R.drawable.noimageavailable);
        }
        else
        {
            imgProduct.setImageResource(resID);
        }

        txtProductName.setText(name);
        txtQuantity.setText(String.valueOf(quantity) + " Stk");
        txtPrice.setText(String.valueOf(price) + " CHF");

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editQuantity(ID, quantity-1);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)context;
                activity.onFoodDetailClicked(ID, name, quantity, price, picture, supplierName, supplierPhoneNumber);
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)context;
                activity.onFoodEditClicked(ID, name, quantity, price, picture, supplierName, supplierPhoneNumber);
            }
        });
    }

    private void editQuantity(int foodID, int newQuantity) {
        if(newQuantity < 0)
            return;

        try {
            SQLiteDatabase db = foodDbHelper.getWritableDatabase();
            String sqlQuery = String.format("UPDATE %s SET %s = %d WHERE %s=%d",
                    Food.Contract.TABLE_NAME,
                    Food.Contract.COLUMN_QUANTITY,
                    newQuantity,
                    Food.Contract._ID,
                    foodID);

            db.execSQL(sqlQuery);

            MainActivity activity = (MainActivity)context;
            activity.readDataFromDatabase();
        }
        catch(Exception ex) {

        }
    }
}
