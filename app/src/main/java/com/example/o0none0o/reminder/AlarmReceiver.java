package com.example.o0none0o.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

/**
 * Created by o0None0o on 10/21/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
AlarmReceiver context = AlarmReceiver.this;
    private NotificationManager noti;

    @Override
    ///push notification create
    public void onReceive(final Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Event Notification");
        builder.setContentText("This is a reminder");
        Intent i = new Intent(context, MainMenu.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(i);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());


    }


}