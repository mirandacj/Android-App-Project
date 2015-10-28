package com.example.o0none0o.reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by o0None0o on 10/24/2015.
 */
public class AlertAlarm extends Activity {
    Context context = AlertAlarm.this;
    @Override
////pop up dialog create
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_noti);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("NOTIFICATION");
        alertDialogBuilder.setMessage("This is an alarm");
        alertDialogBuilder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cleanList();
                Intent i = new Intent(context, DisplayActivity.class);
                startActivity(i);
            }
        });
        alertDialogBuilder.setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setSnooze(30000);
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void setSnooze(int duration){
        final Intent  intent = new Intent(context, AlertAlarm.class);

        int currentTimeMillis = (int) System.currentTimeMillis();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run()

            {
                startActivity(intent);
            }
        }, currentTimeMillis + duration);

    }

    public void cleanList(){
        try{
            File list = new File("/data/data/com.example.o0none0o.reminder/files/Reminders.txt");
            File temp = new File("/data/data/com.example.o0none0o.reminder/files/temp.txt");
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            BufferedWriter writer = null;
            Calendar testTime = Calendar.getInstance();
            Calendar testC = Calendar.getInstance();
            Scanner in = null;
            if(!temp.exists()) {
                temp.createNewFile();
            }
            if (list.exists()) {
                in = new Scanner(list);
                writer = new BufferedWriter(new FileWriter(temp));
                while (in.hasNext()) {
                    String line = in.nextLine();
                    String[] testL = line.split("~");
                    testC.setTime(format.parse(testL[2]));
                    if (testC.compareTo(testTime)==-1) {
                        continue;
                    }
                    else {
                        writer.write(line);
                    }
                }
                writer.close();
                in.close();
                temp.renameTo(list);

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
}
