package com.example.o0none0o.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);
    }
    public void switchActivity(View view)
    {
        
        String button_text;
        button_text=((Button) view).getText().toString();
        if (button_text.equals("Display"))
        {
            Intent intent = new Intent (this, MainActivity.class);
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

