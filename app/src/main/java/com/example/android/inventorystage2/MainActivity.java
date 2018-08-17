package com.example.android.inventorystage2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventorystage2.data.Food;
import com.example.android.inventorystage2.data.FoodDbHelper;

public class MainActivity extends AppCompatActivity {

    private ListView lstGroceries;
    private TextView txtNoProduct;
    private FoodDbHelper mFoodHelper;
    private GroceriesCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodHelper = new FoodDbHelper(this);

        lstGroceries = (ListView)findViewById(R.id.lstGroceries);
        txtNoProduct = (TextView)findViewById(R.id.txtNoProduct);

        readDataFromDatabase();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_data:
                Intent intent = new Intent(this, AddEditActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_delete_all_entries:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void readDataFromDatabase() {
        Cursor cursor = mFoodHelper.readFoods();
        if (cursor.getCount() == 0) {
            lstGroceries.setVisibility(View.GONE);
            txtNoProduct.setVisibility(View.VISIBLE);
        } else {
            lstGroceries.setVisibility(View.VISIBLE);
            txtNoProduct.setVisibility(View.GONE);
        }
        adapter = new GroceriesCursorAdapter(this, cursor, mFoodHelper);
        lstGroceries.setAdapter(adapter);
    }

    private void deleteAll() {
        SQLiteDatabase db = mFoodHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Food.Contract.TABLE_NAME);
        Toast.makeText(this, "All data have been deleted from database!", Toast.LENGTH_SHORT).show();
        readDataFromDatabase();
    }

    public void onFoodDetailClicked(int id, String name, int quantity, double price, String picture, String supplierName, String supplierPhoneNumber) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra(Food.Contract._ID, id);
        intent.putExtra(Food.Contract.COLUMN_NAME, name);
        intent.putExtra(Food.Contract.COLUMN_QUANTITY, quantity);
        intent.putExtra(Food.Contract.COLUMN_PRICE, price);
        intent.putExtra(Food.Contract.COLUMN_PICTURE, picture);
        intent.putExtra(Food.Contract.COLUMN_SUPPLIER_NAME, supplierName);
        intent.putExtra(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        startActivity(intent);
    }

    public void onFoodEditClicked(int id, String name, int quantity, double price, String picture, String supplierName, String supplierPhoneNumber) {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra(Food.Contract._ID, id);
        intent.putExtra(Food.Contract.COLUMN_NAME, name);
        intent.putExtra(Food.Contract.COLUMN_QUANTITY, quantity);
        intent.putExtra(Food.Contract.COLUMN_PRICE, price);
        intent.putExtra(Food.Contract.COLUMN_PICTURE, picture);
        intent.putExtra(Food.Contract.COLUMN_SUPPLIER_NAME, supplierName);
        intent.putExtra(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        startActivity(intent);
    }
}
