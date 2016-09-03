package com.ihandy.a2013010952.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.CategoryAdapter;

public class CategoryManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cm_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.category_management));

        RecyclerView cmRecyclerView = (RecyclerView) findViewById(R.id.cm_recycler_view);
        cmRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        cmRecyclerView.setLayoutManager(mLayoutManager);
        CategoryAdapter cmAdapter = new CategoryAdapter();
        cmRecyclerView.setAdapter(cmAdapter);
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
}
