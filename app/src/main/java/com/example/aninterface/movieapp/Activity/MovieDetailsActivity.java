package com.example.aninterface.movieapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.example.aninterface.movieapp.Adapter.SimilarMoviesAdapter;
import com.example.aninterface.movieapp.Adapter.TrailerAdapter;
import com.example.aninterface.movieapp.Model.CreditsModel;
import com.example.aninterface.movieapp.Model.DatabaseModel;
import com.example.aninterface.movieapp.Model.MovieDetailsModel;
import com.example.aninterface.movieapp.Model.MovieModel;
import com.example.aninterface.movieapp.Model.Trailer;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Database;
import com.example.aninterface.movieapp.Utils.EndlessRecyclerViewScrollListener;
import com.example.aninterface.movieapp.Utils.Network;
import com.example.aninterface.movieapp.WebServices.MovieClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageView
            backDrop,
            saved,
            noSaved;

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

    private LinearLayoutManager
            layoutManager_similarMovie,
            layoutManager_trailer,
            layoutManager_cast;

    private SimilarMoviesAdapter similarMoviesAdapter;
    private MovieCastAdapter castAdapter;
    private TrailerAdapter trailerAdapter;

    private int
            id,
            currntPage = 1;

    private Call<MovieDetailsModel> movieDetailsModelCall;
    private Call<Trailer> trailerCall;
    private Call<CreditsModel> creditsModelCall;
    private Call<MovieModel> movieModelCall;

    private Database db;

    private boolean
            isCastLoaded,
            isSimilarLoaded,
            isTrilerLoaded,
            isDetailsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        bind();
        Intent intent = getIntent();
        if (intent.hasExtra(Constanc.OPENT_DETAILS_KEY)) {
            id = intent.getIntExtra(Constanc.OPENT_DETAILS_KEY, 0);
            if (id != 0) {
                init();
                if (Network.isNetworkConnection(this)) {
                    loadSimilerMovie();
                    loadCastMovie();
                    loadTrailersMovie();
                    loadDetailsMovie();
                }
            }
        }
    }

    private void bind() {
        isCastLoaded = false;
        isDetailsLoaded = false;
        isSimilarLoaded = false;
        isTrilerLoaded = false;

        toolbar = findViewById(R.id.toolbar_viewDetails);
        setSupportActionBar(toolbar);
        setTitle("");

        db = new Database(this);
        appBarLayout = findViewById(R.id.app_bar_viewDetails);
        progressBar = findViewById(R.id.progressbar_viewDetails);
        contentMain = findViewById(R.id.content_viewDetails);
        collapsingToolbarLayout = findViewById(R.id.collapsing);

        backDrop = findViewById(R.id.movie_backDrop_viewDetails);
        saved = findViewById(R.id.save_viewDetails);
        noSaved = findViewById(R.id.noSaved_viewDetails);
        name = findViewById(R.id.movieName_viewDetails);
        genre = findViewById(R.id.movieGenre_viewDetails);
        runTime_form = findViewById(R.id.runTime_form);
        releaseDate_form = findViewById(R.id.releaseData_form);
        rating = findViewById(R.id.rating_movieDetails);
        overView = findViewById(R.id.overView_viewDetails);
        releaseDate = findViewById(R.id.releaseDate_movieDetails);
        readMore = findViewById(R.id.readMore_movieDetails);
        runTime = findViewById(R.id.runTime_movieDetails);

        trailerForm = findViewById(R.id.trailerForm);
        castForm = findViewById(R.id.castForm);
        similarForm = findViewById(R.id.similarMovieForm);

        trailersRecycler = findViewById(R.id.trailers_recycler_movieDetails);
        castRecycler = findViewById(R.id.cast_recycler_movieDetails);
        similarMoviesRecycler = findViewById(R.id.similarMovies_recycler_movieDetails);

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

    private void init() {

        layoutManager_similarMovie = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        similarMoviesRecycler.setLayoutManager(layoutManager_similarMovie);
        similarMoviesRecycler.setHasFixedSize(true);
        similarMoviesAdapter = new SimilarMoviesAdapter(this);
        similarMoviesRecycler.setAdapter(similarMoviesAdapter);
        similarMoviesRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager_similarMovie) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadSimilerMovie();
                currntPage++;
            }
        });

        layoutManager_trailer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecycler.setLayoutManager(layoutManager_trailer);
        trailersRecycler.setHasFixedSize(true);
        trailerAdapter = new TrailerAdapter(this);
        trailersRecycler.setAdapter(trailerAdapter);

        castRecycler = findViewById(R.id.cast_recycler_movieDetails);
        castRecycler.setHasFixedSize(true);
        layoutManager_cast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castRecycler.setLayoutManager(layoutManager_cast);
        castAdapter = new MovieCastAdapter(this);
        castRecycler.setAdapter(castAdapter);

        if (db.selectItem(Constanc.MOVIE_TABLE, String.valueOf(id))) {
            noSaved.setVisibility(View.GONE);
            saved.setVisibility(View.VISIBLE);
        } else {
            noSaved.setVisibility(View.VISIBLE);
            saved.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        castAdapter.notifyDataSetChanged();
        trailerAdapter.notifyDataSetChanged();
        similarMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (movieModelCall != null)
            movieModelCall.cancel();
        if (trailerCall != null)
            trailerCall.cancel();
        if (creditsModelCall != null)
            creditsModelCall.cancel();
        if (movieDetailsModelCall != null)
            movieDetailsModelCall.cancel();
    }


    private void loadDetailsMovie() {
        movieDetailsModelCall = MovieClient.movieDetails().getMovieDetail(id);
        movieDetailsModelCall.enqueue(new Callback<MovieDetailsModel>() {
            @Override
            public void onResponse(final Call<MovieDetailsModel> call, Response<MovieDetailsModel> response) {
                isDetailsLoaded = true;
                if (!response.isSuccessful() || response.body() == null)
                    return;
                final MovieDetailsModel model = response.body();

                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle(model.getTitle());
                        } else {
                            collapsingToolbarLayout.setTitle("");
                        }
                    }
                });

                rating.setText(String.valueOf(model.getVoteAverage()));
                releaseDate.setText(model.getReleaseDate());
                runTime.setText(runTime(model.getRuntime()));
                overView.setText(model.getOverview());
                name.setText(model.getTitle());
                genre.setText(setGenre(model.getGenres()));
                Picasso.with(getApplicationContext()).load(Constanc.IMAGE_BASE_URL + model.getBackdropPath()).into(backDrop);
                isAllDataLoaded();
                setdatabase(model.getTitle(), model.getPosterPath());
            }

            @Override
            public void onFailure(Call<MovieDetailsModel> call, Throwable t) {
            }
        });
    }

    private void loadTrailersMovie() {
        trailerCall = MovieClient.movieTrailers().getTrailers(id);
        trailerCall.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                isTrilerLoaded = true;
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null
                        || response.body().getResults().size() <= 0) {
                    trailerForm.setText("No Trailer");
                    trailersRecycler.setVisibility(View.GONE);
                    return;
                }

                trailerAdapter.addList(response.body().getResults());
                isAllDataLoaded();
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
            }
        });
    }

    private void loadCastMovie() {
        creditsModelCall = MovieClient.castMovie().getCreditsMovies(id);
        creditsModelCall.enqueue(new Callback<CreditsModel>() {
            @Override
            public void onResponse(Call<CreditsModel> call, Response<CreditsModel> response) {
                isCastLoaded = true;
                if (!response.isSuccessful() || response.body() == null || response.body().getCast().size() <= 0) {
                    castForm.setText("No Cast");
                    castRecycler.setVisibility(View.GONE);
                    return;
                }
                castAdapter.addList(response.body().getCast());
                isAllDataLoaded();
            }

            @Override
            public void onFailure(Call<CreditsModel> call, Throwable t) {

            }
        });
    }

    private void loadSimilerMovie() {
        movieModelCall = MovieClient.movieSimiler().getSimilarMovies(id, currntPage);
        movieModelCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                isSimilarLoaded = true;
                if (response.body() == null || !response.isSuccessful() ||
                        response.body().getResults() == null || response.body().getResults().size() <= 0) {
                    similarForm.setText("No Similar Movie");
                    similarMoviesRecycler.setVisibility(View.GONE);
                    return;
                }

                if (currntPage != 1) {
                    similarMoviesAdapter.removeFooter();
                }
                similarMoviesAdapter.addList(response.body().getResults());
                similarMoviesAdapter.addLoadingFooter();

                isAllDataLoaded();
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void isAllDataLoaded() {
        if (isTrilerLoaded & isDetailsLoaded & isCastLoaded & isSimilarLoaded) {
            progressBar.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);
            contentMain.setVisibility(View.VISIBLE);
        }
    }

    private String runTime(int runTime) {

        int hr = 0;
        int min = 0;
        if (runTime > 60) {
            hr = runTime / 60;
            min = runTime % 60;
        }
        if (hr > 0 && min > 0) {
            return String.valueOf(hr) + " hr - " + String.valueOf(min) + " min ";
        } else if (hr > 0 && min == 0) {
            return String.valueOf(hr) + " hr";
        } else if (hr == 0 && min == 0) {
            return "";
        }
        return "";
    }

    private String setGenre(List<MovieDetailsModel.Genre> genre) {
        String s = "";
        for (int i = 0; i < genre.size(); i++) {
            s += genre.get(i).getName() + " - ";
        }
        return s.substring(0, s.length() - 2);
    }

    private void setdatabase(final String title, final String poster) {
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete( Constanc.MOVIE_TABLE, String.valueOf(id));
                noSaved.setVisibility(View.VISIBLE);
                saved.setVisibility(View.GONE);
            }
        });
        noSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.add(Constanc.MOVIE_TABLE, new DatabaseModel(String.valueOf(id), title, poster))) {
                    noSaved.setVisibility(View.GONE);
                    saved.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
