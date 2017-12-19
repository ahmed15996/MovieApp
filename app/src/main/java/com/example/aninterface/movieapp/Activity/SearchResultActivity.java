package com.example.aninterface.movieapp.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aninterface.movieapp.Adapter.SearchAdapter;
import com.example.aninterface.movieapp.Model.SearchResult;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.EndlessRecyclerViewScrollListener;
import com.example.aninterface.movieapp.Utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noResult;
    private GridLayoutManager gridLayoutManager;
    private SearchAdapter adapter;

    private SearchResponce searchResult;

    private String query;
    private int currentPage = 1;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", -1);
        }
        bind();
        Intent i = getIntent();
        if (i.hasExtra(Constanc.QUERY_SEARCH)) {
            query = i.getStringExtra(Constanc.QUERY_SEARCH);
            getSupportActionBar().setTitle(query);
            init();
            loadData();
            if (currentPosition != -1)
                recyclerView.smoothScrollToPosition(currentPosition);
            recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (Network.isNetworkConnection(getApplicationContext())) {
                        loadData();
                        currentPage++;
                    } else {
                        Network.snakBar(recyclerView);
                    }

                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPosition", gridLayoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }

    private void bind() {
        toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView_search);
        progressBar = findViewById(R.id.progressbar_search);
        noResult = findViewById(R.id.noResut_search);
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new SearchAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            gridLayoutManager = new GridLayoutManager(this, 1);
        else
            gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

    }


    private void loadData() {
        searchResult = new SearchResponce();
        searchResult.execute();
        if (currentPage == 1) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    class SearchResponce extends AsyncTask<Void, Void, SearchResult> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SearchResult doInBackground(Void... voids) {
            String mUrl = "https://api.themoviedb.org/3/search/multi?api_key=0427aa69e9d4db5185eaf3d77c7728c9&query="
                    + query + "&page=" + currentPage;
            URL url;
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            StringBuffer buffer = new StringBuffer();
            SearchResult result = new SearchResult();
            try {
                url = new URL(mUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode() != 200) {
                    return null;
                }
                inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = (reader.readLine())) != null) {
                    buffer.append(line);
                }
                if (buffer.toString() == null) {
                    return null;
                }
                JSONObject jsonObject = new JSONObject(buffer.toString());
                result.setPage(jsonObject.getInt("page"));
                result.setTotalPages(jsonObject.getInt("total_pages"));
                result.setTotalResults(jsonObject.getInt("total_results"));
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                List<SearchResult.Result> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    SearchResult.Result item = new SearchResult.Result();
                    item.setId(object.getInt("id"));
                    item.setMediaType(object.getString("media_type"));
                    item.setPosterPath(object.getString("poster_path"));
                    item.setOverview(object.getString("overview"));

                    if (object.getString("media_type").equals("tv")) {

                        item.setFirstAirDate(object.getString("first_air_date"));
                        item.setName(object.getString("name"));

                    } else if (object.getString("media_type").equals("movie")) {
                        item.setFirstAirDate(object.getString("release_date"));
                        item.setName(object.getString("title"));
                    }
                    list.add(item);
                }
                result.setResults(list);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(SearchResult responce) {
            super.onPostExecute(responce);
            if (currentPage == 1 && (responce == null || responce.getResults() == null || responce.getResults().size() <= 0)) {
                progressBar.setVisibility(View.GONE);
                noResult.setVisibility(View.VISIBLE);
                return;
            }
            if (responce.getTotalPages() == currentPage) {
                adapter.addList(responce.getResults());
                return;
            }
            if (searchResult != null || responce.getResults() != null || responce.getResults().size() != 0) {
                if (currentPage != 1) {
                    adapter.removeFooter();
                }
                adapter.addList(responce.getResults());
                adapter.addLoadingFooter();
            }
        }

    }
}
