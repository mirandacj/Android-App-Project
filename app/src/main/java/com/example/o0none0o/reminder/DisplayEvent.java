package com.example.o0none0o.reminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        test = intent.getStringExtra(DisplayActivity.EXTRA_MESSAGE);
        setContentView(R.layout.activity_display_event);
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
                    Toast.makeText(DisplayEvent.this, test, Toast.LENGTH_SHORT).show();
                    String[] testLine = line.split("~");
                    String[] displayed = test.split("~");
                    if (testLine[0].compareTo(displayed[0])==0) {
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

    public void finished(){
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }
}
