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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

// Variables

    private static final String TAG = "MainActivity";
    private TaskDBHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<CharSequence> adapter;
//    TextView type = (TextView)findViewById(R.id.type_textView);
    TextView total_text = (TextView) findViewById(R.id.total_text);
    int total = 0;

// OnCreate method with the current time initialisations and displaying it.
// Also the database initialisations.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView todaysDate = (TextView) findViewById(R.id.todaysDate);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        todaysDate.setText(currentDateTimeString);

        mHelper = new TaskDBHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();
        updateUI();

    }

// Creating the all option menu (visible and hidden)

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

        final Spinner spinner = (Spinner)v.findViewById(R.id.type);
        adapter = ArrayAdapter.createFromResource(this, R.array.type_of_spends, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+" selected", Toast.LENGTH_SHORT).show();
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
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
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

            case R.id.change_budget:
                final EditText taskEditText1 = new EditText(this);
                AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("Change budget")
                        .setMessage("Enter your preferred budget?")
                        .setView(taskEditText1)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                budget_change_toast_message();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog1.show();
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
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

// Function for the cross button to delete the entry from the database and update the UI

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

// Trying to detect the back key press to close the app.

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


// function trying to calculate total dynamically

//    public void calc_total(){
//        TextView taskTitle = (TextView)findViewById(R.id.task_title);
//        String value_buffer = taskTitle.getText().toString();
//        int value_entered = Integer.parseInt(value_buffer);
//        total = total + value_entered;
//        total_text.setText(total);
//    }
}
