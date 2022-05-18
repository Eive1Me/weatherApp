package com.example.weather;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME};
        return  database.query(DatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<City> getCities(){
        ArrayList<City> cities = new ArrayList<>();
        Cursor cursor = getAllEntries();
        while (cursor.moveToNext()){
            //SupressLint для игнорирования ошибок
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            cities.add(new City(id, name));
        }
        cursor.close();
        return  cities;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    public City getCity(long id){
        City city = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            city = new City(id, name);
        }
        cursor.close();
        return  city;
    }

    public long insert(City city){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, city.getName());
        return  database.insert(DatabaseHelper.TABLE, null, cv);
    }

    public long delete(long userId){
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(City city){
        String whereClause = DatabaseHelper.COLUMN_ID + "=" + city.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, city.getName());
        return database.update(DatabaseHelper.TABLE, cv, whereClause, null);
    }
}