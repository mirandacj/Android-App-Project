package com.thecomputors.reminder;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thecomputors.reminder.sqldb.AlertMsg;
import com.thecomputors.reminder.sqldb.AlertTime;
import com.thecomputors.reminder.sqldb.Alert;
import com.thecomputors.reminder.sqldb.DbHelper;

import java.text.SimpleDateFormat;

/**
 * Created by mitchem on 11/14/2015.
 */
public class CreateReminder extends Activity{

    private EditText msgEdit;
    private CheckBox soundCb;
    private CheckBox repeatCb;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private SQLiteDatabase db;


    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Reminder");
        setContentView(R.layout.add_reminder);
        findViews();
        db = Reminder.db;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("hour", timePicker.getCurrentHour());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        timePicker.setCurrentHour(state.getInt("hour"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sdf.applyPattern(Reminder.getDateFormat());
    }

    private void findViews() {
        msgEdit = (EditText) findViewById(R.id.msg_et);
        soundCb = (CheckBox) findViewById(R.id.sound_cb);
        repeatCb = (CheckBox) findViewById(R.id.repeat_cb);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

    }

    private boolean validate() {
        if (TextUtils.isEmpty(msgEdit.getText())) {
            msgEdit.requestFocus();
            Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /* Save */
    public void create(View v) {
        if (!validate()) return;

        Alert alert = new Alert();
        alert.setName(msgEdit.getText().toString());
        alert.setSound(soundCb.isChecked());
        AlertTime alertTime = new AlertTime();
        long alertId = 0;

        if(!repeatCb.isChecked()) {   //one time
            alert.setFromDate(DbHelper.getDateStr(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
            alertId = alert.persist(db);

            alertTime.setAt(DbHelper.getTimeStr(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
            alertTime.setAlertId(alertId);
            alertTime.persist(db);
        }
            //interval = mins, hours, days, months, years
        else {    // repeating
                alert.setFromDate(DbHelper.getDateStr(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                alert.setToDate(DbHelper.getDateStr(datePicker.getYear() + 1, datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                alert.setInterval(Util.concat(0, " ",0, " ", 1, " ", 0, " ", 0));
                alertId = alert.persist(db);
                alertTime.setAt(DbHelper.getTimeStr(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                alertTime.setAlertId(alertId);
                alertTime.persist(db);
        }

        Intent service = new Intent(this, AlarmService.class);
        service.putExtra(AlertMsg.COL_ALERTID, String.valueOf(alertId));
        service.setAction(AlarmService.POPULATE);
        startService(service);

        finished();
    }

    public void finished(){
        Intent i = new Intent(getApplicationContext(), DisplayActivity.class);
        startActivity(i);
        finish();
        super.onStop();
    }

}
