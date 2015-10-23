package com.example.o0none0o.reminder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by o0None0o on 10/18/2015.
 */
public class DisplayActivity extends ActionBarActivity {
    private String[] viewOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String mTitle="";
    private CharSequence mDrawerTitle;
   // private CharSequence mTitle;
private ActionBarDrawerToggle drawerListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);

        mTitle = (String) getTitle();


        viewOptions = getResources().getStringArray(R.array.menus);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        drawerListener= new ActionBarDrawerToggle(this,mDrawerLayout, R.string.drawer_open,R.string.drawer_closed) {
            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(DisplayActivity.this," Drawer Opened ", Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Select view");
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(drawerListener);
        ArrayAdapter<String>adapter= new ArrayAdapter<String>(getBaseContext(),R.layout.menu1_layout,getResources().getStringArray(R.array.menus));
        mDrawerList.setAdapter(adapter);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] menus = getResources().getStringArray(R.array.menus);
                mTitle= menus[position];
                MenuFragment mFragment = new MenuFragment();
                Bundle data = new Bundle();
                data.putInt("position", position);
                mFragment.setArguments(data);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, mFragment);
                ft.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}




