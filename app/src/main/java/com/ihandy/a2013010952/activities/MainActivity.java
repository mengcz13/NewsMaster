package com.ihandy.a2013010952.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.NewsFragmentPagerAdapter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean categoryChanged = false;
    private MaterialViewPager mViewPager;
    private NewsFragmentPagerAdapter newsFragmentStatePagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getResources().getText(R.string.app_name));

        newsFragmentStatePagerAdapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager(), this
        );
        mViewPager = (MaterialViewPager) findViewById(R.id.pager);

        Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(" ");
        }

        mViewPager.getViewPager().setAdapter(newsFragmentStatePagerAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
//        tabLayout = (TabLayout) findViewById(R.id.tab);
//        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page % 4) {
//                    case 0:
//                        return HeaderDesign.fromColorResAndDrawable(
//                                R.color.green,
//                                getResources().getDrawable(R.drawable.greenbgimg));
//                    case 0:
//                        return HeaderDesign.fromColorResAndDrawable(
//                                R.color.blue,
//                                getResources().getDrawable(R.drawable.bluebgimg));
//                    case 1:
//                        return HeaderDesign.fromColorResAndDrawable(
//                                R.color.cyan,
//                                getResources().getDrawable(R.drawable.cyanbgimg));
//                    case 2:
//                        return HeaderDesign.fromColorResAndDrawable(
//                                R.color.red,
//                                getResources().getDrawable(R.drawable.redbgimg));
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }
                return null;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (categoryChanged) {
            newsFragmentStatePagerAdapter = new NewsFragmentPagerAdapter(
                    getSupportFragmentManager(), this
            );
//            mViewPager.setAdapter(newsFragmentStatePagerAdapter);
//            tabLayout.setupWithViewPager(mViewPager);
            mViewPager.getViewPager().setAdapter(newsFragmentStatePagerAdapter);
            mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
            categoryChanged = false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_favorites:
                this.startActivity(new Intent(this, FavoritesActivity.class));
                break;
            case R.id.nav_ctgmng:
                this.startActivity(new Intent(this, CategoryManageActivity.class));
                break;
            case R.id.nav_history:
                this.startActivity(new Intent(this, VisitedNewsActivity.class));
                break;
            case R.id.nav_aboutme:
                this.startActivity(new Intent(this, AboutMeActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
