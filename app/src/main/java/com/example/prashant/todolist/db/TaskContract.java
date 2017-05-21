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
        public static final String COL_TYPE = "type";
        public static final String COL_SUM = "sum";
        public static final String COL_DATE = "date";

        public static final String TABLE1 = "savings";
        public static final String COL_SAVINGS_ID = "savings_id";
        public static final String COL_DAILY_BUDGET = "daily_budget";
        public static final String COL_DAILY_BUDGET_CONST = "daily_budget_const";
        public static final String COL_MONTHLY_BUDGET = "monthly_budget";
        public static final String COL_MONTHLY_BUDGET_CONST = "monthly_budget_const";
        public static final String COL_SAVINGS = "savings";
    }

}
