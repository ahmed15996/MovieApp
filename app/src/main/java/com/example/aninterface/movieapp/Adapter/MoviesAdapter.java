package com.example.aninterface.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aninterface.movieapp.Activity.MovieDetailsActivity;
import com.example.aninterface.movieapp.Model.DatabaseModel;
import com.example.aninterface.movieapp.Model.MovieModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Database;
import com.example.aninterface.movieapp.Utils.LoadImage;
import com.example.aninterface.movieapp.Utils.Network;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<MovieModel.ResultMovie> list;
    private Database db;
    private int ITEM = 1, LOADING = 0;
    private boolean isLoadingAdded;

    public MoviesAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        db = new Database(context);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() -1 && isLoadingAdded)? LOADING : ITEM;
    }

    public void addList(List<MovieModel.ResultMovie> newList){
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(), newList.size() - 1);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view ;
        RecyclerView.ViewHolder holder ;
        if(viewType == ITEM){
            view = inflater.inflate(R.layout.item_movies,parent,false);
            holder = new MoviesHolder(view);
        }else {
            view = inflater.inflate(R.layout.loading_movie_item,parent,false);
            holder = new LoadingHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM){
            MovieModel.ResultMovie item = list.get(position);
            final MoviesHolder mHolder = (MoviesHolder) holder;
                mHolder.rating_movie.setText(String.valueOf(item.getVoteAverage()));
                mHolder.title_movie.setText(item.getTitle());
            LoadImage.LoadImage(context,Constanc.IMAGE_BASE_URL+item.getPosterPath(),mHolder.poster,mHolder.loadig);
                if (db.selectItem(Constanc.MOVIE_TABLE, String.valueOf(item.getId()))) {
                    mHolder.notSaved.setVisibility(View.GONE);
                    mHolder.saved.setVisibility(View.VISIBLE);
                } else {
                    mHolder.notSaved.setVisibility(View.VISIBLE);
                    mHolder.saved.setVisibility(View.GONE);
                }
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public void addLoadingFooter(){
        isLoadingAdded = true;
        list.add(new MovieModel.ResultMovie());
        notifyItemInserted(getItemCount() - 1);
    }
    public void removeFooter(){
        isLoadingAdded = false;
        int position = list.size() - 1;
        MovieModel.ResultMovie item = list.get(position);
        if(item != null && position > 0){
            list.remove(item);
            notifyItemRemoved(position);
        }
    }

    public MovieModel.ResultMovie getItem(int position){
        return list.get(position);
    }


    class MoviesHolder extends RecyclerView.ViewHolder{
        private ImageView poster, saved, notSaved;
        private ProgressBar loadig;
        private  TextView title_movie, rating_movie;
        public MoviesHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            loadig = itemView.findViewById(R.id.movie_loding_holder);
            title_movie = itemView.findViewById(R.id.movie_title);
            rating_movie = itemView.findViewById(R.id.rating_movies);
            saved = itemView.findViewById(R.id.saved_movies);
            notSaved = itemView.findViewById(R.id.notSaved_movies);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Network.isNetworkConnection(context)) {
                        Intent i = new Intent(context, MovieDetailsActivity.class);
                        i.putExtra(Constanc.OPENT_DETAILS_KEY, list.get(getAdapterPosition()).getId());
                        context.startActivity(i);
                    } else {
                        Network.snakBar(view);
                    }
                }
            });
            notSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieModel.ResultMovie movie = list.get(getAdapterPosition());
                 if (db.add(Constanc.MOVIE_TABLE,new DatabaseModel(String.valueOf(movie.getId()),
                            movie.getTitle(),movie.getPosterPath()))){
                     notSaved.setVisibility(View.GONE);
                     saved.setVisibility(View.VISIBLE);
                 }
                }
            });
            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.delete(Constanc.MOVIE_TABLE, String.valueOf(list.get(getAdapterPosition()).getId()));
                    notSaved.setVisibility(View.VISIBLE);
                    saved.setVisibility(View.GONE);
                }
            });

        }
    }
    class LoadingHolder extends RecyclerView.ViewHolder{

        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
