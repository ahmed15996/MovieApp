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

public class ViewAllTvAdapter extends RecyclerView.Adapter<ViewAllTvAdapter.ViewAllTvHolder> {

    private Context context;
    private List<TvModel.Result> list;
    private Database db;

    public ViewAllTvAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        db = new Database(context);
    }

    @Override
    public ViewAllTvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewAllTvHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_all, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewAllTvHolder holder, int position) {
        TvModel.Result movie = list.get(position);

        holder.rating.setText(String.valueOf(movie.getVoteAverage()));
        holder.movieName.setText(movie.getName());
        LoadImage.LoadImage(context, Constanc.IMAGE_BASE_URL + movie.getPosterPath(), holder.poster, holder.progressBar);
        if (db.selectItem(Constanc.TV_TABLE, String.valueOf(movie.getId()))) {
            holder.noSaved.setVisibility(View.GONE);
            holder.saved.setVisibility(View.VISIBLE);
        } else {
            holder.noSaved.setVisibility(View.VISIBLE);
            holder.saved.setVisibility(View.GONE);
        }
    }

    public void addList(List<TvModel.Result> newList) {
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(), newList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewAllTvHolder extends RecyclerView.ViewHolder {
        private ImageView poster, noSaved, saved;
        private TextView movieName, rating;
        private ProgressBar progressBar;

        public ViewAllTvHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster_viewAll);
            progressBar = itemView.findViewById(R.id.loadinngImage_viewAll);
            noSaved = itemView.findViewById(R.id.nosaved_viewAll);
            saved = itemView.findViewById(R.id.save_viewAll);
            movieName = itemView.findViewById(R.id.movie_name_viewAll);
            rating = itemView.findViewById(R.id.movie_rating_viewAll);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Network.isNetworkConnection(context)) {
                        Intent i = new Intent(context, TvDetailsActivity.class);
                        i.putExtra(Constanc.OPENT_DETAILS_KEY, list.get(getAdapterPosition()).getId());
                        context.startActivity(i);
                    } else {
                        Network.snakBar(view);
                    }

                }
            });
            noSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TvModel.Result movie = list.get(getAdapterPosition());
                    if (db.add(Constanc.TV_TABLE, new DatabaseModel(String.valueOf(movie.getId()), movie.getName(), movie.getPosterPath()))) {
                        noSaved.setVisibility(View.GONE);
                        saved.setVisibility(View.VISIBLE);
                    }
                }
            });
            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.delete(Constanc.TV_TABLE, String.valueOf(list.get(getAdapterPosition()).getId()));
                    noSaved.setVisibility(View.VISIBLE);
                    saved.setVisibility(View.GONE);
                }
            });
        }
    }
}
