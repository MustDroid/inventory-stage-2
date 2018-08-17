package com.example.android.inventorystage2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventorystage2.data.Food;
import com.example.android.inventorystage2.data.FoodDbHelper;

public class FoodDetailActivity extends AppCompatActivity {

    private TextView txtFoodName;
    private ImageView imgFood;
    private TextView txtPrice;
    private TextView txtQuantity;
    private TextView txtSupplierName;
    private TextView txtSupplierPhoneNumber;

    private FoodDbHelper mFoodHelper;

    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mFoodHelper = new FoodDbHelper(this);

        txtFoodName = (TextView)findViewById(R.id.txtFoodName);
        imgFood = (ImageView)findViewById(R.id.imgFood);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        txtQuantity = (TextView)findViewById(R.id.txtQuantity);
        txtSupplierName = (TextView)findViewById(R.id.txtSupplierName);
        txtSupplierPhoneNumber = (TextView)findViewById(R.id.txtSupplierPhoneNumber);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            food = new Food(extras.getString(Food.Contract.COLUMN_NAME),
                    extras.getInt(Food.Contract.COLUMN_QUANTITY),
                    extras.getDouble(Food.Contract.COLUMN_PRICE),
                    extras.getString(Food.Contract.COLUMN_PICTURE),
                    extras.getString(Food.Contract.COLUMN_SUPPLIER_NAME),
                    extras.getString(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER));

            food.setID(extras.getInt(Food.Contract._ID));

            txtFoodName.setText(food.getName());

            Resources res = getResources();
            int resID = res.getIdentifier(food.getPicture(), "drawable", getPackageName());
            if(resID == 0)
            {
                imgFood.setImageResource(R.drawable.noimageavailable);
            }
            else
            {
                imgFood.setImageResource(resID);
            }

            txtPrice.setText("PRICE: " + extras.getDouble(Food.Contract.COLUMN_PRICE) + " CHF");
            txtQuantity.setText(String.valueOf(food.getQuantity()) + " Stk");
            txtSupplierName.setText("SUPPLIER: " + food.getSupplierName());
            txtSupplierPhoneNumber.setText("PHONE NUMBER: " + food.getSupplierPhoneNumber());
        }
    }

    public void btnDelete_OnClick(View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteFood();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete food '" + food.getName() + "'?")
                .setTitle("Confirm deletion")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                .show();
    }

    public void btnPlus_OnClick(View view) {
        editQuantity(food.getQuantity()+1);
    }

    public void btnMinus_OnClick(View view) {
        editQuantity(food.getQuantity()-1);
    }
    public void btnCallSupplier_OnClick(View View){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + food.getSupplierPhoneNumber()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void deleteFood() {
        try {
            SQLiteDatabase db = mFoodHelper.getWritableDatabase();
            String sqlQuery = String.format("DELETE FROM %s WHERE %s = %d",
                    Food.Contract.TABLE_NAME,
                    Food.Contract._ID,
                    food.getID());

            db.execSQL(sqlQuery);
        }
        catch(Exception ex) {
            return;
        }

        Toast.makeText(this, "Food with ID " + food.getID() + " has been successfully deleted!", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private void editQuantity(int newQuantity) {
        if(newQuantity < 0)
            return;

        try {
            SQLiteDatabase db = mFoodHelper.getWritableDatabase();
            String sqlQuery = String.format("UPDATE %s SET %s = %d WHERE %s=%d",
                    Food.Contract.TABLE_NAME,
                    Food.Contract.COLUMN_QUANTITY,
                    newQuantity,
                    Food.Contract._ID,
                    food.getID());

            db.execSQL(sqlQuery);
        }
        catch(Exception ex) {
            return;
        }

        food.setQuantity(newQuantity);
        txtQuantity.setText(food.getQuantity() + " Stk");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
