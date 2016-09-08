package com.ihandy.a2013010952.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.FavoriteFragmentPagerAdapter;

public class FavoritesActivity extends AppCompatActivity {

    FavoriteFragmentPagerAdapter favoriteFragmentPagerAdapter;
    ViewPager fvPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fv_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.favorites));

        favoriteFragmentPagerAdapter = new FavoriteFragmentPagerAdapter(
                getSupportFragmentManager(), this
        );
        fvPager = (ViewPager) findViewById(R.id.fv_pager);
        fvPager.setAdapter(favoriteFragmentPagerAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteFragmentPagerAdapter = new FavoriteFragmentPagerAdapter(
                getSupportFragmentManager(), this
        );
        fvPager = (ViewPager) findViewById(R.id.fv_pager);
        fvPager.setAdapter(favoriteFragmentPagerAdapter);
    }
}
