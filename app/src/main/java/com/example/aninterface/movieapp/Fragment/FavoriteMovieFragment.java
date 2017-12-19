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
import com.example.aninterface.movieapp.Utils.Database;


public class FavoriteMovieFragment extends Fragment implements Callback{

    private RecyclerView recyclerView;
    private LinearLayout noFavorite;
    private FavoriteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        recyclerView = view.findViewById(R.id.recyclerfavoriteMovie);
        noFavorite = view.findViewById(R.id.noFavoriteMovie);
        adapter = new FavoriteAdapter(getContext(), Constanc.MOVIE_TABLE,this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        if (adapter.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            noFavorite.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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
