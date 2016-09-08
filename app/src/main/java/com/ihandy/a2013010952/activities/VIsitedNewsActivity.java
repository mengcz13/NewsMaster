package com.ihandy.a2013010952.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.VisitedFragmentPagerAdapter;
import com.ihandy.a2013010952.database.model.VisitedNews;

import org.litepal.crud.DataSupport;

public class VisitedNewsActivity extends AppCompatActivity {

    VisitedFragmentPagerAdapter visitedFragmentPagerAdapter;
    ViewPager vdPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.visited_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.history));

        visitedFragmentPagerAdapter = new VisitedFragmentPagerAdapter(
                getSupportFragmentManager(), this
        );
        vdPager = (ViewPager) findViewById(R.id.visited_pager);
        vdPager.setAdapter(visitedFragmentPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.visited_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ClearHistoryTask().execute();
                Snackbar.make(view, "History Clear!", Snackbar.LENGTH_LONG).show();
            }
        });
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
        visitedFragmentPagerAdapter = new VisitedFragmentPagerAdapter(
                getSupportFragmentManager(), this
        );
        vdPager = (ViewPager) findViewById(R.id.visited_pager);
        vdPager.setAdapter(visitedFragmentPagerAdapter);
    }

    class ClearHistoryTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DataSupport.deleteAll(VisitedNews.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            visitedFragmentPagerAdapter = new VisitedFragmentPagerAdapter(
                    VisitedNewsActivity.this.getSupportFragmentManager(), VisitedNewsActivity.this
            );
            vdPager = (ViewPager) findViewById(R.id.visited_pager);
            vdPager.setAdapter(visitedFragmentPagerAdapter);
        }
    }

}
