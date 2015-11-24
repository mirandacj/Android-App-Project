package com.thecomputors.reminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import com.thecomputors.reminder.sqldb.Alert;
import com.thecomputors.reminder.sqldb.AlertMsg;

/**
 * Created by mitchem on 11/14/2015.
 */
public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";

    public static final String POPULATE = "POPULATE";
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super(TAG);
        matcher = new IntentFilter();
        matcher.addAction(POPULATE);
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String alertId = intent.getStringExtra(AlertMsg.COL_ALERTID);
        String alertMsgId = intent.getStringExtra(AlertMsg.COL_ID);
        String startTime = intent.getStringExtra(Alert.COL_FROMDATE);
        String endTime = intent.getStringExtra(Alert.COL_TODATE);

        if (matcher.matchAction(action)) {
            if (POPULATE.equals(action)) {
                Reminder.dbHelper.populate(Long.parseLong(alertId), Reminder.db);
                execute(CREATE, alertId);
            }

            if (CREATE.equals(action)) {
                execute(CREATE, alertId, alertMsgId, startTime, endTime);
            }

            if (CANCEL.equals(action)) {
                execute(CANCEL, alertId, alertMsgId, startTime, endTime);
                Reminder.dbHelper.shred(Reminder.db);
            }
        }
    }

    /*
        params: action, alarmId, alarmMsgId, startTime, endTime
     */

    private void execute(String action, String... args) {
        Intent i;
        PendingIntent pi;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Cursor c;

        String alertId = (args!=null && args.length>0) ? args[0] : null;
        String alertMsgId = (args!=null && args.length>1) ? args[1] : null;
        String startTime = (args!=null && args.length>2) ? args[2] : null;
        String endTime = (args!=null && args.length>3) ? args[3] : null;

        String status = CANCEL.equals(action) ? AlertMsg.CANCELLED : AlertMsg.ACTIVE;

        if (alertMsgId != null) {
            c = Reminder.db.query(AlertMsg.TABLE_NAME, null, AlertMsg.COL_ID+" = ?", new String[]{alertMsgId}, null, null, null);

        } else {
            c = AlertMsg.list(Reminder.db, alertId, startTime, endTime, status);
        }

        if (c.moveToFirst()) {
            long now = System.currentTimeMillis();
            long time, diff;
            do {
                i = new Intent(this, AlarmReceiver.class);
                i.putExtra(AlertMsg.COL_ID, c.getLong(c.getColumnIndex(AlertMsg.COL_ID)));
                i.putExtra(AlertMsg.COL_ALERTID, c.getLong(c.getColumnIndex(AlertMsg.COL_ALERTID)));

                pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                time = c.getLong(c.getColumnIndex(AlertMsg.COL_DATETIME));
                diff = time-now + (long)Util.MIN;
                if (CREATE.equals(action)) {
                    if (diff > 0 && diff < Util.YEAR)
                        am.set(AlarmManager.RTC_WAKEUP, time, pi);

                } else if (CANCEL.equals(action)) {
                    am.cancel(pi);
                }
            } while(c.moveToNext());
        }
        c.close();
    }
}
