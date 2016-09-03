package com.ihandy.a2013010952.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.adapter.NewsAdapter;

import org.json.JSONArray;

/**
 * Created by Mengcz on 2016/9/1.
 */
abstract public class NewsFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected NewsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View rootView = inflater.inflate(
                R.layout.single_column_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(context, new JSONArray());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreNews();
            }
        });

        refreshNews();

        return rootView;
    }

    abstract public void refreshNews();

    abstract public void loadMoreNews();

    public void setContext(Context context) {
        this.context = context;
    }

    public abstract class EndlessRecyclerOnScrollListener extends
            RecyclerView.OnScrollListener {

        int firstVisibleItem, visibleItemCount, totalItemCount;
        private int previousTotal = 0;
        private boolean loading = true;
        private int currentPage = 1;

        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerOnScrollListener(
                LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//
//            visibleItemCount = recyclerView.getChildCount();
//            totalItemCount = mLinearLayoutManager.getItemCount();
//            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
//
//            if (loading) {
//                if (totalItemCount > previousTotal) {
//                    loading = false;
//                    previousTotal = totalItemCount;
//                }
//            }
//            if (!loading
//                    && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
//                currentPage++;
//                onLoadMore(currentPage);
//                loading = true;
//                Log.d("scroll", totalItemCount + " " + visibleItemCount + " " + firstVisibleItem);
//            }
//        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 1) {
                    onLoadMore(currentPage);
                }
            }
        }

        public abstract void onLoadMore(int currentPage);
    }

}