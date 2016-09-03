package com.ihandy.a2013010952.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ihandy.a2013010952.R;
import com.ihandy.a2013010952.activities.MainActivity;
import com.ihandy.a2013010952.adapter.NewsFragmentPagerAdapter.CatTitlePair;
import com.ihandy.a2013010952.util.LikedColumnsSingleton;
import com.ihandy.a2013010952.util.MyApplication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mengcz on 2016/9/3.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Map<String, CatTitlePair> catMap = new LinkedHashMap<>();
    private List<CatTitlePair> likeList = new ArrayList<>();
    private List<CatTitlePair> dislikeList = new ArrayList<>();

    public CategoryAdapter() {
        super();
        List<CatTitlePair> allColumns = LikedColumnsSingleton.getInstance(MyApplication.getAppContext()).getAllColumns();
        Log.d("size", "size " + allColumns.size());
        for (CatTitlePair pair : allColumns) {
            catMap.put(pair.category, pair);
            if (pair.status == CatTitlePair.LIKE)
                likeList.add(pair);
            else if (pair.status == CatTitlePair.DISLIKE)
                dislikeList.add(pair);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 0) {
            holder.categoryNameView.setText("Watched");
            holder.imageButton.setVisibility(View.INVISIBLE);
        } else if (position == 1 + likeList.size()) {
            holder.categoryNameView.setText("Unwatched");
            holder.imageButton.setVisibility(View.INVISIBLE);
        } else if (position > 0 && position < 1 + likeList.size()) {
            final int pos = position - 1;
            final CatTitlePair pair = likeList.get(pos);
            holder.categoryNameView.setText(pair.title);
            holder.imageButton.setVisibility(View.VISIBLE);
            holder.imageButton.setImageResource(R.drawable.ic_arrow_downward_24dp);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pair.status = CatTitlePair.DISLIKE;
                    likeList.remove(pos);
                    dislikeList.add(pair);
                    catMap.put(pair.category, pair);
                    Log.d("size", likeList.size() + " " + dislikeList.size() + " " + catMap.size());
                    saveModification();
                    notifyDataSetChanged();
                    notifyItemRangeChanged(0, getItemCount());
                }
            });
        } else {
            final int pos = position - 2 - likeList.size();
            final CatTitlePair pair = dislikeList.get(pos);
            holder.categoryNameView.setText(pair.title);
            holder.imageButton.setVisibility(View.VISIBLE);
            holder.imageButton.setImageResource(R.drawable.ic_arrow_upward_24dp);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pair.status = CatTitlePair.LIKE;
                    dislikeList.remove(pos);
                    likeList.add(pair);
                    catMap.put(pair.category, pair);
                    saveModification();
                    notifyDataSetChanged();
                    notifyItemRangeChanged(0, getItemCount());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return catMap.size() + 2;
    }

    public void saveModification() {
        List<CatTitlePair> list = new ArrayList<>();
        for (Map.Entry<String, CatTitlePair> entry : catMap.entrySet()) {
            list.add(entry.getValue());
        }
        LikedColumnsSingleton.getInstance(MyApplication.getAppContext()).setNewColumns(list);
        MainActivity.categoryChanged = true;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameView;
        public ImageButton imageButton;
        public View rootView;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            categoryNameView = (TextView) rootView.findViewById(R.id.category_name);
            imageButton = (ImageButton) rootView.findViewById(R.id.category_button);
        }
    }
}
