package com.example.aninterface.movieapp.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.aninterface.movieapp.Adapter.ViewAllAdapter;
import com.example.aninterface.movieapp.Adapter.ViewAllTvAdapter;
import com.example.aninterface.movieapp.Model.MovieModel;
import com.example.aninterface.movieapp.Model.TvModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Network;
import com.example.aninterface.movieapp.WebServices.MovieClient;
import com.example.aninterface.movieapp.WebServices.TvClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllActivity extends AppCompatActivity {
    private LinearLayout form;
    private RecyclerView recyclerView;
    private ProgressBar progressBar, progressBar_loading;
    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;
    private ViewAllAdapter adapter;
    private ViewAllTvAdapter tvAdapter;
    private Call<MovieModel> moviesCall;
    private Call<TvModel> tvCall;

    private String typeMovies;
    private int type;
    private int currentPage = 1;
    private String path;
    private boolean isMovie;

    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", -1);
        }

        form = findViewById(R.id.viewall_from);
        progressBar = findViewById(R.id.progresbar_viewAll_movies);
        toolbar = findViewById(R.id.toolbar_viewAll_movies);
        progressBar_loading = findViewById(R.id.test);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.hasExtra(Constanc.TYPE_MOVIE)) {
            type = intent.getIntExtra(Constanc.TYPE_MOVIE, 0);
            hadelIntent(type);
            getSupportActionBar().setTitle(typeMovies);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            recyclerView = findViewById(R.id.viewAll_recycler_movies);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            } else {
                layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            }
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            if (isMovie) {
                adapter = new ViewAllAdapter(this);
                recyclerView.setAdapter(adapter);
                if (currentPosition != -1)
                    recyclerView.scrollToPosition(currentPosition);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if (totalItemCount > previousTotal) {
                                loading = false;
                                previousTotal = totalItemCount;
                            }
                        }
                        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                            loadMovies();
                            loading = true;
                        }

                    }

                });
                loadMovies();
            } else {

                tvAdapter = new ViewAllTvAdapter(this);
                recyclerView.setAdapter(tvAdapter);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if (totalItemCount > previousTotal) {
                                loading = false;
                                previousTotal = totalItemCount;
                            }
                        }
                        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                            loadTv();
                            loading = true;
                        }

                    }

                });
                loadTv();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter Movie OR Tv Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Network.isNetworkConnection(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), SearchResultActivity.class);
                    i.putExtra(Constanc.QUERY_SEARCH, query);
                    startActivity(i);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (moviesCall != null)
            moviesCall.cancel();
        if (tvCall != null)
            tvCall.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("currentPosition", layoutManager.findFirstVisibleItemPosition());
    }

    private void hadelIntent(int type) {
        if (type == 1) {
            typeMovies = "Playing Now Movies";
            path = Constanc.NOW_PLAYING_PATH;
            isMovie = true;
        } else if (type == 2) {
            typeMovies = "Top Rated Movies";
            path = Constanc.TOP_RATED_PATH;
            isMovie = true;
        } else if (type == 3) {
            typeMovies = "Popular Movies";
            path = Constanc.POPULAR_PATH;
            isMovie = true;
        } else if (type == 4) {
            typeMovies = "Upcoming Movies";
            path = Constanc.UPCOMING_PATH;
            isMovie = true;
        } else if (type == 5) {
            typeMovies = "On The Air TV Show";
            path = Constanc.ON_THE_AIR_PATH;
            isMovie = false;
        } else if (type == 6) {
            typeMovies = "Airing To Day TV Show";
            path = Constanc.AIRING_TO_DAY_PATH;
            isMovie = false;
        } else if (type == 7) {
            typeMovies = "Popular TV Show";
            path = Constanc.POPULAR_PATH;
            isMovie = false;
        } else if (type == 8) {
            typeMovies = "Top Rated TV Show";
            path = Constanc.TOP_RATED_PATH;
            isMovie = false;
        }
    }

    private void loadMovies() {
        progressBar_loading.setVisibility(View.VISIBLE);
        moviesCall = MovieClient.nowPlaying().getMovies(path, currentPage);
        moviesCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                if (!response.isSuccessful() && response.body() == null && response.body().getResults() == null)
                    return;

                adapter.addList(response.body().getResults());
                progressBar_loading.setVisibility(View.GONE);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), response.body().getResults().size() - 1);
                if (currentPage == 1) {
                    progressBar.setVisibility(View.GONE);
                    form.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    private void loadTv(){
        progressBar_loading.setVisibility(View.VISIBLE);
        tvCall = TvClient.topRated().getTv(path, currentPage);
        tvCall.enqueue(new Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                if (!response.isSuccessful() && response.body() == null && response.body().getResults() == null)
                    return;
                if (currentPage == 1) {
                    progressBar.setVisibility(View.GONE);
                    form.setVisibility(View.VISIBLE);
                }
                progressBar_loading.setVisibility(View.GONE);
                tvAdapter.addList(response.body().getResults());

            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {

            }
        });
    }


}
