package com.example.maciek.sleepwell.DataBase.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maciek.sleepwell.DataBase.DataBase;

/**
 * Created by Maciek on 15.10.2017.
 */

public class UserTable {

    public static final String dbTAG= "UserTable";

    private SQLiteDatabase mSQLiteDatabase;
    private DataBase mDataBase;

    public UserTable(DataBase dataBase){
        mDataBase = dataBase;
        try{
            open();
        }catch(SQLException e){
            Log.e(dbTAG, "SQLException on openning databas " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException{
        mSQLiteDatabase = mDataBase.getWritableDatabase();
    };

    public void close(){
        mDataBase.close();
    }

    public Cursor getAllData(){
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM " + DataBase.TABLE_USER_TABLE, null);
        return cursor;
    }

    public boolean insertData(String userName)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBase.COLUMNN_USER_TABLE_USER_NAME, userName);

        long result = mSQLiteDatabase.insert(DataBase.TABLE_USER_TABLE, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateDate(int userID, String userName)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBase.COLUMNN_USER_TABLE_USER_NAME, userName);

        mSQLiteDatabase.update(DataBase.TABLE_USER_TABLE, contentValues, DataBase.COLUMNN_USER_TABLE_USER_ID + " = ?", new String[] {Integer.toString(userID)});
        return true;
    }

    public Integer deleteData(int userID)
    {
        return mSQLiteDatabase.delete(DataBase.TABLE_USER_TABLE, DataBase.COLUMNN_USER_TABLE_USER_ID + " = ?", new String[] {Integer.toString(userID)});
    }

}
