package com.example.prashant.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class Calender extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        overridePendingTransition(R.anim.slide_in, R.anim.hold);

        setContentView(R.layout.activity_calender);

        CalendarView view = new CalendarView(this);
        setContentView(view);

        view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                Toast.makeText(getApplicationContext(),date+ "/"+(month+1)+"/"+year,Toast.LENGTH_SHORT).show();
                int increasedMonth = month+1;
//                String dateToSend = date + " - " + increasedMonth + " - " + year;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("date", dateToSend);
////                intent.putExtra("data", bundle);
                intent.putExtra("date", date);
                intent.putExtra("month", increasedMonth);
                intent.putExtra("year", year);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        // Whenever this activity is paused (i.e. looses focus because another activity is started etc)
        // Override how this activity is animated out of view
        // The new activity is kept still and this activity is pushed out to the left
        overridePendingTransition(R.anim.hold, R.anim.slide_out);
        super.onPause();
    }
}
