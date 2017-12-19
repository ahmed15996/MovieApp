package com.example.aninterface.movieapp.Fragment;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.aninterface.movieapp.Activity.ViewAllActivity;
import com.example.aninterface.movieapp.Adapter.MoviesAdapter;
import com.example.aninterface.movieapp.Model.MovieModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.EndlessRecyclerViewScrollListener;
import com.example.aninterface.movieapp.Utils.InternetReceiver;
import com.example.aninterface.movieapp.Utils.Network;
import com.example.aninterface.movieapp.WebServices.MovieClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment implements View.OnClickListener {

    private RecyclerView
            nowPlaying_recycler,
            topRated_recycler,
            popular_recycler,
            upComing_recycler;

    private LinearLayoutManager
            nowPalying_layoutManager,
            topRated_layoutManager,
            popular_layoutManager,
            upComing_layoutManagr;

    private TextView
            viewAll_nowPlaying,
            viewAll_topRated,
            viewAll_popular,
            viewAll_upComing;

    private MoviesAdapter
            nowPlaying_adapter,
            topRated_adapter,
            popularAdapter,
            upComingAdapter;

    private Call<MovieModel>
            nowPlayingCall,
            topRatedCall,
            popularCall,
            upComingCall;

    private boolean
            isNowPlayingLoaded,
            isTopRatedLoded,
            isPopularLoaded,
            isUpcomingLoaded;

    private boolean
            isNowPlayingScrolled,
            isTopRatedScrolled,
            isPopularScrolled,
            isUpcomingScrolled;

    private ProgressBar progressBar;
    private ScrollView scrollView;
    private LinearLayout noInternet;

    private InternetReceiver receiver;
    private boolean isOpenReceiver;
    private boolean isDataLoaded;

    private int
            nowPlaying_currentPage = 1,
            topRated_currentPage = 1,
            upComing_currentPage = 1,
            popular_currentPage = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_movie, container, false);

        scrollView = v.findViewById(R.id.content_movies);
        noInternet = v.findViewById(R.id.no_internet_movie);
        progressBar = v.findViewById(R.id.progressbar_movies);
        progressBar.setVisibility(View.VISIBLE);

        isNowPlayingLoaded = false;
        isTopRatedLoded = false;
        isPopularLoaded = false;
        isUpcomingLoaded = false;

        nowPlaying_recycler = v.findViewById(R.id.recyclerView_nowPlaying_movies);
        topRated_recycler = v.findViewById(R.id.recyclerView_topRated_movies);
        popular_recycler = v.findViewById(R.id.recyclerView_popular_movies);
        upComing_recycler = v.findViewById(R.id.recyclerView_upComing_movies);

        nowPalying_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        topRated_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        popular_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        upComing_layoutManagr = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        viewAll_nowPlaying = v.findViewById(R.id.viewAll_nowPlaying_movies);
        viewAll_topRated = v.findViewById(R.id.viewAll_topRated_movies);
        viewAll_popular = v.findViewById(R.id.viewAll_popular_movies);
        viewAll_upComing = v.findViewById(R.id.viewAll_upComing_movies);

        nowPlaying_recycler.setLayoutManager(nowPalying_layoutManager);
        topRated_recycler.setLayoutManager(topRated_layoutManager);
        popular_recycler.setLayoutManager(popular_layoutManager);
        upComing_recycler.setLayoutManager(upComing_layoutManagr);

        nowPlaying_recycler.setHasFixedSize(true);
        topRated_recycler.setHasFixedSize(true);
        popular_recycler.setHasFixedSize(true);
        upComing_recycler.setHasFixedSize(true);
        nowPlaying_adapter = new MoviesAdapter(getActivity());
        topRated_adapter = new MoviesAdapter(getActivity());
        popularAdapter = new MoviesAdapter(getActivity());
        upComingAdapter = new MoviesAdapter(getActivity());

        nowPlaying_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(nowPalying_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getContext())) {
                    Network.snakBar(nowPlaying_recycler);
                    isNowPlayingScrolled = false;
                    if (!isOpenReceiver){
                        loadMore();
                    }
                } else {
                    load_nowPlaying_movies();
                    nowPlaying_currentPage++;
                    isNowPlayingScrolled = true;
                }

            }
        });
        topRated_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(topRated_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    Network.snakBar(nowPlaying_recycler);
                    isTopRatedScrolled = false;
                    if (!isOpenReceiver){
                        loadMore();
                    }
                } else {
                    load_topRated_movies();
                    topRated_currentPage++;
                    isTopRatedScrolled = true;
                }


            }
        });
        popular_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(topRated_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    Network.snakBar(nowPlaying_recycler);
                    isPopularScrolled = false;
                    if (!isOpenReceiver){
                        loadMore();
                    }
                } else {
                    load_popular_movies();
                    popular_currentPage++;
                    isPopularScrolled = true;
                }

            }
        });
        upComing_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(topRated_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    Network.snakBar(nowPlaying_recycler);
                    isUpcomingScrolled = false;
                    if (!isOpenReceiver){
                        loadMore();
                    }
                } else {
                    load_upComing_movies();
                    upComing_currentPage++;
                    isUpcomingScrolled = true;
                }
            }
        });

        viewAll_nowPlaying.setOnClickListener(this);
        viewAll_topRated.setOnClickListener(this);
        viewAll_popular.setOnClickListener(this);
        viewAll_upComing.setOnClickListener(this);

        nowPlaying_recycler.setAdapter(nowPlaying_adapter);
        topRated_recycler.setAdapter(topRated_adapter);
        popular_recycler.setAdapter(popularAdapter);
        upComing_recycler.setAdapter(upComingAdapter);

        if (Network.isNetworkConnection(getActivity())) {
            load_nowPlaying_movies();
            load_topRated_movies();
            load_popular_movies();
            load_upComing_movies();
            isDataLoaded = true;
        }else {
            progressBar.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!Network.isNetworkConnection(getActivity()) && !isDataLoaded) {
            progressBar.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
            receiver = new InternetReceiver(new InternetReceiver.InternetListener() {
                @Override
                public void networkConnected() {
                    progressBar.setVisibility(View.VISIBLE);
                    noInternet.setVisibility(View.GONE);

                    load_nowPlaying_movies();
                    load_topRated_movies();
                    load_popular_movies();
                    load_upComing_movies();

                    isDataLoaded = true;
                    isOpenReceiver = false;
                    getActivity().unregisterReceiver(receiver);
                }
            });
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            getActivity().registerReceiver(receiver, filter);
            isOpenReceiver = true;
        }else if (Network.isNetworkConnection(getActivity()) && !isDataLoaded) {
            progressBar.setVisibility(View.VISIBLE);

            load_nowPlaying_movies();
            load_topRated_movies();
            load_popular_movies();
            load_upComing_movies();

            isDataLoaded = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        nowPlaying_adapter.notifyDataSetChanged();
        topRated_adapter.notifyDataSetChanged();
        popularAdapter.notifyDataSetChanged();
        upComingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isOpenReceiver) {
            getActivity().unregisterReceiver(receiver);
            isOpenReceiver = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (nowPlayingCall != null) nowPlayingCall.cancel();
        if (upComingCall != null) upComingCall.cancel();
        if (popularCall != null) popularCall.cancel();
        if (topRatedCall != null) topRatedCall.cancel();
    }


    private void load_nowPlaying_movies() {
        nowPlayingCall = MovieClient.nowPlaying().getMovies(Constanc.NOW_PLAYING_PATH, nowPlaying_currentPage);
        nowPlayingCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;
                loadNextPage(response.body().getResults(), 1);
                isNowPlayingLoaded = true;
                allDataLoaded();


            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    private void load_topRated_movies() {
        topRatedCall = MovieClient.movieTopRated().getMovies(Constanc.TOP_RATED_PATH, topRated_currentPage);
        topRatedCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;

                loadNextPage(response.body().getResults(), 2);
                isTopRatedLoded = true;
                allDataLoaded();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    private void load_popular_movies() {
        popularCall = MovieClient.moviePopular().getMovies(Constanc.POPULAR_PATH, popular_currentPage);
        popularCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;

                loadNextPage(response.body().getResults(), 3);
                isPopularLoaded = true;
                allDataLoaded();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    private void load_upComing_movies() {
        upComingCall = MovieClient.upComing().getMovies(Constanc.UPCOMING_PATH, upComing_currentPage);
        upComingCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;

                loadNextPage(response.body().getResults(), 4);
                isUpcomingLoaded = true;
                allDataLoaded();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });


    }

    private void allDataLoaded() {
        if (isNowPlayingLoaded && isTopRatedLoded && isPopularLoaded && isUpcomingLoaded) {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void loadNextPage(List<MovieModel.ResultMovie> addList, int adapterType) {
        if (adapterType == 1) {
            if (nowPlaying_currentPage != 1) {
                nowPlaying_adapter.removeFooter();
            }
            nowPlaying_adapter.addList(addList);
            nowPlaying_adapter.addLoadingFooter();
        } else if (adapterType == 2) {
            if (topRated_currentPage != 1) {
                topRated_adapter.removeFooter();
            }
            topRated_adapter.addList(addList);
            topRated_adapter.addLoadingFooter();
        } else if (adapterType == 3) {
            if (popular_currentPage != 1) {
                popularAdapter.removeFooter();
            }
            popularAdapter.addList(addList);
            popularAdapter.addLoadingFooter();

        } else if (adapterType == 4) {
            if (upComing_currentPage != 1) {
                upComingAdapter.removeFooter();
            }
            upComingAdapter.addList(addList);
            upComingAdapter.addLoadingFooter();
        }
    }

    private void loadMore(){
        receiver = new InternetReceiver(new InternetReceiver.InternetListener() {
            @Override
            public void networkConnected() {
                if (!isNowPlayingScrolled) {
                    load_nowPlaying_movies();
                    nowPlaying_currentPage++;
                    isNowPlayingScrolled = true;
                }
                if (!isPopularScrolled) {
                    load_popular_movies();
                    popular_currentPage++;
                    isPopularScrolled = true;

                }
                if (!isTopRatedScrolled) {

                    load_topRated_movies();
                    topRated_currentPage++;
                    isTopRatedScrolled = true;
                }
                if (!isUpcomingScrolled) {
                    load_upComing_movies();
                    upComing_currentPage++;
                    isUpcomingScrolled = true;
                }
                isOpenReceiver = false;
                getActivity().unregisterReceiver(receiver);
            }
        });
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(receiver, filter);
        isOpenReceiver = true;
    }

    @Override
    public void onClick(View view) {
        if (!Network.isNetworkConnection(getActivity())){
            Network.snakBar(view);
            return;
        }
        Intent intent = new Intent(getActivity(), ViewAllActivity.class);
        switch (view.getId()) {
            case R.id.viewAll_nowPlaying_movies:
                intent.putExtra(Constanc.TYPE_MOVIE, Constanc.NOW_PLAYING);
                break;
            case R.id.viewAll_topRated_movies:
                intent.putExtra(Constanc.TYPE_MOVIE, Constanc.TOP_RATED);
                break;
            case R.id.viewAll_popular_movies:
                intent.putExtra(Constanc.TYPE_MOVIE, Constanc.POPULAR);
                break;
            case R.id.viewAll_upComing_movies:
                intent.putExtra(Constanc.TYPE_MOVIE, Constanc.UPCOMING);
                break;
        }
        getActivity().startActivity(intent);

    }


}
