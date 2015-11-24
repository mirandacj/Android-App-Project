package com.thecomputors.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.thecomputors.reminder.sqldb.Alert;
import com.thecomputors.reminder.sqldb.AlertMsg;

/**
 * Created by mitchem on 11/14/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long alertMsgId = intent.getLongExtra(AlertMsg.COL_ID, -1);
        long alertId = intent.getLongExtra(AlertMsg.COL_ALERTID, -1);

        AlertMsg alertMsg = new AlertMsg(alertMsgId);
        alertMsg.setStatus(AlertMsg.EXPIRED);
        alertMsg.persist(Reminder.db);

        Alert alarm = new Alert(alertId);
        alarm.load(Reminder.db);

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
        Notification.Builder nb = new Notification.Builder(context)
                .setContentIntent(pi)
                .setContentTitle("Reminder")
                .setContentText(alarm.getName())
                .setSmallIcon(R.drawable.ic_launcher);
        Notification n = nb.build();

        if (Reminder.isVibrate()) {
            n.defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (alarm.getSound()) {
            n.sound = Uri.parse(Reminder.getRingtone());
        }
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int)alertMsgId, n);
    }

}
