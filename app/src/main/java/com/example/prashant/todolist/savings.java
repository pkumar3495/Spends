package com.example.prashant.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class savings extends AppCompatActivity {

    int dailyBudget;
    int monthlyBudget;
    int savings;
    TextView savingsText;
    TextView dayLeft;
    TextView monthLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        savingsText = (TextView) findViewById(R.id.saving_text);
        dayLeft = (TextView) findViewById(R.id.day_left);
        monthLeft = (TextView) findViewById(R.id.month_left);

        dailyBudget = getIntent().getExtras().getInt("dailyBudget");
        monthlyBudget = getIntent().getExtras().getInt("monthlyBudget");
        savings = getIntent().getExtras().getInt("savings");

        savingsText.setText(""+savings);
        dayLeft.setText(""+dailyBudget);
        monthLeft.setText(""+monthlyBudget);
    }
}
