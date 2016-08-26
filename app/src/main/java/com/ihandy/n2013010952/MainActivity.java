package com.ihandy.n2013010952;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
        ViewPager mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(newsFragmentStatePagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
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

//    public static class DemoObjectFragment extends Fragment {
//        public static final String ARG_OBJECT = "object";
//
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 ViewGroup container, Bundle savedInstanceState) {
//            // The last two arguments ensure LayoutParams are inflated
//            // properly.
//            View rootView = inflater.inflate(
//                    R.layout.single_column_main, container, false);
//            Bundle args = getArguments();
//            int repeatTime = args.getInt(ARG_OBJECT);
//
//            String[] data = {
//                    "Mon 6/23 - Sunny - 31/17",
//                    "Tue 6/24 - Foggy - 21/8",
//                    "Wed 6/25 - Cloudy - 22/17",
//                    "Thurs 6/26 - Rainy - 18/11",
//                    "Fri 6/27 - Foggy - 21/10",
//                    "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
//                    "Sun 6/29 - Sunny - 20/7"
//            };
//            List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));
//            for (int i = 0; i < repeatTime - 1; ++i) {
//                weekForecast.addAll(Arrays.asList(data));
//            }
//            ArrayAdapter<String> mForecastAdapter =
//                    new ArrayAdapter<String>(
//                            getActivity(), // The current context (this activity)
//                            R.layout.list_item_news, // The name of the layout ID.
//                            R.id.news_item, // The ID of the textview to populate.
//                            weekForecast);
//            ListView listView = (ListView)rootView.findViewById(R.id.listview);
//            listView.setAdapter(mForecastAdapter);
//
//            return rootView;
//        }
//    }
//
//    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
//        private int count = 5;
//        public DemoCollectionPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            Fragment fragment = new DemoObjectFragment();
//            Bundle args = new Bundle();
//            // Our object is just an integer :-P
//            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return count;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "OBJECT " + (position + 1);
//        }
//
//        public void setCount(int newCount) {
//            count = newCount;
//            notifyDataSetChanged();
//        }
//    }

    public static class NewsObjectFragment extends Fragment {
        public static final String ARG_TITLE = "title";
        public static final String ARG_CTGY = "category";
        private CharSequence title;
        private CharSequence ctgy;
        private RecyclerView mRecyclerView;
        private NewsAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private SwipeRefreshLayout mSwipeRefreshLayout;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
            View rootView = inflater.inflate(
                    R.layout.single_column_main, container, false);
            Bundle args = getArguments();
            title = args.getCharSequence(ARG_TITLE);
            ctgy = args.getCharSequence(ARG_CTGY);

            mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.layout_swipe_refresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshNews();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new NewsAdapter(new JSONArray());
            mRecyclerView.setAdapter(mAdapter);

            refreshNews();

            return rootView;
        }

        private void refreshNews() {
            String url = getResources().getString(R.string.news_query_url) + "&category=" + ctgy.toString().replaceAll("\\s*", "");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray newsArray = response.getJSONObject("data").getJSONArray("news");
                        mAdapter.setNewsArray(newsArray);
                    }
                    catch (org.json.JSONException e) {}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestSingleton.getInstance(MyApplication.getAppContext()).getRequestQueue().add(jsObjRequest);
        }

        public static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
            private JSONArray newsArray;

            public static class ViewHolder extends RecyclerView.ViewHolder {
                public TextView titleTextView;
                public TextView sourceNameTextView;
                public ViewHolder(View rootview) {
                    super(rootview);
                    titleTextView = (TextView) rootview.findViewById(R.id.title_textview);
                    sourceNameTextView = (TextView) rootview.findViewById(R.id.source_textview);
                }
            }

            public NewsAdapter(JSONArray jsonArray) {
                setNewsArray(jsonArray);
            }

            @Override
            public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_news, parent, false);
                ViewHolder vh = new ViewHolder(v);
                return vh;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                try {
                    JSONObject news = newsArray.getJSONObject(position);
                    String title = news.getString("title");
                    String source = news.getJSONObject("source").getString("name");
                    holder.titleTextView.setText(title);
                    holder.sourceNameTextView.setText(source);
                }
                catch (org.json.JSONException e) {}
            }

            @Override
            public int getItemCount() {
                return newsArray.length();
            }

            public void setNewsArray(JSONArray jsonArray) {
                newsArray = jsonArray;
                notifyDataSetChanged();
            }
        }
    }

    public class NewsFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public NewsFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
            refreshCategory();
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new NewsObjectFragment();
            Bundle args = new Bundle();
            CatTitlePair pair = catTitlePairs.get(i);
            args.putCharSequence(NewsObjectFragment.ARG_TITLE, pair.title);
            args.putCharSequence(NewsObjectFragment.ARG_CTGY, pair.category);
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
                        for (Iterator<String> it = cats.keys(); it.hasNext();) {
                            String key = it.next();
                            pairs.add(new CatTitlePair(key, cats.getString(key)));
                        }
                        setCatTitlePairs(pairs);
                    }
                    catch (org.json.JSONException e) {

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
