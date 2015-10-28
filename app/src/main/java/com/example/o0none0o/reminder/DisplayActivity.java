package com.example.o0none0o.reminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by o0None0o on 10/18/2015.
 */
public class DisplayActivity extends ActionBarActivity {

    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String mTitle="";
    private ArrayList<Event> events;
    private FragmentManager fragmentManager;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);
        mTitle = (String) getTitle();
        try {
            events = getList();
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        intent = new Intent(this, DisplayEvent.class);
        if(events!=null) {
            int count = 0;
            for (int i = 0; i < events.size(); i++) {
                count++;
            }
            Event [] eventList = new Event[count];
            for (int j = 0; j < eventList.length;) {
                eventList[j] = events.get(j);
                j++;
            }


            ListAdapter adapt = new CustomAdapter(this, eventList);
            ListView listView = (ListView) findViewById(R.id.listView3);
            listView.setAdapter(adapt);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Object o = arg0.getItemAtPosition(position);
                    Event test = (Event) o;
                    String message = test.getName() + "~ " + test.getDescription();
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                }
            });
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
         ArrayList<String> menusArray = new ArrayList<String>();
        menusArray.add("Yearly");
        menusArray.add("Monthly");
        menusArray.add("Daily");
        ArrayAdapter<String>adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menusArray);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadSelection(position);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        mDrawerToggle= new ActionBarDrawerToggle(this,mDrawerLayout, R.string.drawer_open,R.string.drawer_closed) ;

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //ArrayAdapter<String>adapter= new ArrayAdapter<String>(getBaseContext(),R.layout.drawer_list_item,getResources().getStringArray(R.array.menus));
        ActionBar actionBar= getSupportActionBar();
        // mDrawerList.setAdapter(adapter);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        fragmentManager= getSupportFragmentManager();
        loadSelection(0);

    }

    public ArrayList<Event> getList(){
        ArrayList<Event> events = new ArrayList<>();
        File list = new File("/data/data/com.example.o0none0o.reminder/files/Reminders.txt");
        Scanner in = null;
        if(list.exists()){
            try {
                in = new Scanner(list);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            while(in.hasNext()){
                try {
                    String line = in.nextLine();
                    String[] lineSplit = line.split("~");
                    Event temp = new Event(lineSplit[0], lineSplit[2], lineSplit[1]);
                    events.add(temp);
                }
                catch(NoSuchElementException e){
                    e.printStackTrace();
                }
            }
            in.close();
        }
        else{
            Event sample = new Event();
            events.add(sample);
        }

        return events;
    }



private void loadSelection(int i ){
    mDrawerList.setItemChecked(i, true);
    switch (i){
        case 0:
            menu1_Fragment f1 = new menu1_Fragment();

            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, f1);
            fragmentTransaction.commit();
            break;
        case 1:
        menu2_Fragment f2 = new menu2_Fragment();
            fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, f2);
        fragmentTransaction.commit();
            break;
        case 2:
            menu3_Fragment f3 = new menu3_Fragment();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, f3);
            fragmentTransaction.commit();
            break;
    }
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_settings)
        {
            return true;
        }
        else if (id== android.R.id.home)
        {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)){
                mDrawerLayout.closeDrawer(mDrawerList);
            }else{
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


}




