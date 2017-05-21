//Created by Prashant.
//..
package com.example.prashant.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prashant.todolist.db.TaskContract;
import com.example.prashant.todolist.db.TaskDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

// Variables

    private static final String TAG = "MainActivity";
    private TaskDBHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private String itemSelected;
    //    TextView type = (TextView)findViewById(R.id.type_textView);
    TextView total_text;
    int total = 0;
    TextView taskTitle;
    String symbol;
    public int valueEntered;
    int try1;
    int try2;
    int dateReceived;
    int monthReceived;
    int yearReceived;
    List <String> list = new ArrayList<String>();
    String dateCombined_buffer;
    int dateCombined;

    int totalBudget;
    int lastDate;
    int dailyBudget;
    int count;
    TextView dailyBudgetTextView;
    int savings;
    int currTask = 0;
    int totalBudgetConst;
    int dailyBudgetConst;
    int flag = 0;

    int dailyBudgetToSend;
    int dailyBudgetToSendConst;
    int monthlyBudgetToSend;
    int monthlyBudgetToSendConst;
    int savingsToSend;

// OnCreate method with the current time initialisations and displaying it.
// Also the database initialisations.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Currency currency = Currency.getInstance(Locale.getDefault());
        symbol = currency.getSymbol();

        taskTitle = (TextView)findViewById(R.id.task_title);
        total_text = (TextView) findViewById(R.id.total_text);
        dailyBudgetTextView = (TextView) findViewById(R.id.daily_budget_textView);
//        total_text.setText(total);
//        calc_total();

        TextView todaysDate = (TextView) findViewById(R.id.todaysDate);
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd - MMM - yyyy");
//        String currentDateTimeString = df.format(c.getTime());
        final Calendar c = Calendar.getInstance();
        yearReceived = c.get(Calendar.YEAR);
        int mm_buffer = c.get(Calendar.MONTH);
        monthReceived = mm_buffer + 1;
        dateReceived = c.get(Calendar.DAY_OF_MONTH);
        String currentDateTimeString = dateReceived + " - " + monthReceived + " - " + yearReceived;
//        dateReceived = currentDateTimeString;
        todaysDate.setText(currentDateTimeString);

        lastDate = Calendar.getInstance().getActualMaximum(c.DAY_OF_MONTH);

        //Receiving data from calender activity
//        Bundle bundle = getIntent().getBundleExtra("data");

        Intent intent = getIntent();
        if (intent.hasExtra("date")) {
            dateReceived = getIntent().getExtras().getInt("date");
            monthReceived = getIntent().getExtras().getInt("month");
            yearReceived = getIntent().getExtras().getInt("year");
            Log.e(TAG, "" + dateReceived);
            Log.e(TAG, "" + monthReceived);
            Log.e(TAG, "" + yearReceived);
            todaysDate.setText(dateReceived + " - " + monthReceived + " - " + yearReceived);
            flag = 1;
        }
        Log.e(TAG, "Current date : " + dateReceived);
        Log.e(TAG, "Current month : " + monthReceived);
        Log.e(TAG, "Current year : " + yearReceived);

        dateCombined_buffer = dateReceived+""+monthReceived+""+yearReceived;
        dateCombined = Integer.parseInt(dateCombined_buffer);

        mHelper = new TaskDBHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TYPE, TaskContract.TaskEntry.COL_SUM, TaskContract.TaskEntry.COL_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TYPE);
            int idx2= cursor.getColumnIndex(TaskContract.TaskEntry.COL_SUM);
            int idx3= cursor.getColumnIndex(TaskContract.TaskEntry.COL_DATE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
            Log.d(TAG, "Type: " + cursor.getString(idx1));
            Log.d(TAG, "Sum: " + cursor.getString(idx2));
            Log.d(TAG, "Date: " + cursor.getString(idx3));
        }
        cursor.close();
        db.close();
        updateUI();
    }

// Creating all the option menu (visible and hidden)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

// Checking for selection and initialising the custom layout with the spinner for the alert dialog box.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.activity_dialog_layout, null);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_dialog_layout, null);

        LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = inflater1.inflate(R.layout.budget_dialog_layout, null);

        final Spinner spinner = (Spinner)v.findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_spends, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+" selected", Toast.LENGTH_SHORT).show();
                itemSelected = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

// All the menu buttons functionality checking with the switch statement
// Also calling updateUi everytime to update the according to the entries in the database

        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = (EditText)v.findViewById(R.id.Amount);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add")
                        .setMessage("How much did u spend?")
                        .setView(v)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
//                                TextView todaysDate = (TextView) findViewById(R.id.todaysDate);
//                                String date = todaysDate.getText().toString();
                                String date_buffer = dateReceived+""+monthReceived+""+yearReceived;
                                int date = Integer.parseInt(date_buffer);
                                String type = itemSelected;

                                currTask = Integer.parseInt(task);

                                //Calculating total
                                valueEntered = Integer.parseInt(task);
                                total = total + valueEntered;
//                                Log.e(TAG, "total : " +total);
                                total_text.setText(""+total);

                                String sum = " " + type + " ( " + symbol + " " + task + " )";
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                values.put(TaskContract.TaskEntry.COL_TYPE, type);
                                values.put(TaskContract.TaskEntry.COL_SUM, sum);
                                values.put(TaskContract.TaskEntry.COL_DATE, date);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            case R.id.calender:
                Intent intent = new Intent(this, Calender.class);
                startActivity(intent);
                return true;

            case R.id.refresh:
                updateUI();
//                Intent intent1 = new Intent(this, dialog_layout.class);
//                startActivity(intent1);
                return true;

            case R.id.savings:
                SQLiteDatabase fetchingCurrentResourceDb = mHelper.getReadableDatabase();
                Cursor fetchingCurrentResourceCursor = fetchingCurrentResourceDb.query(TaskContract.TaskEntry.TABLE1,
                        new String[]{TaskContract.TaskEntry.COL_SAVINGS_ID, TaskContract.TaskEntry.COL_DAILY_BUDGET, TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST, TaskContract.TaskEntry.COL_MONTHLY_BUDGET, TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST, TaskContract.TaskEntry.COL_SAVINGS},
                        null, null, null, null, null);
                while (fetchingCurrentResourceCursor.moveToNext()) {
                    int index = fetchingCurrentResourceCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAILY_BUDGET);
                    int index4 = fetchingCurrentResourceCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST);
                    int index1 = fetchingCurrentResourceCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTHLY_BUDGET);
                    int index2 = fetchingCurrentResourceCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST);
                    int index3 = fetchingCurrentResourceCursor.getColumnIndex(TaskContract.TaskEntry.COL_SAVINGS);
                    dailyBudgetToSend = fetchingCurrentResourceCursor.getInt(index);
                    dailyBudgetToSendConst = fetchingCurrentResourceCursor.getInt(index4);
                    monthlyBudgetToSend = fetchingCurrentResourceCursor.getInt(index1);
                    monthlyBudgetToSendConst = fetchingCurrentResourceCursor.getInt(index2);
                    savingsToSend = fetchingCurrentResourceCursor.getInt(index3);
                }
                fetchingCurrentResourceCursor.close();
                fetchingCurrentResourceDb.close();
                dailyBudgetToSend = dailyBudgetToSendConst - try1;
                monthlyBudgetToSend = monthlyBudgetToSendConst - try2;

                Intent intent2 = new Intent(this, savings.class);
                intent2.putExtra("dailyBudget", dailyBudgetToSend);
                intent2.putExtra("monthlyBudget", monthlyBudgetToSend);
                intent2.putExtra("savings", savingsToSend);
                startActivity(intent2);
                return true;

            case R.id.About:
                Intent intent1 = new Intent(this, About.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

// The display toast message after changing the budget successfully.

    public void budget_change_toast_message(){
        Toast.makeText(MainActivity.this, "Budget successfully changed :)", Toast.LENGTH_LONG).show();
    }

// Function to update UI according to the entries in the database.

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String> typeList = new ArrayList<>();
        ArrayList<String> sumList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT SUM(" + TaskContract.TaskEntry.COL_TASK_TITLE + ") FROM " + TaskContract.TaskEntry.TABLE +" WHERE " + TaskContract.TaskEntry.COL_DATE + "=" +dateCombined, null);
        if(cur.moveToFirst())
            try1 =  cur.getInt(0);
        total_text.setText(""+try1);

        Cursor cur1 = db.rawQuery("SELECT SUM(" + TaskContract.TaskEntry.COL_TASK_TITLE + ") FROM " + TaskContract.TaskEntry.TABLE, null);
        if(cur1.moveToFirst())
            try2 =  cur1.getInt(0);

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TYPE, TaskContract.TaskEntry.COL_SUM, TaskContract.TaskEntry.COL_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TYPE);
            int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_SUM);
            int idx3 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_DATE);
            taskList.add(cursor.getString(idx));
            typeList.add(cursor.getString(idx1));
            sumList.add(cursor.getString(idx2));
            dateList.add(cursor.getString(idx3));

            String dateBuffer_buffer = cursor.getString(idx3);
            int dateBuffer = Integer.parseInt(dateBuffer_buffer);
//            Log.e(TAG, "Date buffer : " + dateBuffer + " ----- Date Received : " + dateReceived );
            String dateCombined_buffer = dateReceived+""+monthReceived+""+yearReceived;
            int dateCombined = Integer.parseInt(dateCombined_buffer);
            if (dateBuffer == dateCombined)
                list.add(cursor.getString(idx2));
            System.out.println(list);
        }
//        SQLiteDatabase db1 = mHelper.getReadableDatabase();
//        Cursor cursor1 = db1.rawQuery("SELECT * FROM " + TaskContract.TaskEntry.TABLE, null);
//        count = cursor1.getCount();

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    list);
            mTaskListView.setAdapter(mAdapter);
        } else {
//            mAdapter.clear();
            Object[] st = list.toArray();
            for (Object s : st){
                if (list.indexOf(s) != list.lastIndexOf(s))
                    list.remove(list.lastIndexOf(s));
            }
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    list);
            mTaskListView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();

        //Counting the number of entries in the savings database to check whether the app is running for the first time or not

        SQLiteDatabase countDb = mHelper.getReadableDatabase();
        Cursor countCursor = countDb.rawQuery("SELECT COUNT(" + TaskContract.TaskEntry.COL_DAILY_BUDGET + ") FROM " + TaskContract.TaskEntry.TABLE1 + ";", null);
        if (countCursor.moveToFirst())
            count = countCursor.getInt(0);
        Log.e(TAG, "Count : " + count);
        countCursor.close();
        countDb.close();

        if (dateReceived == 1 || count == 0){
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setMaxLines(1);
            AlertDialog budgetEntryDialog = new AlertDialog.Builder(this)
                    .setTitle("Enter the budget for this month !")
                    .setView(editText)
                    .setCancelable(false)
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            totalBudget = 0;
                            dailyBudget = 0;
                            totalBudget = Integer.parseInt(editText.getText().toString());
                            totalBudgetConst = totalBudget;
                            dailyBudget = totalBudget / (lastDate - dateReceived);
                            dailyBudgetConst = dailyBudget;
                            dailyBudgetTextView.setText(""+dailyBudget);
                            savings = 0;
                            Log.e(TAG, "Total budget : " + totalBudget);
                            Log.e(TAG, "Daily budget : " + dailyBudget);
                            Log.e(TAG, "Savings : " + savings);
                            savingsToDb();
                            logging();
                        }
                    })
                    .create();
            budgetEntryDialog.show();
        }
        else {
            calculatingDailyBudget();
            logging();
        }
    }

    public void calculatingDailyBudget(){
        SQLiteDatabase fetchingTotalBudgetDb = mHelper.getReadableDatabase();
        Cursor fetchingTotalBudgetCursor = fetchingTotalBudgetDb.query(TaskContract.TaskEntry.TABLE1,
                new String[]{TaskContract.TaskEntry.COL_SAVINGS_ID, TaskContract.TaskEntry.COL_DAILY_BUDGET, TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST, TaskContract.TaskEntry.COL_MONTHLY_BUDGET, TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST, TaskContract.TaskEntry.COL_SAVINGS},
                null, null, null, null, null);
        while (fetchingTotalBudgetCursor.moveToNext()){
            int index = fetchingTotalBudgetCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAILY_BUDGET);
            int index4 = fetchingTotalBudgetCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST);
            int index1 = fetchingTotalBudgetCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTHLY_BUDGET);
            int index2 = fetchingTotalBudgetCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST);
            int index3 = fetchingTotalBudgetCursor.getColumnIndex(TaskContract.TaskEntry.COL_SAVINGS);
            dailyBudget = fetchingTotalBudgetCursor.getInt(index);
            dailyBudgetConst = fetchingTotalBudgetCursor.getInt(index4);
            totalBudget = fetchingTotalBudgetCursor.getInt(index1);
            totalBudgetConst = fetchingTotalBudgetCursor.getInt(index2);
            savings = fetchingTotalBudgetCursor.getInt(index3);
        }
        fetchingTotalBudgetCursor.close();
        fetchingTotalBudgetDb.close();

        int month;
        int monthBuffer;
        Calendar monthCheck = Calendar.getInstance();
        monthBuffer = monthCheck.get(Calendar.MONTH);
        month = monthBuffer + 1;

        if (flag == 0) {
            dailyBudget = totalBudgetConst / (lastDate - dateReceived);
            Log.e(TAG, "flag : " + flag);
        }
        else if (flag == 1) {
            if (month != monthReceived){
                AlertDialog invalidDateDialog = new AlertDialog.Builder(this)
                        .setTitle("Invalid date selection")
                        .setMessage("Please select the date within the current month !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getBaseContext(), Calender.class);
                                startActivity(intent);
                            }
                        })
                        .create();
                invalidDateDialog.show();
            }
            else {
                dailyBudget = dailyBudgetConst;
                Log.e(TAG, "flag : " + flag);
            }
        }
        dailyBudgetTextView.setText(""+dailyBudget);
        totalBudget = totalBudget - currTask;
        savings = totalBudget;
        savingsToDb();
    }

    public void savingsToDb(){
        SQLiteDatabase savingsToDbDb = mHelper.getWritableDatabase();
        ContentValues savingsToDbCv = new ContentValues();
        savingsToDbCv.put(TaskContract.TaskEntry.COL_DAILY_BUDGET, dailyBudget);
        savingsToDbCv.put(TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST, dailyBudgetConst);
        savingsToDbCv.put(TaskContract.TaskEntry.COL_MONTHLY_BUDGET, totalBudget);
        savingsToDbCv.put(TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST, totalBudgetConst);
        savingsToDbCv.put(TaskContract.TaskEntry.COL_SAVINGS, savings);
        savingsToDbDb.insertWithOnConflict(TaskContract.TaskEntry.TABLE1,
                null,
                savingsToDbCv,
                SQLiteDatabase.CONFLICT_REPLACE);
        savingsToDbDb.close();
    }

    public void logging(){
        SQLiteDatabase loggingDb = mHelper.getReadableDatabase();
        Cursor loggingCursor = loggingDb.query(TaskContract.TaskEntry.TABLE1,
                new String[]{TaskContract.TaskEntry.COL_SAVINGS_ID, TaskContract.TaskEntry.COL_DAILY_BUDGET, TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST, TaskContract.TaskEntry.COL_MONTHLY_BUDGET, TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST, TaskContract.TaskEntry.COL_SAVINGS},
                null, null, null, null, null);
        while (loggingCursor.moveToNext()) {
            int index = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAILY_BUDGET);
            int index4 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAILY_BUDGET_CONST);
            int index1 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTHLY_BUDGET);
            int index2 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTHLY_BUDGET_CONST);
            int index3 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_SAVINGS);
            Log.e(TAG, "Daily Budget from db: " + loggingCursor.getInt(index) + " Daily budget CONST from db : " + loggingCursor.getInt(index4) + " Monthly budget from db : " + loggingCursor.getInt(index1) + " Monthly budget CONST from db : " + loggingCursor.getInt(index2) + " Savings from db : " + loggingCursor.getInt(index3));
        }
    }

// Function for the cross button to delete the entry from the database and update the UI

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Cursor cur = db.rawQuery("SELECT SUM(" + TaskContract.TaskEntry.COL_TASK_TITLE + ") FROM " + TaskContract.TaskEntry.TABLE +" WHERE " + TaskContract.TaskEntry.COL_DATE + "=" +dateCombined, null);
        if(cur.moveToFirst())
            try1 =  cur.getInt(0);
        total_text.setText(""+try1);

        Log.e(TAG, "delete task : " +"\"" + task + "\"");
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_SUM + " = ? AND " + TaskContract.TaskEntry.COL_DATE + " = ?",
                new String[]{task, dateCombined_buffer});
        list.remove(task);
//        db.rawQuery("DELETE FROM " + TaskContract.TaskEntry.TABLE + " WHERE " + TaskContract.TaskEntry.COL_SUM + "=" +"\"" + task + "\""+ " AND " + TaskContract.TaskEntry.COL_DATE + "=" + dateCombined + ";", null);
        db.close();
        updateUI();
    }

// Trying to detect the back key press to close the app.
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit)
            finish(); //finish activity
        else{
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
