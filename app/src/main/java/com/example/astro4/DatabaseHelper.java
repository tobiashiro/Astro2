package com.example.astro4;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "latlong_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Refreshing";
    private static final String COL3 = "Lat";
    private static final String COL4 = "Long";


    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 3);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " INTEGER, "+ COL3 + " DOUBLE, " + COL4 + " DOUBLE);";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(int refreshing, double lat, double lng){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 2);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, refreshing);
        contentValues.put(COL3, lat);
        contentValues.put(COL4, lng);

        Log.d(TAG, "addData: Adding " + refreshing+" lat: " +lat+ " long: " + lng  + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in

     * @return
     */
    public Cursor getItemID(String city){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + city + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

}

