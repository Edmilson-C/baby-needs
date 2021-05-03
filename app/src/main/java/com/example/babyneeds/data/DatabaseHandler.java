package com.example.babyneeds.data;

import  android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.babyneeds.model.BabyNeeds;
import com.example.babyneeds.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_ITEM + " TEXT,"
                + Util.KEY_QTY + " INTEGER," + Util.KEY_COLOR + " TEXT,"
                + Util.KEY_SIZE + " INTEGER," + Util.KEY_DATE + " LONG)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }

    public void addNeed(BabyNeeds needs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Util.KEY_ITEM, needs.getItem());
        values.put(Util.KEY_QTY, needs.getQuantity());
        values.put(Util.KEY_COLOR, needs.getColor());
        values.put(Util.KEY_SIZE, needs.getSize());
        values.put(Util.KEY_DATE, java.lang.System.currentTimeMillis());

        db.insert(Util.TABLE_NAME, null, values);
        db.close();
    }

    public BabyNeeds getOneNeed(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        BabyNeeds needs = new BabyNeeds();

        String selectOne = "SELECT * FROM " + Util.TABLE_NAME + " WHERE " + Util.KEY_ID + "=?";
        Cursor cursor = db.rawQuery(selectOne, new String[]{String.valueOf(id)});

        if(cursor != null)
            cursor.moveToFirst();

        needs.setId(cursor.getInt(0));
        needs.setItem(cursor.getString(1));
        needs.setQuantity(cursor.getInt(2));
        needs.setColor(cursor.getString(3));
        needs.setSize(cursor.getInt(4));
        DateFormat dateFormat = DateFormat.getDateInstance();
        String df = dateFormat.format(new Date(cursor.getLong(5)).getTime());
        needs.setDate(df);

        return needs;
    }

    public List<BabyNeeds> getAllNeeds() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<BabyNeeds> needsList = new ArrayList<>();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME + " ORDER BY cast(" + Util.KEY_DATE + " as REAL) DESC";
        Cursor cursor = db.rawQuery(selectAll,null);

        if(cursor.moveToFirst()) {
            do {
                BabyNeeds needs = new BabyNeeds();
                needs.setId(cursor.getInt(0));
                needs.setItem(cursor.getString(1));
                needs.setQuantity(cursor.getInt(2));
                needs.setColor(cursor.getString(3));
                needs.setSize(cursor.getInt(4));
                DateFormat dateFormat = DateFormat.getDateInstance();
                String df = dateFormat.format(new Date(cursor.getLong(5)).getTime());
                needs.setDate(df);

                needsList.add(needs);
            }while (cursor.moveToNext());
        }
        return  needsList;
    }

    public int updateNeeds(BabyNeeds needs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Util.KEY_ITEM, needs.getItem());
        values.put(Util.KEY_QTY, needs.getQuantity());
        values.put(Util.KEY_COLOR, needs.getColor());
        values.put(Util.KEY_SIZE, needs.getSize());
        values.put(Util.KEY_DATE, java.lang.System.currentTimeMillis());

        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?", new String[]{String.valueOf(needs.getId())});
    }

    public void deleteNeed(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME,null);
        return cursor.getCount();
    }
}
