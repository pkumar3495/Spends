package com.example.prashant.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Prashant on 11/8/2016.
 */

public class TaskDBHelper extends SQLiteOpenHelper{

    public TaskDBHelper(Context context){
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " NUMBER NOT NULL, " +
                TaskContract.TaskEntry.COL_TYPE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_SUM + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_DATE + " NUMBER NOT NULL);";

        String createTable1 = "CREATE TABLE " + TaskContract.TaskEntry.TABLE1 + " ( " +
                TaskContract.TaskEntry.COL_SAVINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_DAILY_BUDGET + " NUMBER, " +
                TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST + " NUMBER, " +
                TaskContract.TaskEntry.COL_MONTHLY_BUDGET + " NUMBER, " +
                TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST + " NUMBER, " +
                TaskContract.TaskEntry.COL_SAVINGS + " NUMBER);";

        db.execSQL(createTable);
        db.execSQL(createTable1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}