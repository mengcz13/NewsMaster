package com.ihandy.a2013010952.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ihandy.a2013010952.fragment.FavoritesNewsFragment;

/**
 * Created by Mengcz on 2016/9/4.
 */
public class FavoriteFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public FavoriteFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FavoritesNewsFragment();
        FavoritesNewsFragment re = (FavoritesNewsFragment) fragment;
        re.setContext(context);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
