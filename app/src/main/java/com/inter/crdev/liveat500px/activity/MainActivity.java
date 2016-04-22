package com.inter.crdev.liveat500px.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.inter.crdev.liveat500px.R;
import com.inter.crdev.liveat500px.dao.PhotoItemDao;
import com.inter.crdev.liveat500px.fragment.MainFragment;

public class MainActivity extends AppCompatActivity
        implements MainFragment.FragmentListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())             //แปะ fragment
                    .commit();
        }
    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolBar);         // set toolbar
        setSupportActionBar(toolbar);                           // set toolbar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer

        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    ////////////////////////////////////////////////////////////
    // สร้าง ปุ่ม hamberger
    ///////////////////////////////////////////////////////////

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    ////////////////////////////////////////////////////////////
    // End
    ///////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPhotoItemClicked(PhotoItemDao dao) {
        //ทำใน mobile
        Intent intent = new Intent(MainActivity.this,
                MoreInfoActivity.class);
        intent.putExtra("dao",dao);
        startActivity(intent);
        ///

    }
}
