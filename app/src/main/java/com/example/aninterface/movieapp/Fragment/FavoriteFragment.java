package com.example.aninterface.movieapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aninterface.movieapp.Adapter.ViewPagerAdapter;
import com.example.aninterface.movieapp.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class FavoriteFragment extends Fragment {
    private SmartTabLayout mSmartTabLayout;
    private ViewPager mViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        mSmartTabLayout =  view.findViewById(R.id.tab_view_pager_fav);
        mViewPager = view.findViewById(R.id.view_pager_fav);
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        mSmartTabLayout.setViewPager(mViewPager);

        return view;
    }

}
