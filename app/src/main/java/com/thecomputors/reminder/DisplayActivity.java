package com.thecomputors.reminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.thecomputors.reminder.sqldb.Alert;
import com.thecomputors.reminder.sqldb.AlertMsg;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by mitchem on 11/14/2015.
 */
public class DisplayActivity extends ListActivity {

    private TextView header;
    private TextView dateRange;

    private Typeface type;
    private boolean change = false;

    private SQLiteDatabase db;

    public final Calendar cal = Calendar.getInstance();
    public final Date dt = new Date();
    private String[] monthArr;

    private Alert alert = new Alert();
    private AlertMsg alertMsg = new AlertMsg();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.display_layout);
        findViews();
        db = Reminder.db;
        monthArr = getResources().getStringArray(R.array.spinner3_arr);

        if(!Reminder.getFontPref().contains("Default")) {
            String font = "fonts/" + Reminder.getFontPref().toLowerCase() +".ttf";
            type = Typeface.createFromAsset(getAssets(),font);
            header.setTypeface(type);
            dateRange.setTypeface(type);
            change=true;
        }

        int r = Reminder.getDateRange();
        switch(r){
            case 3:
                cal.set(Calendar.MONTH, 0);
            case 2:
                cal.set(Calendar.DATE, 1);
            case 1:
                if(r==1) cal.set(Calendar.DATE, cal.getFirstDayOfWeek());
            case 0:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
        }

        registerForContextMenu(getListView());

    }

    private void findViews() {
        header = (TextView) findViewById(R.id.tvHeading);
        dateRange = (TextView) findViewById(R.id.tvDateRange);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("cal", cal.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        cal.setTimeInMillis(state.getLong("cal"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSettings:
                startActivity(new Intent(this, DisplayPref.class));
                break;
            case R.id.btnAdd:
                startActivity(new Intent(this, CreateReminder.class));
                break;
            case R.id.btnLeft:
                move(-1);
                dateRange.setText(getRangeStr());
                ((SimpleCursorAdapter)getListAdapter()).changeCursor(createCursor());
                break;
            case R.id.btnRight:
                move(+1);
                dateRange.setText(getRangeStr());
                ((SimpleCursorAdapter)getListAdapter()).changeCursor(createCursor());
                break;
        }
    }

    private Cursor createCursor() {
        Cursor c = Reminder.dbHelper.listNotifications(db, cal.getTimeInMillis() + move(+1), cal.getTimeInMillis() + move(-1));
        startManagingCursor(c);
        return c;
    }

    private String move(int step) {
        switch(Reminder.getDateRange()) {
            case 0: //add one day
                cal.add(Calendar.DATE, 1 * step);
                break;
            case 1: //add one week
                cal.add(Calendar.DATE, 7 * step);
                break;
            case 2: //add one month
                cal.add(Calendar.MONTH, 1 * step);
                break;
            case 3: //add one year
                cal.add(Calendar.YEAR, 1 * step);
                break;
        }
        return "";
    }


    @Override
    protected void onResume() {
        super.onResume();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.reminder,
                createCursor(),
                new String[]{Alert.COL_NAME, AlertMsg.COL_DATETIME, AlertMsg.COL_DATETIME,
                AlertMsg.COL_DATETIME, AlertMsg.COL_DATETIME},
                new int[]{R.id.msg_tv, R.id.time_tv});

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                TextView tv = (TextView)view;

                if (view.getId() == R.id.msg_tv) {
                    if(change){
                        tv.setTypeface(type);
                    }
                    return false;
                }

                switch(view.getId()) {
                    case R.id.time_tv:
                        dt.setTime(cursor.getLong(columnIndex));
                        tv.setText(dt.getHours()+":"+dt.getMinutes());
                        if(change){
                            tv.setTypeface(type);
                        }
                        break;
                }
                return true;
            }
        });
        setListAdapter(adapter);
        dateRange.setText(getRangeStr());
    }

    /*
      Creates menu when item is selected from list
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
            menu.setHeaderTitle("Choose an Option");
            menu.setHeaderIcon(R.drawable.ic_drawer);

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            alertMsg.setId(info.id);
            alertMsg.load(db);
            if (alertMsg.getDateTime() < System.currentTimeMillis())
                menu.removeItem(R.id.menu_edit);
        }
    }

    /*
       Context menu functionality for when item is selected
       Delete deletes a singular reminder
       Delete Repeating deletes all instances of the event (in the case of those that reoccur)
       Edit is self explanatory
     */

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        boolean refresh = false;

        switch (item.getItemId()) {
            case R.id.menu_edit:
                alertMsg.setId(info.id);
                alertMsg.load(db);
                alert.reset();
                alert.setId(alertMsg.getAlertId());
                alert.load(db);

                showDialog(R.id.menu_edit);
                break;

            case R.id.menu_delete:
                Reminder.dbHelper.cancelNotification(db, info.id, false);
                refresh = true;

                Intent cancelThis = new Intent(this, AlarmService.class);
                cancelThis.putExtra(AlertMsg.COL_ID, String.valueOf(info.id));
                cancelThis.setAction(AlarmService.CANCEL);
                startService(cancelThis);
                break;

            case R.id.menu_delete_repeating:
                alertMsg.setId(info.id);
                alertMsg.load(db);
                Reminder.dbHelper.cancelNotification(db, alertMsg.getAlertId(), true);
                refresh = true;

                Intent cancelRepeating = new Intent(this, AlarmService.class);
                cancelRepeating.putExtra(AlertMsg.COL_ALERTID, String.valueOf(alertMsg.getAlertId()));
                cancelRepeating.setAction(AlarmService.CANCEL);
                startService(cancelRepeating);
                break;
        }

        if (refresh) {
            SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
            adapter.getCursor().requery();
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    private String getRangeStr() {
        int date = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        dt.setTime(System.currentTimeMillis());

        switch(Reminder.getDateRange()) {
            case 0: // Daily
                if (date==dt.getDate() && month==dt.getMonth() && year==dt.getYear()+1900) return "Today";
                else return date+" "+monthArr[month+1];

            case 1: // Weekly
                return date+" "+monthArr[month+1] + move(+1) + " - " + cal.get(Calendar.DATE)+" "+monthArr[cal.get(Calendar.MONTH)+1] + move(-1);

            case 2: // Monthly
                return monthArr[month+1]+" "+year;

            case 3: // Yearly
                return year+"";
        }
        return null;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        openContextMenu(v);
    }

    /*
       Edit dialog
     */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case R.id.menu_edit:
                return new AlertDialog.Builder(this)
                        .setTitle("Edit")
                        .setView(getLayoutInflater().inflate(R.layout.edit, null))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog d = (Dialog) dialog;
                                EditText msgEdit = (EditText) d.findViewById(R.id.msg_et);
                                CheckBox soundCb = (CheckBox) d.findViewById(R.id.sound_cb);

                                alert.setSound(soundCb.isChecked());
                                if (!TextUtils.isEmpty(msgEdit.getText())) {
                                    alert.setName(msgEdit.getText().toString());
                                    alert.persist(db);

                                    SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
                                    adapter.getCursor().requery();
                                    adapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(DisplayActivity.this, "Enter a message", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case R.id.menu_edit:
                EditText msgEdit = (EditText) dialog.findViewById(R.id.msg_et);
                CheckBox soundCb = (CheckBox) dialog.findViewById(R.id.sound_cb);

                msgEdit.setText(alert.getName());
                soundCb.setChecked(alert.getSound());
                break;
        }
    }

}
