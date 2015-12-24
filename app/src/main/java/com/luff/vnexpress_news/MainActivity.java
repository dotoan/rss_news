package com.luff.vnexpress_news;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.luff.vnexpress_news.adapters.SlidingAdapter;
import com.luff.vnexpress_news.fragments.BussinessFragment;
import com.luff.vnexpress_news.fragments.DetailFragment;
import com.luff.vnexpress_news.fragments.HomeFragment;
import com.luff.vnexpress_news.fragments.NewsFragment;
import com.luff.vnexpress_news.fragments.SportFragment;
import com.luff.vnexpress_news.fragments.WorldFragment;
import com.luff.vnexpress_news.models.SlidingMenuItem;
import com.luff.vnexpress_news.ultils.Variables;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView lvSlidingMenu;
    private ActionBarDrawerToggle drawerToggle;

    // slide menu item
    private String[]titles;
    private TypedArray icons;

    // navigation drawer titles
    private CharSequence drawerTitle;
    private CharSequence appTitle;

    private ArrayList<SlidingMenuItem> slidingMenuItems;
    private SlidingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appTitle = drawerTitle = getTitle();
        // load resource
        titles = getResources().getStringArray(R.array.nav_drawer_title);
        icons = getResources().obtainTypedArray(R.array.nav_drawer_icon);
        // get sliding menu listview
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvSlidingMenu = (ListView) findViewById(R.id.lv_sliding_menu);

        slidingMenuItems = new ArrayList<SlidingMenuItem>();
        slidingMenuItems.add(new SlidingMenuItem(icons.getResourceId(0,-1),titles[0]));
        slidingMenuItems.add(new SlidingMenuItem(icons.getResourceId(1,-1),titles[1]));
        slidingMenuItems.add(new SlidingMenuItem(icons.getResourceId(2,-1),titles[2]));
        slidingMenuItems.add(new SlidingMenuItem(icons.getResourceId(3,-1),titles[3]));
        slidingMenuItems.add(new SlidingMenuItem(icons.getResourceId(4, -1), titles[4]));

        icons.recycle();
        lvSlidingMenu.setOnItemClickListener(new SlileMenuClickListener());
        adapter = new SlidingAdapter(this,slidingMenuItems);
        lvSlidingMenu.setAdapter(adapter);

        // enable action bar app icon and behavior , it as toggle buttons
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name )
        {
            public void onDrawerClosed(View view){
                getActionBar().setTitle(appTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View view){
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        if(savedInstanceState == null){
            displayView(0);
        }

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private class SlileMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            displayView(position);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        appTitle = title;
        getActionBar().setTitle(appTitle);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                fragment = new DetailFragment();
                Bundle objectId = new Bundle();
                objectId.putInt(Variables.KEYPAGE, position);
                fragment.setArguments(objectId);
            default:
                break;
        }
        if(fragment != null ){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_detail,fragment).commit();
            // update selected item and title, then close the drawer
            lvSlidingMenu.setItemChecked(position,true);
            lvSlidingMenu.setSelection(position);
            setTitle(titles[position]);
            drawerLayout.closeDrawer(lvSlidingMenu);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(lvSlidingMenu);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}
