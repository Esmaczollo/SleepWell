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

    public static final String TAG = "DataBase";

    //columns of the UserTable
    public static final String TABLE_USER_TABLE = "USER_TABLE";
    public static final String COLUMNN_USER_TABLE_USER_ID = "USER_ID";
    public static final String COLUMNN_USER_TABLE_USER_NAME ="USER_NAME";
    //mozna jeszcze dodac jakies ustawienia itd

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
    public static final String COLUMNN_ALL_DREAMS_TABLE_USER_ID = "USER_ID";

    //columns of the CurrentDreamTable
    public static final String TABLE_CURRENT_DREAM_TABLE = "CURRENT_DREAM_TABLE";
    public static final String COLUMN_CURRENT_DREAM_TABLE_SAMPLE_ID = "SAMPLE_ID";
    public static final String COLUMN_CURRENT_DREAM_TABLE_AMPLITUDE_OF_BREATH = "AMPLITUDE_OF_BREATH";
    public static final String COLUMN_CURRENT_DREAM_TABLE_DATE_OF_BREATH = "DATE_OF_BREATH";
    public static final String COLUMN_CURRENT_DREAM_TABLE_TYPE_OF_SLEEP_PHASE = "TYPE_OF_SLEEP_PHASE";
    public static final String COLUMN_CURRENT_DREAM_TABLE_WHERE_WE_ARE_IN_PHASE = "WHERE_WE_ARE_IN_PHASE";
    public static final String COLUMN_CURRENT_DREAM_TABLE_TREND = "TREND";
    public static final String COLUMN_CURRENT_DREAM_TABLE_SLEEP_ID = "SLEEP_ID";


    public static final String DATABASE_NAME ="DataBase.db";
    public static final int DATABASE_VERSION = 1;

    //SQL statement of UserTable creation
    public static final String SQL_CREATE_TABLE_USER_TABLE= "CREATE TABLE " + TABLE_USER_TABLE + "("
            + COLUMNN_USER_TABLE_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNN_USER_TABLE_USER_NAME + " TEXT"
            + ");";

    //SQL statement of AllDreamsTable creation
    public static final String SQL_CREATE_TABLE_ALL_DREAMS_TABLE = "CREATE TABLE " + TABLE_ALL_DREAMS_TABLE + "("
            + COLUMNN_ALL_DREAMS_TABLE_SLEEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_REM_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_NREM_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_DEEP_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_AVERANGE_SHALLOW_TIME + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_EVALUATION_OF_SLEEP_QUALITY + " INTEGER, "
            + COLUMNN_ALL_DREAMS_TABLE_TIME_OF_SLEEP + " REAL NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_SLEEP_START_DATE + " DATE NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_SLEEP_STOP_DATE + " DATE NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_PHASE_OF_AWAKENING + " TEXT NOT NULL, "
            + COLUMNN_ALL_DREAMS_TABLE_USER_ID + " INTEGER NOT NULL "
            + ");";

    //SQL statement of AllDreamsTable creation
    public static final String SQL_CREATE_TABLE_CURRENT_DREAM_TABLE = "CREATE TABLE " + TABLE_CURRENT_DREAM_TABLE + "("
            + COLUMN_CURRENT_DREAM_TABLE_SAMPLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CURRENT_DREAM_TABLE_AMPLITUDE_OF_BREATH + " REAL NOT NULL, "
            + COLUMN_CURRENT_DREAM_TABLE_DATE_OF_BREATH + " DATE NOT NULL, "
            + COLUMN_CURRENT_DREAM_TABLE_TYPE_OF_SLEEP_PHASE + " TEXT NOT NULL, "
            + COLUMN_CURRENT_DREAM_TABLE_WHERE_WE_ARE_IN_PHASE + " REAL NOT NULL, "
            + COLUMN_CURRENT_DREAM_TABLE_TREND + " TEXT NOT NULL, "
            + COLUMN_CURRENT_DREAM_TABLE_SLEEP_ID + " INTEGER NOT NULL "
            + ");";


    public DataBase(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("Konstruktor DataBase");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String dropTable = "DROP TABLE IF EXISTS ";

        db.execSQL(dropTable + TABLE_USER_TABLE);
        db.execSQL(dropTable + TABLE_ALL_DREAMS_TABLE);
        db.execSQL(dropTable + TABLE_CURRENT_DREAM_TABLE);

        System.out.println("Tworzenie tabel: START");
        db.execSQL(SQL_CREATE_TABLE_USER_TABLE);
        //db.execSQL(SQL_CREATE_TABLE_ALL_DREAMS_TABLE);
        //db.execSQL(SQL_CREATE_TABLE_CURRENT_DREAM_TABLE);
        System.out.println("Tworzenie tabel: STOP");

        db = getReadableDatabase();
        Cursor dbCursor = db.query(TABLE_USER_TABLE, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
        //System.out.println(columnNames);

//        for (String s : columnNames)
//        {
//            System.out.println(s);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS ";

        db.execSQL(dropTable + TABLE_USER_TABLE);
        db.execSQL(dropTable + TABLE_ALL_DREAMS_TABLE);
        db.execSQL(dropTable + TABLE_CURRENT_DREAM_TABLE);

        onCreate(db);
    }


}
