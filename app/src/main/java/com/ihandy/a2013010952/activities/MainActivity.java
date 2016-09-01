package com.ihandy.a2013010952.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.fragment.RefreshableNewsFragment;
import com.ihandy.a2013010952.itemlistener.ItemOnClickListener;
import com.ihandy.a2013010952.util.MyApplication;
import com.ihandy.a2013010952.util.RequestSingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.app_name));

        NewsFragmentStatePagerAdapter newsFragmentStatePagerAdapter = new NewsFragmentStatePagerAdapter(
                getSupportFragmentManager()
        );
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(newsFragmentStatePagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                break;
            case R.id.nav_ctgmng:
                break;
            case R.id.nav_aboutme:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class NewsFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public NewsFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
            refreshCategory();
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new RefreshableNewsFragment();
            RefreshableNewsFragment re = (RefreshableNewsFragment)fragment;
            re.setContext(MainActivity.this);
            Bundle args = new Bundle();
            CatTitlePair pair = catTitlePairs.get(i);
            args.putCharSequence(RefreshableNewsFragment.ARG_TITLE, pair.title);
            args.putCharSequence(RefreshableNewsFragment.ARG_CTGY, pair.category);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return catTitlePairs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catTitlePairs.get(position).title;
        }

        private class CatTitlePair {
            public String category;
            public String title;

            public CatTitlePair(String category, String title) {
                this.category = category;
                this.title = title;
            }
        }

        private List<CatTitlePair> catTitlePairs = new ArrayList<>();

        private void setCatTitlePairs(List<CatTitlePair> list) {
            catTitlePairs = list;
            notifyDataSetChanged();
        }

        private void refreshCategory() {
            String url = getResources().getString(R.string.category_url) + "?timestamp=" + System.currentTimeMillis();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONObject cats;
                    try {
                        cats = response.getJSONObject("data").getJSONObject("categories");
                        List<CatTitlePair> pairs = new ArrayList<>();
                        for (Iterator<String> it = cats.keys(); it.hasNext(); ) {
                            String key = it.next();
                            pairs.add(new CatTitlePair(key, cats.getString(key)));
                        }
                        setCatTitlePairs(pairs);
                    } catch (org.json.JSONException e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
        }
    }

}
