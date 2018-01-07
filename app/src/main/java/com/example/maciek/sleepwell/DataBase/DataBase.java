package com.example.maciek.sleepwell.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maciek on 15.10.2017.
 */

public class DataBase extends SQLiteOpenHelper {

    //columns of the UserTable
    public static final String TABLE_USER_TABLE = "USER_TABLE";
    public static final String COLUMNN_USER_TABLE_USER_ID = "USER_ID";
    public static final String COLUMNN_USER_TABLE_USER_NAME ="USER_NAME";
    public static final String COLUMNN_USER_TABLE_USER_PASSWORD ="USER_PASSWORD";

    //columns of the AllDreamsTable
    public static final String TABLE_ALL_DREAMS_TABLE = "ALL_DREAMS_TABLE";
    public static final String COLUMNN_ALL_DREAMS_TABLE_SLEEP_ID = "SLEEP_ID";
    public static final String COLUMNN_ALL_DREAMS_TABLE_AVERANGE_REM_TIME = "AVERANGE_REM_TIME";
    public static final String COLUMNN_ALL_DREAMS_TABLE_AVERANGE_NREM_TIME = "AVERANGE_NREM_TIME";
    public static final String COLUMNN_ALL_DREAMS_TABLE_AVERANGE_DEEP_TIME = "AVERANGE_DEEP_TIME";
    public static final String COLUMNN_ALL_DREAMS_TABLE_AVERANGE_SHALLOW_TIME = "AVERANGE_SHALLOW_TIME";
    public static final String COLUMNN_ALL_DREAMS_TABLE_EVALUATION_OF_SLEEP_QUALITY = "EVALUATION_OF_SLEEP_QUALITY";
    public static final String COLUMNN_ALL_DREAMS_TABLE_TIME_OF_SLEEP = "TIME_OF_SLEEP";
    public static final String COLUMNN_ALL_DREAMS_TABLE_SLEEP_START_DATE = "SLEEP_START_DATE";
    public static final String COLUMNN_ALL_DREAMS_TABLE_SLEEP_STOP_DATE = "SLEEP_STOP_DATE";
    public static final String COLUMNN_ALL_DREAMS_TABLE_PHASE_OF_AWAKENING = "PHASE_OF_AWAKENING";

    //colums of the OneDreamTable
    public static final String TABLE_ONE_DREAM_TABLE = "ONE_DREAM_TABLE";
    public static final String COLUMNN_ONE_DREAM_TABLE_ROW_ID = "PRIMARY_KEY_ID ";
    public static final String COLUMNN_ONE_DREAM_TABLE_SLEEP_ID = "SLEEP_ID";
    public static final String COLUMNN_ONE_DREAM_TABLE_PHASE_TIME= "PHASE_TIME";
    public static final String COLUMNN_ONE_DREAM_TABLE_PHASE_TYPE = "PHASE_TYPE";

    //SQL statement of UserTable creation
    private static final String SQL_CREATE_TABLE_USER_TABLE= "CREATE TABLE " + TABLE_USER_TABLE + "("
            + COLUMNN_USER_TABLE_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNN_USER_TABLE_USER_NAME + " TEXT"
            + ");";

    //SQL statement of AllDreamsTable creation
    private static final String SQL_CREATE_TABLE_ALL_DREAMS_TABLE = "CREATE TABLE " + TABLE_ALL_DREAMS_TABLE + "("
            + COLUMNN_ALL_DREAMS_TABLE_SLEEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_REM_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_NREM_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_DEEP_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_SHALLOW_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_EVALUATION_OF_SLEEP_QUALITY + " INTEGER, "
            + COLUMNN_ALL_DREAMS_TABLE_TIME_OF_SLEEP + " INTEGER NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_SLEEP_START_DATE + " TEXT NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_SLEEP_STOP_DATE + " TEXT NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_PHASE_OF_AWAKENING + " TEXT NOT NULL"
            + ");";

    //SQL statement of OneDreamTable creation
    private static final String SQL_CREATE_TABLE_ONE_DREAM_TABLE= "CREATE TABLE " + TABLE_ONE_DREAM_TABLE + "("
            + COLUMNN_ONE_DREAM_TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNN_ONE_DREAM_TABLE_SLEEP_ID + " INTEGER, "
            + COLUMNN_ONE_DREAM_TABLE_PHASE_TIME + " TEXT"
            + COLUMNN_ONE_DREAM_TABLE_PHASE_TYPE + " TEXT"
            + ");";



    public static final String DATABASE_NAME ="DataBase.db";
    public static final int DATABASE_VERSION = 1;


    public DataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER_TABLE);
        db.execSQL(SQL_CREATE_TABLE_ALL_DREAMS_TABLE);
        db.execSQL(SQL_CREATE_TABLE_ONE_DREAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_DREAMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_DREAM_TABLE);
        onCreate(db);
    }
    /**
     * Metoda pozwalająca wpisać dane do bazy
     * @param SLEEP_ID
     * @param PHASE_TIME
     * @param PHASE_TYPE
     * @return
     */
    public boolean insertDataToOneDreamTable(String SLEEP_ID, String PHASE_TIME, String PHASE_TYPE){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMNN_ONE_DREAM_TABLE_SLEEP_ID,SLEEP_ID);
        contentValues.put(COLUMNN_ONE_DREAM_TABLE_PHASE_TIME,PHASE_TIME);
        contentValues.put(COLUMNN_ONE_DREAM_TABLE_PHASE_TYPE,PHASE_TYPE);
        long result = database.insert(TABLE_ONE_DREAM_TABLE, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public int getMaxSleepIDFromOneDreamTable(){
        String sqlQuery = "SELECT MAX(" + COLUMNN_ONE_DREAM_TABLE_SLEEP_ID + ")" + " FROM " + TABLE_ONE_DREAM_TABLE;
//                + " ORDER BY " + COLUMNN_ONE_DREAM_TABLE_SLEEP_ID + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(sqlQuery, null);
        int maxId = 0;
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0);
            return maxId;
        }
        else        {
            return maxId;
        }

    }
}
