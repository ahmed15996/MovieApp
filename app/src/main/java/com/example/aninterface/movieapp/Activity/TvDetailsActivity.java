package com.example.aninterface.movieapp.Activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aninterface.movieapp.Adapter.MovieCastAdapter;
import com.example.aninterface.movieapp.Adapter.SimilarTvAdapter;
import com.example.aninterface.movieapp.Adapter.TrailerAdapter;
import com.example.aninterface.movieapp.Model.CreditsModel;
import com.example.aninterface.movieapp.Model.DatabaseModel;
import com.example.aninterface.movieapp.Model.Trailer;
import com.example.aninterface.movieapp.Model.TvDetailsModel;
import com.example.aninterface.movieapp.Model.TvModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Database;
import com.example.aninterface.movieapp.Utils.EndlessRecyclerViewScrollListener;
import com.example.aninterface.movieapp.Utils.Network;
import com.example.aninterface.movieapp.WebServices.TvClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageView
            backDrop;

    private RecyclerView
            trailersRecycler,
            castRecycler,
            similarMoviesRecycler;

    private TextView
            trailerForm,
            castForm,
            similarForm;

    private TextView
            rating,
            overView,
            releaseDate,
            runTime,
            readMore,
            name,
            genre;

    private LinearLayout
            runTime_form,
            releaseDate_form,
            contentMain;
    private MovieCastAdapter castAdapter;
    private TrailerAdapter trailerAdapter;
    private SimilarTvAdapter similarTvAdapter;

    private int
            id,
            currntPage = 1;
    private LinearLayoutManager
            layoutManager_similarMovie,
            layoutManager_trailer,
            layoutManager_cast;
    private boolean
            isCastLoaded,
            isSimilarLoaded,
            isTrilerLoaded,
            isDetailsLoaded;

    private Call<CreditsModel> creditsModelCall;
    private Call<Trailer> trailerCall;
    private Call<TvModel> tvModelCall;
    private Call<TvDetailsModel> detailsModelCall;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);
        bind();
        Intent intent = getIntent();
        if (intent.hasExtra(Constanc.OPENT_DETAILS_KEY)) {
            id = intent.getIntExtra(Constanc.OPENT_DETAILS_KEY, -1);
            if (id != -1) {
                init();
                if (Network.isNetworkConnection(this)) {
                    loadCastTv();
                    loadSimilerTv();
                    loadTrailerTv();
                    loadTvDetails();
                    Toast.makeText(this,"Done",Toast.LENGTH_LONG).show();
                }
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        similarTvAdapter.notifyDataSetChanged();
        castAdapter.notifyDataSetChanged();
        trailerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (detailsModelCall != null)
            detailsModelCall.cancel();
        if (tvModelCall != null)
            tvModelCall.cancel();
        if (trailerCall != null)
            trailerCall.cancel();
        if (creditsModelCall != null)
            creditsModelCall.cancel();
    }

    private void bind() {
        isCastLoaded = false;
        isDetailsLoaded = false;
        isSimilarLoaded = false;
        isTrilerLoaded = false;

        toolbar = findViewById(R.id.toolbar_viewDetails_tv);
        setSupportActionBar(toolbar);
        setTitle("");

        db = new Database(this);

        appBarLayout = findViewById(R.id.app_bar_viewDetails_tv);
        progressBar = findViewById(R.id.progressbar_viewDetails_tv);
        contentMain = findViewById(R.id.content_viewDetails_tv);
        collapsingToolbarLayout = findViewById(R.id.collapsing_tv);

        backDrop = findViewById(R.id.tv_backDrop_viewDetails);
        name = findViewById(R.id.tvName_viewDetails);
        genre = findViewById(R.id.tvGenre_viewDetails);
        runTime_form = findViewById(R.id.runTime_form_tv);
        releaseDate_form = findViewById(R.id.releaseData_form_tv);
        rating = findViewById(R.id.rating_tvDetails);
        overView = findViewById(R.id.overView_viewDetails_tv);
        releaseDate = findViewById(R.id.releaseDate_tvDetails);
        readMore = findViewById(R.id.readMore_tvDetails);
        runTime = findViewById(R.id.runTime_tvDetails);

        trailerForm = findViewById(R.id.trailerFormTv);
        castForm = findViewById(R.id.castFormTv);
        similarForm = findViewById(R.id.similarTvForm);

        trailersRecycler = findViewById(R.id.trailers_recycler_tvDetails);
        castRecycler = findViewById(R.id.cast_recycler_tvDetails);
        similarMoviesRecycler = findViewById(R.id.similarTv_recycler_tvDetails);

    }

    private void init() {
        layoutManager_trailer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecycler.setLayoutManager(layoutManager_trailer);
        trailersRecycler.setHasFixedSize(true);
        trailerAdapter = new TrailerAdapter(this);
        trailersRecycler.setAdapter(trailerAdapter);


        castRecycler.setHasFixedSize(true);
        layoutManager_cast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castRecycler.setLayoutManager(layoutManager_cast);
        castAdapter = new MovieCastAdapter(this);
        castRecycler.setAdapter(castAdapter);

        layoutManager_similarMovie = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        similarMoviesRecycler.setLayoutManager(layoutManager_similarMovie);
        similarMoviesRecycler.setHasFixedSize(true);
        similarTvAdapter = new SimilarTvAdapter(this);
        similarMoviesRecycler.setAdapter(similarTvAdapter);
        similarMoviesRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager_similarMovie) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadSimilerTv();
                currntPage++;
            }
        });

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseDate_form.setVisibility(View.VISIBLE);
                runTime_form.setVisibility(View.VISIBLE);
                readMore.setVisibility(View.GONE);
                overView.setMaxLines(10);
            }
        });
    }

    private void loadCastTv() {
        creditsModelCall = TvClient.casttv().getCreditsTv(id);
        creditsModelCall.enqueue(new Callback<CreditsModel>() {
            @Override
            public void onResponse(Call<CreditsModel> call, Response<CreditsModel> response) {
                isCastLoaded = true;
                if (!response.isSuccessful() || response.body() == null ||
                        response.body().getCast() == null || response.body().getCast().size() == 0) {
                    castForm.setText("No Cast");
                    castRecycler.setVisibility(View.GONE);
                    return;
                }

                castAdapter.addList(response.body().getCast());

                isAllLoaded();
            }

            @Override
            public void onFailure(Call<CreditsModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "cast", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTrailerTv() {
        trailerCall = TvClient.trailers().getTrailersTv(id);
        trailerCall.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                isTrilerLoaded = true;
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null ||
                        response.body().getResults().size() == 0)
                    return;
                if (response.body().getResults().size() <= 0) {
                    trailerForm.setText("No Trailer");
                    trailersRecycler.setVisibility(View.GONE);
                    return;
                }
                trailerAdapter.addList(response.body().getResults());

                isAllLoaded();

            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "triler", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSimilerTv() {
        tvModelCall = TvClient.tvSimiler().getSimilarTv(id, currntPage);
        tvModelCall.enqueue(new Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                isSimilarLoaded = true;
                if (!(response.isSuccessful() || response.body() == null || response.body().getResults() == null ||
                        response.body().getResults().size() == 0) && currntPage == 1) {
                    similarForm.setText("No Similar Tv");
                    similarMoviesRecycler.setVisibility(View.GONE);
                    return;
                }

                if (currntPage != 1) {
                    similarTvAdapter.removeFooter();
                }
                similarTvAdapter.addList(response.body().getResults());
                similarTvAdapter.addLoadingFooter();
                isAllLoaded();
            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "similedr", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadTvDetails() {
        detailsModelCall = TvClient.tvDetails().gettvDetail(id);
        detailsModelCall.enqueue(new Callback<TvDetailsModel>() {
            @Override
            public void onResponse(Call<TvDetailsModel> call, Response<TvDetailsModel> response) {
                isDetailsLoaded = true;
                if (!response.isSuccessful() || response.body() == null)
                    return;
                final TvDetailsModel model = response.body();

                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle(model.getName());
                        } else {
                            collapsingToolbarLayout.setTitle("");
                        }
                    }
                });
                rating.setText(String.valueOf(model.getVoteAverage()));
                releaseDate.setText(model.getFirstAirDate());
                runTime.setText(runTime(model.getEpisodeRunTime()));
                overView.setText(model.getOverview());
                name.setText(model.getName());
                genre.setText(setGenre(model.getGenres()));
                Picasso.with(getApplicationContext()).load(Constanc.IMAGE_BASE_URL + model.getBackdropPath()).into(backDrop);
                isAllLoaded();
            }

            @Override
            public void onFailure(Call<TvDetailsModel> call, Throwable t) {
            }
        });
    }


    private void isAllLoaded() {
        if (isCastLoaded && isDetailsLoaded && isSimilarLoaded && isTrilerLoaded) {
            progressBar.setVisibility(View.GONE);
            contentMain.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }

    private String runTime(List<Integer> time) {
        String result = "";
        int hr = 0, min = 0;
        if (!time.isEmpty() || time.get(0) > 0) {
            if (time.get(0) > 60) {
                hr = time.get(0) / 60;
                min = time.get(0) % 60;
                result = "hr " + String.valueOf(hr) + " min " + String.valueOf(min);
            } else if (time.get(0) < 60) {
                result = String.valueOf(time.get(0)) + " ( min )";
            }
        }
        return result;
    }

    private String setGenre(List<TvDetailsModel.Genre> genre) {
        String s = "";
        for (int i = 0; i < genre.size(); i++) {
            s += genre.get(i).getName() + " - ";
        }
        return s.substring(0, s.length() - 2);
    }

}
