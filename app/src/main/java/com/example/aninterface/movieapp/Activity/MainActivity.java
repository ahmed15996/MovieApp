package com.example.aninterface.movieapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aninterface.movieapp.Fragment.FavoriteFragment;
import com.example.aninterface.movieapp.Fragment.MovieFragment;
import com.example.aninterface.movieapp.Fragment.TvShowFragment;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Network;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private MovieFragment movieFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TvShowFragment tvShowFragment;
    private boolean doubleClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (movieFragment == null){
            movieFragment = new MovieFragment();
        }
        if(manager == null){
            manager = getSupportFragmentManager();
        }
        openFragment(movieFragment,"Movies");
        navigationView.setCheckedItem(R.id.movies_navigation);

    }


    @Override
    public void onBackPressed() {
        drawerLayout =  findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleClick){
                super.onBackPressed();
                return;
            }
            doubleClick = true;
            Toast.makeText(this,"One More To Cancel App", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClick = false;
                }
            },2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Enter Movie OR Tv Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!Network.isNetworkConnection(getApplicationContext())){
                  Network.snakBar(searchView);
                  return true;
                }
                Intent i = new Intent(MainActivity.this,SearchResultActivity.class);
                i.putExtra(Constanc.QUERY_SEARCH,query);
                startActivity(i);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(manager == null){
            manager = getSupportFragmentManager();
        }

        int id = item.getItemId();
        if(id == R.id.movies_navigation){
            if(movieFragment == null){
                movieFragment = new MovieFragment();
            }
            openFragment(movieFragment,"Movies");
        }else if(id == R.id.tvShow_navigation){
            if(tvShowFragment == null){
                tvShowFragment = new TvShowFragment();
            }
            openFragment(tvShowFragment,"Tv Show");

        }else if(id == R.id.favorites_navigation){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place,new FavoriteFragment()).commit();
        }else if (id == R.id.about_navigation){

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openFragment(Fragment fragment,String title){
        transaction = manager.beginTransaction().replace(R.id.fragment_place,fragment,"tag");
        transaction.commit();
        getSupportActionBar().setTitle(title);
    }
}

