package com.example.o0none0o.reminder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateEvent extends Activity {
    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;
    EditText text;
    String name, desc;
    StringBuilder sb = new StringBuilder();
    FileOutputStream out = null;
    private int year;
    private int month;
    private int day;
    final static int RQS_1 = 1;

    private TextView tvDisplayTime;
    private TimePicker timePicker1;
    private Button btnChangeTime;

    private int hour;
    private int minute;

    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 888;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_layout);
        setCurrentDateOnView();
        setCurrentTimeOnView();
        addListenerOnButton();

    }

    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.dateResult);
        dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        dpResult.init(year, month, day, null);

    }

    public void setCurrentTimeOnView() {

        tvDisplayTime = (TextView) findViewById(R.id.tvTime);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // set current time into textview
        tvDisplayTime.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));

        // set current time into timepicker
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);

    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        btnChangeDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

        btnChangeTime = (Button) findViewById(R.id.btnChangeTime);

        btnChangeTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute, false);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also
            dpResult.init(year, month, day, null);

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    tvDisplayTime.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

                    // set current time into timepicker
                    timePicker1.setCurrentHour(hour);
                    timePicker1.setCurrentMinute(minute);

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    //this method should add to array and switch activities
    public void createReminder(View view) {
        text = (EditText) findViewById(R.id.eventName);
        name = text.getText().toString();

        text = (EditText) findViewById(R.id.eventDesc);
        desc = text.getText().toString();

        TextView text2 = (TextView) findViewById(R.id.tvTime);
        String time = text2.getText().toString();
        text2 = (TextView) findViewById(R.id.dateResult);
        time = text2.getText().toString() + " " + time + ":00";

        Event temp = new Event(name, time, desc);
        setAlarm(temp);
        writeOut(name, desc, time);


    }


    public void writeOut(String name, String text, String time) {
        try {
            out = openFileOutput("Reminders.txt", 0);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            sb.append(name);
            sb.append("~ ");
            sb.append(text);
            sb.append("~ ");
            sb.append(time);
            sb.append(System.getProperty("line.separator"));
            out.write(sb.toString().getBytes());
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void done(View view) {
            Intent intent = new Intent(this, DisplayActivity.class);
            startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
        private void setAlarm(Event events){
        final int _id = (int) System.currentTimeMillis();

//call push noti
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), _id, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, events.getEnd().getTimeInMillis(), pendingIntent);

//call pop up dialog
        Intent intent2 = new Intent(getBaseContext(), AlertAlarm.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(getBaseContext(), _id, intent2, 0);
        AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager2.setExact(AlarmManager.RTC_WAKEUP, events.getEnd().getTimeInMillis(), pendingIntent2);
    }

}

