package com.example.android.inventorystage2.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Emoke Hajdu on 7/26/2018.
 */

public class FoodDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = FoodDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "food_database.db";

    private static final int DATABASE_VERSION = 3;

    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_FOOD_TABLE =  "CREATE TABLE " + Food.Contract.TABLE_NAME + " ("
                + Food.Contract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Food.Contract.COLUMN_NAME + " TEXT NOT NULL,"
                + Food.Contract.COLUMN_PRICE + " REAL,"
                + Food.Contract.COLUMN_QUANTITY + " INTEGER NOT NULL,"
                + Food.Contract.COLUMN_PICTURE + " TEXT NOT NULL,"
                + Food.Contract.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL,"
                + Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Food.Contract.TABLE_NAME);
        onCreate(db);
    }

    public Cursor readFoods() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                Food.Contract._ID,
                Food.Contract.COLUMN_NAME,
                Food.Contract.COLUMN_PRICE,
                Food.Contract.COLUMN_QUANTITY,
                Food.Contract.COLUMN_PICTURE,
                Food.Contract.COLUMN_SUPPLIER_NAME,
                Food.Contract.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        Cursor cursor = db.query(
                Food.Contract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
}
