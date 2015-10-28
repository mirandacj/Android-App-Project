package com.example.o0none0o.reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainMenu extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);
        checkFirst();
    }

    private void checkFirst(){
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("first", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            FileOutputStream out = null;
            StringBuilder sb = new StringBuilder();
            try {
                out = openFileOutput("Reminders.txt", 0);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                sb.append("Example");
                sb.append("~ ");
                sb.append("Sample description");
                sb.append("~ ");
                sb.append("10-10-2015 12:00:00");
                sb.append(System.getProperty("line.separator"));
                out.write(sb.toString().getBytes());
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("first", false).commit();
        }
    }

    public void switchActivity(View view)
    {
        
        String button_text;
        button_text=((Button) view).getText().toString();
        if (button_text.equals("Display"))
        {
            Intent intent = new Intent (this, DisplayActivity.class);
            startActivity(intent);
        }
        else if (button_text.equals("Add event"))
        {
            Intent intent = new Intent (this, CreateEvent.class);
            startActivity(intent);
        }
        else if (button_text.equals("Personalize"))
        {
            Intent intent = new Intent (this, PersonalizeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }

