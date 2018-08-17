/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.inventorystage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventorystage2.data.Food;
import com.example.android.inventorystage2.data.FoodDbHelper;

import static android.R.id.edit;

public class AddEditActivity extends AppCompatActivity {
    private FoodDbHelper mDbHelper;

    private EditText editName;
    private EditText editQuantity;
    private EditText editPrice;
    private EditText editPicture;
    private EditText editSupplierName;
    private EditText editSupplierPhoneNumber;

    private int foodID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDbHelper = new FoodDbHelper(this);

        editName = (EditText) findViewById(R.id.editName);
        editQuantity = (EditText) findViewById(R.id.editQuantity);
        editPrice = (EditText) findViewById(R.id.editPrice);
        editPicture = (EditText) findViewById(R.id.editPicture);
        editSupplierName = (EditText) findViewById(R.id.editSupplierName);
        editSupplierPhoneNumber = (EditText) findViewById(R.id.editSupplierPhoneNumber);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            foodID = extras.getInt(Food.Contract._ID);
            editName.setText(extras.getString(Food.Contract.COLUMN_NAME));
            editQuantity.setText(String.valueOf(extras.getInt(Food.Contract.COLUMN_QUANTITY)));
            editPrice.setText(String.valueOf(extras.getDouble(Food.Contract.COLUMN_PRICE)));
            editPicture.setText(extras.getString(Food.Contract.COLUMN_PICTURE));
            editSupplierName.setText(extras.getString(Food.Contract.COLUMN_SUPPLIER_NAME));
            editSupplierPhoneNumber.setText(extras.getString(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER));
        }
    }

    private boolean checkInput()
    {
        if(editName.getText().toString().length() == 0)
        {
            Toast.makeText(this, R.string.msg_food_not_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        if(editQuantity.getText().toString().length() == 0)
        {
            Toast.makeText(this, R.string.msg_quantity_not_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        if(!Utility.isInteger(editQuantity.getText().toString()))
        {
            Toast.makeText(this, R.string.quantity_not_number, Toast.LENGTH_LONG).show();
            return false;
        }
        if(editPrice.getText().toString().length() == 0)
        {
            Toast.makeText(this, R.string.msg_price_not_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        if(!Utility.isDouble(editPrice.getText().toString()))
        {
            Toast.makeText(this, R.string.price_not_number, Toast.LENGTH_LONG).show();
            return false;
        }
        if(editSupplierName.getText().toString().length() == 0)
        {
            Toast.makeText(this, R.string.msg_empty_supplier_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if(editSupplierPhoneNumber.getText().toString().length() == 0)
        {
            Toast.makeText(this, R.string.msg_empty_phone_number, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void insertFood() {
        if(!checkInput())
            return;

        Food food = new Food(editName.getText().toString(),
                Integer.parseInt(editQuantity.getText().toString()),
                Double.parseDouble(editPrice.getText().toString()),
                editPicture.getText().toString(),
                editSupplierName.getText().toString(),
                editSupplierPhoneNumber.getText().toString());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Food.Contract.COLUMN_NAME, food.getName());
        values.put(Food.Contract.COLUMN_QUANTITY, food.getQuantity());
        values.put(Food.Contract.COLUMN_PRICE, food.getPrice());
        values.put(Food.Contract.COLUMN_PICTURE, food.getPicture());
        values.put(Food.Contract.COLUMN_SUPPLIER_NAME, food.getSupplierName());
        values.put(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER, food.getSupplierPhoneNumber());

        long newRowId = db.insert(Food.Contract.TABLE_NAME, null, values);

        if (newRowId == -1) {

            Toast.makeText(this, R.string.error_saving_food, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.food_saved) + newRowId, Toast.LENGTH_SHORT).show();
            returnToMainActivity();
        }
    }

    private void editFood() {
        if(!checkInput())
            return;

        Food food = new Food(editName.getText().toString(),
                Integer.parseInt(editQuantity.getText().toString()),
                Double.parseDouble(editPrice.getText().toString()),
                editPicture.getText().toString(),
                editSupplierName.getText().toString(),
                editSupplierPhoneNumber.getText().toString());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Food.Contract.COLUMN_NAME, food.getName());
        values.put(Food.Contract.COLUMN_QUANTITY, food.getQuantity());
        values.put(Food.Contract.COLUMN_PRICE, food.getPrice());
        values.put(Food.Contract.COLUMN_PICTURE, food.getPicture());
        values.put(Food.Contract.COLUMN_SUPPLIER_NAME, food.getSupplierName());
        values.put(Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER, food.getSupplierPhoneNumber());

        int rowsAffected = db.update(Food.Contract.TABLE_NAME,
                values,
                Food.Contract._ID + "=?",
                new String[]{Integer.toString(foodID)});

        if (rowsAffected > 0) {
            Toast.makeText(this, R.string.food_edited, Toast.LENGTH_SHORT).show();
            returnToMainActivity();
        } else {
            Toast.makeText(this, R.string.food_edit_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                if(foodID == -1) {
                    insertFood();
                }
                else {
                    editFood();
                }

                return true;

            case R.id.action_cancel:
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                return true;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}