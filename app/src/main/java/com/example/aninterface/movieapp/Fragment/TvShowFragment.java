package com.example.aninterface.movieapp.Fragment;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.example.aninterface.movieapp.Adapter.TvAdapter;
import com.example.aninterface.movieapp.Model.TvModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.EndlessRecyclerViewScrollListener;
import com.example.aninterface.movieapp.Utils.InternetReceiver;
import com.example.aninterface.movieapp.Utils.Network;
import com.example.aninterface.movieapp.WebServices.TvClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class TvShowFragment extends Fragment {
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private LinearLayout noInternet;

    private TextView
            airingToDay_viewAll,
            onTheAir_viewAll,
            popular_viewAll,
            toprated_viewAll;

    private RecyclerView
            airingToDay_recycler,
            onTheAir_recycler,
            popular_recycler,
            toprated_recycler;

    private LinearLayoutManager
            airingToDay_layoutManager,
            onTheAir_layoutManager,
            popular_layoutManager,
            toprated_layoutManager;

    private TvAdapter
            airingToDay_adapter,
            onTheAir_adapter,
            popular_adapter,
            toprated_adapter;

    private Call<TvModel>
            airingToDay_call,
            onTheAir_call,
            popular_call,
            toprated_call;

    private int
            airingToDay_currentPage = 1,
            onTheAir_currentPage = 1,
            popular_currentPage = 1,
            topRated_currentPage = 1;

    private boolean
            isAiringToday_loaded = false,
            isOnTheAir_loaded = false,
            isPopular_loaded = false,
            isToprated_loaded = false;

    private boolean
            isAiringTodayScrolled,
            isOnTheAirScrolled,
            isPopularScrolled,
            isTopratedScrolled;

    private InternetReceiver receiver;
    private boolean isDataLoaded;
    private boolean isOpenReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        scrollView = view.findViewById(R.id.content_tv);
        progressBar = view.findViewById(R.id.progressbar_tv);
        noInternet = view.findViewById(R.id.no_internet_tv);
        progressBar.setVisibility(View.VISIBLE);

        airingToDay_viewAll = view.findViewById(R.id.viewAll_airingToDay_tv);
        onTheAir_viewAll = view.findViewById(R.id.viewAll_onTheAir_tv);
        popular_viewAll = view.findViewById(R.id.viewAll_popular_tv);
        toprated_viewAll = view.findViewById(R.id.viewAll_topRated_tv);

        airingToDay_recycler = view.findViewById(R.id.recyclerView_airingToday_tv);
        onTheAir_recycler = view.findViewById(R.id.recyclerView_onTheAir_tv);
        popular_recycler = view.findViewById(R.id.recyclerView_popular_tv);
        toprated_recycler = view.findViewById(R.id.recyclerView_topRAted_tv);

        airingToDay_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        onTheAir_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        popular_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        toprated_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        airingToDay_recycler.setLayoutManager(airingToDay_layoutManager);
        onTheAir_recycler.setLayoutManager(onTheAir_layoutManager);
        toprated_recycler.setLayoutManager(toprated_layoutManager);
        popular_recycler.setLayoutManager(popular_layoutManager);

        airingToDay_recycler.setHasFixedSize(true);
        onTheAir_recycler.setHasFixedSize(true);
        popular_recycler.setHasFixedSize(true);
        toprated_recycler.setHasFixedSize(true);

        airingToDay_adapter = new TvAdapter(getActivity());
        onTheAir_adapter = new TvAdapter(getActivity());
        popular_adapter = new TvAdapter(getActivity());
        toprated_adapter = new TvAdapter(getActivity());

        airingToDay_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Network.isNetworkConnection(getActivity())){
                    Network.snakBar(view);
                    return;
                }
                startActivity(Constanc.AIRING_TO_DAY, view);
            }
        });
        onTheAir_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Network.isNetworkConnection(getActivity())){
                    Network.snakBar(view);
                    return;
                }
                startActivity(Constanc.ON_THE_AIR, view);
            }
        });
        toprated_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Network.isNetworkConnection(getActivity())){
                    Network.snakBar(view);
                    return;
                }
                startActivity(Constanc.TOP_RATED_TV, view);
            }
        });
        popular_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Network.isNetworkConnection(getActivity())){
                    Network.snakBar(view);
                    return;
                }
                startActivity(Constanc.POPULAR_TV, view);
            }
        });

        airingToDay_recycler.setAdapter(airingToDay_adapter);
        onTheAir_recycler.setAdapter(onTheAir_adapter);
        popular_recycler.setAdapter(popular_adapter);
        toprated_recycler.setAdapter(toprated_adapter);

        airingToDay_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(airingToDay_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    isAiringTodayScrolled = false;
                    Network.snakBar(airingToDay_recycler);
                    if (!isOpenReceiver){
                        loadMore();
                    }
                }else {
                    loadAiringToDay();
                    airingToDay_currentPage++;
                    isAiringTodayScrolled = true;
                }

            }
        });
        onTheAir_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(onTheAir_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    isOnTheAirScrolled = false;
                    Network.snakBar(onTheAir_recycler);
                    if (!isOpenReceiver) {
                        loadMore();
                    }
                } else {
                    loadOnTheAir();
                    onTheAir_currentPage++;
                    isOnTheAirScrolled = true;
                }

            }
        });
        popular_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(popular_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    isPopularScrolled = false;
                    Network.snakBar(popular_recycler);
                    if (!isOpenReceiver) {
                        loadMore();
                    }
                } else {
                    loadPopular();
                    popular_currentPage++;
                    isPopularScrolled = true;
                }
            }
        });
        toprated_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(toprated_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!Network.isNetworkConnection(getActivity())) {
                    isTopratedScrolled = false;
                    Network.snakBar(toprated_recycler);
                    if (!isOpenReceiver) {
                        loadMore();
                    }
                } else {
                    loadTopRated();
                    topRated_currentPage++;
                    isTopratedScrolled = true;
                }
            }
        });

        if (Network.isNetworkConnection(getActivity()) && !isDataLoaded) {
            loadAiringToDay();
            loadOnTheAir();
            loadPopular();
            loadTopRated();

            isDataLoaded = true;
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (onTheAir_call != null) onTheAir_call.cancel();
        if (airingToDay_call != null) airingToDay_call.cancel();
        if (toprated_call != null) toprated_call.cancel();
        if (toprated_call != null) toprated_call.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        airingToDay_adapter.notifyDataSetChanged();
        onTheAir_adapter.notifyDataSetChanged();
        popular_adapter.notifyDataSetChanged();
        toprated_adapter.notifyDataSetChanged();
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
                    loadAiringToDay();
                    loadOnTheAir();
                    loadPopular();
                    loadTopRated();

                    isDataLoaded = true;
                    isOpenReceiver = false;
                    getActivity().unregisterReceiver(receiver);
                }
            });
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            getActivity().registerReceiver(receiver, filter);
            isOpenReceiver = true;
        } else if (Network.isNetworkConnection(getActivity()) && !isDataLoaded) {
            loadAiringToDay();
            loadOnTheAir();
            loadPopular();
            loadTopRated();
            isDataLoaded = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isOpenReceiver) {
            getActivity().unregisterReceiver(receiver);
            isOpenReceiver = false;
        }
    }

    private void startActivity(int typeMovie, View view) {
        if (Network.isNetworkConnection(getActivity())) {
            Intent intent = new Intent(getActivity(), ViewAllActivity.class);
            intent.putExtra(Constanc.TYPE_MOVIE, typeMovie);
            getActivity().startActivity(intent);
        } else {
            Network.snakBar(view);
        }
    }

    private void loadOnTheAir() {
        onTheAir_call = TvClient.onTheAir().getTv(Constanc.ON_THE_AIR_PATH, onTheAir_currentPage);
        onTheAir_call.enqueue(new retrofit2.Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;

                loadNextPage(2, response.body().getResults());
                isOnTheAir_loaded = true;
                loadAll();

            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {

            }
        });
    }

    private void loadAiringToDay() {
        airingToDay_call = TvClient.airingToDay().getTv(Constanc.AIRING_TO_DAY_PATH, airingToDay_currentPage);
        airingToDay_call.enqueue(new retrofit2.Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;
                loadNextPage(1, response.body().getResults());
                isAiringToday_loaded = true;
                loadAll();

            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {

            }
        });
    }

    private void loadPopular() {
        popular_call = TvClient.popular().getTv(Constanc.POPULAR_PATH, popular_currentPage);
        popular_call.enqueue(new retrofit2.Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;
                loadNextPage(3, response.body().getResults());
                isPopular_loaded = true;
                loadAll();

            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {

            }
        });
    }

    private void loadTopRated() {
        toprated_call = TvClient.topRated().getTv(Constanc.TOP_RATED_PATH, topRated_currentPage);
        toprated_call.enqueue(new retrofit2.Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null)
                    return;
                loadNextPage(4, response.body().getResults());
                isToprated_loaded = true;
                loadAll();
            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {

            }
        });
    }

    private void loadNextPage(int adapterType, List<TvModel.Result> newList) {
        if (adapterType == 1) {
            if (airingToDay_currentPage != 1)
                airingToDay_adapter.removeFooter();

            airingToDay_adapter.addList(newList);
            airingToDay_adapter.addLoadingFooter();
        } else if (adapterType == 2) {
            if (onTheAir_currentPage != 1)
                onTheAir_adapter.removeFooter();
            onTheAir_adapter.addList(newList);
            onTheAir_adapter.addLoadingFooter();
        } else if (adapterType == 3) {
            if (popular_currentPage != 1)
                popular_adapter.removeFooter();
            popular_adapter.addList(newList);
            popular_adapter.addLoadingFooter();
        } else if (adapterType == 4) {
            if (topRated_currentPage != 1)
                toprated_adapter.removeFooter();
            toprated_adapter.addList(newList);
            toprated_adapter.addLoadingFooter();
        }
    }

    private void loadAll() {

        if (isAiringToday_loaded && isOnTheAir_loaded && isPopular_loaded && isToprated_loaded) {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void loadMore() {
        receiver = new InternetReceiver(new InternetReceiver.InternetListener() {
            @Override
            public void networkConnected() {
                if (!isAiringTodayScrolled) {
                    loadAiringToDay();
                    airingToDay_currentPage++;
                    isAiringTodayScrolled = true;
                }
                if (!isPopularScrolled) {
                    loadPopular();
                    popular_currentPage++;
                    isPopularScrolled = true;

                }
                if (!isOnTheAirScrolled) {

                    loadOnTheAir();
                    onTheAir_currentPage++;
                    isOnTheAirScrolled = true;
                }
                if (!isTopratedScrolled) {
                    loadTopRated();
                    topRated_currentPage++;
                    isTopratedScrolled = true;
                }
                isOpenReceiver = false;
                getActivity().unregisterReceiver(receiver);
            }
        });
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(receiver, filter);
        isOpenReceiver = true;
    }
}
