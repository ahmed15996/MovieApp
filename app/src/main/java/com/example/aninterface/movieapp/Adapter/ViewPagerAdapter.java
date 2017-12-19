package com.example.aninterface.movieapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.aninterface.movieapp.Fragment.FavoriteMovieFragment;
import com.example.aninterface.movieapp.Fragment.FavoriteTvFragment;
import com.example.aninterface.movieapp.Utils.Constanc;

/**
 * Created by interface on 18/12/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FavoriteMovieFragment();
            case 1:
                return new FavoriteTvFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 : return "Movies";
            case 1 : return  "TV";
        }
        return super.getPageTitle(position);
    }
}