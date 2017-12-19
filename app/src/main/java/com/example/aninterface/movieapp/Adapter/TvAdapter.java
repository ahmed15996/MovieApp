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

import com.example.aninterface.movieapp.Activity.TvDetailsActivity;
import com.example.aninterface.movieapp.Model.DatabaseModel;
import com.example.aninterface.movieapp.Model.TvModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Database;
import com.example.aninterface.movieapp.Utils.LoadImage;
import com.example.aninterface.movieapp.Utils.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by interface on 08/12/2017.
 */

public class TvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<TvModel.Result> list;
    private Database db;
    private int ITEM = 1, LOADING = 0;
    private boolean isLoadingAdded;

    public TvAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        db = new Database(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM) {
            view = inflater.inflate(R.layout.item_movies, parent, false);
            holder = new TvHolder(view);
        } else {
            view = inflater.inflate(R.layout.loading_movie_item, parent, false);
            holder = new LoadingHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM) {
            TvModel.Result item = list.get(position);
            TvHolder mHolder = (TvHolder) holder;

            mHolder.rating_movie.setText(String.valueOf(item.getVoteAverage()));
            mHolder.title_movie.setText(item.getName());

            LoadImage.LoadImage(context, Constanc.IMAGE_BASE_URL + item.getPosterPath(), mHolder.poster, mHolder.progressBar);

            if (db.selectItem(Constanc.TV_TABLE, String.valueOf(item.getId()))) {
                mHolder.saved.setVisibility(View.VISIBLE);
                mHolder.notSaved.setVisibility(View.GONE);
            } else {
                mHolder.saved.setVisibility(View.GONE);
                mHolder.notSaved.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        list.add(new TvModel.Result());
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeFooter() {
        isLoadingAdded = false;
        int position = list.size() - 1;
        TvModel.Result item = list.get(position);
        if (item != null) {
            list.remove(item);
            notifyItemRemoved(position);
        }
    }

    public void addList(List<TvModel.Result> newList) {
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(), newList.size() - 1);
    }


    public TvModel.Result getItem(int position) {
        return list.get(position);
    }

    class TvHolder extends RecyclerView.ViewHolder {
        private ImageView poster, saved, notSaved;
        private TextView title_movie, rating_movie;
        private ProgressBar progressBar;

        public TvHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            progressBar = itemView.findViewById(R.id.movie_loding_holder);
            title_movie = itemView.findViewById(R.id.movie_title);
            rating_movie = itemView.findViewById(R.id.rating_movies);
            saved = itemView.findViewById(R.id.saved_movies);
            notSaved = itemView.findViewById(R.id.notSaved_movies);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Network.isNetworkConnection(context)) {
                        Intent intent = new Intent(context, TvDetailsActivity.class);
                        intent.putExtra(Constanc.OPENT_DETAILS_KEY, list.get(getAdapterPosition()).getId());
                        context.startActivity(intent);
                    } else {
                        Network.snakBar(view);
                    }

                }
            });

            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.delete(Constanc.TV_TABLE, String.valueOf(list.get(getAdapterPosition()).getId()));
                    saved.setVisibility(View.GONE);
                    notSaved.setVisibility(View.VISIBLE);
                }
            });
            notSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TvModel.Result result = list.get(getAdapterPosition());
                    if (db.add(Constanc.TV_TABLE, new DatabaseModel(String.valueOf(result.getId()),
                            result.getName(), result.getPosterPath()))) {
                        notSaved.setVisibility(View.GONE);
                        saved.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
