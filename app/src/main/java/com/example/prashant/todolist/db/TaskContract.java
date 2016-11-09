package com.example.prashant.todolist.db;
import android.provider.BaseColumns;

/**
 * Created by Prashant on 11/8/2016.
 */

public class TaskContract {

    public static final String DB_NAME = "com.example.prashant.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
    }

}
