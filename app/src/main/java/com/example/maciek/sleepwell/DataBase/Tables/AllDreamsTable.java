package com.example.maciek.sleepwell.DataBase.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maciek.sleepwell.DataBase.DataBase;

/**
 * Created by Maciek on 15.10.2017.
 */

public class AllDreamsTable {

    public static final String dbTAG= "UserTable";

    private SQLiteDatabase mSQLiteDatabase;
    private DataBase mDataBase;


    public AllDreamsTable(DataBase dataBase){
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
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM " + DataBase.TABLE_ALL_DREAMS_TABLE, null);
        return cursor;
    }

    public boolean insertData(Double avrREMTime, Double avrNREMTime, Double avrDeepTime, Double avrShallowTime, int sleepQuality,
                              int timeOfSleep, String sleepStartDate, String sleepStopDate, String awakePhase)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_REM_TIME, Double.toString(avrREMTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_NREM_TIME, Double.toString(avrNREMTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_DEEP_TIME, Double.toString(avrDeepTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_SHALLOW_TIME, Double.toString(avrShallowTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_EVALUATION_OF_SLEEP_QUALITY, Integer.toString(sleepQuality));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_TIME_OF_SLEEP, Integer.toString(timeOfSleep));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_SLEEP_START_DATE, sleepStartDate);
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_SLEEP_STOP_DATE, sleepStopDate);
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_PHASE_OF_AWAKENING, awakePhase);

        long result = mSQLiteDatabase.insert(DataBase.TABLE_ALL_DREAMS_TABLE, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateDate(int sleepID,Double avrREMTime, Double avrNREMTime, Double avrDeepTime, Double avrShallowTime, int sleepQuality,
                              int timeOfSleep, String sleepStartDate, String SleepStopDate, String awakePhase)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_REM_TIME, Double.toString(avrREMTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_NREM_TIME, Double.toString(avrNREMTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_DEEP_TIME, Double.toString(avrDeepTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_AVERANGE_SHALLOW_TIME, Double.toString(avrShallowTime));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_EVALUATION_OF_SLEEP_QUALITY, Integer.toString(sleepQuality));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_TIME_OF_SLEEP, Integer.toString(timeOfSleep));
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_SLEEP_START_DATE, sleepStartDate);
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_SLEEP_STOP_DATE, SleepStopDate);
        contentValues.put(DataBase.COLUMNN_ALL_DREAMS_TABLE_PHASE_OF_AWAKENING, awakePhase);

        mSQLiteDatabase.update(DataBase.TABLE_ALL_DREAMS_TABLE, contentValues, DataBase.COLUMNN_ALL_DREAMS_TABLE_SLEEP_ID + " = ?", new String[] {Integer.toString(sleepID)});
        return true;
    }

    public Integer deleteData(int sleepID)
    {
        return mSQLiteDatabase.delete(DataBase.TABLE_USER_TABLE, DataBase.COLUMNN_USER_TABLE_USER_ID + " = ?", new String[] {Integer.toString(sleepID)});
    }
}
