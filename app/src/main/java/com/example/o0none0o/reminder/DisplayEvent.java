package com.example.o0none0o.reminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DisplayEvent extends AppCompatActivity {

    String test;
    Event eTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        test = intent.getStringExtra(DisplayActivity.EXTRA_MESSAGE);
        eTest = getEvent(test);
        setContentView(R.layout.activity_display_event);
        setText();


    }

    public Event getEvent(String search){
        File list = new File("/data/data/com.example.o0none0o.reminder/files/Reminders.txt");
        Scanner in = null;
        Event temp = null;
        if(list.exists()) {
            try {
                in = new Scanner(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (in.hasNext()) {
                try {
                    String line = in.nextLine();
                    String[] lineSplit = line.split("~");
                    String[] searchSplit = search.split("~");
                    if(searchSplit[0].compareTo(lineSplit[0])==0){
                        temp = new Event(lineSplit[0],lineSplit[2],lineSplit[1]);
                    }
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            }
            in.close();
        }

        return temp;
    }

    public void deleteEvent(View view){
        try{
            File list = new File("/data/data/com.example.o0none0o.reminder/files/Reminders.txt");
            File temp = new File("/data/data/com.example.o0none0o.reminder/files/temp.txt");

            BufferedWriter writer = null;
            Scanner in = null;
            if(!temp.exists()) {
                temp.createNewFile();
            }
            if (list.exists()) {
                in = new Scanner(list);
                writer = new BufferedWriter(new FileWriter(temp));
                while (in.hasNext()) {
                    String line = in.nextLine();
                    String[] testLine = line.split("~");
                    String[] displayed = test.split("~");
                    if ((testLine[0].trim().compareTo(displayed[0].trim())==0)&&(testLine[1].trim().compareTo(displayed[1].trim())==0)) {
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
        finished();
    }

    public void setText(){
        TextView name = (TextView) findViewById(R.id.eventName);
        name.setText(eTest.getName());
        TextView desc = (TextView) findViewById(R.id.eventDesc);
        desc.setText(eTest.getDescription());
        TextView end = (TextView) findViewById(R.id.eventEnd);
        end.setText(eTest.getEnd().getTime().toString());
    }

    public void finished(){
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }
}
