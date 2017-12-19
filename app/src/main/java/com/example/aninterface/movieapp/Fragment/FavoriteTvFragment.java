package com.example.aninterface.movieapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aninterface.movieapp.Adapter.FavoriteAdapter;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;

public class FavoriteTvFragment extends Fragment implements Callback{

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private LinearLayout noFavorite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_tv, container, false);
        recyclerView = view.findViewById(R.id.recyclerfavoriteTv);
        noFavorite = view.findViewById(R.id.noFavoriteTv);
        adapter = new FavoriteAdapter(getContext(), Constanc.TV_TABLE,this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        if (adapter.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            noFavorite.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(int position, View view) {
        adapter.deleteItem(position);
        if (adapter.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            noFavorite.setVisibility(View.VISIBLE);
        }
    }
}
