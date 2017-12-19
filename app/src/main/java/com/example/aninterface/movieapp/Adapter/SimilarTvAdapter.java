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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by interface on 12/12/2017.
 */

public class SimilarTvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TvModel.Result> list;
    private Database db;
    private boolean isAddLoadded = false;
    private int ITEM = 0, LOADING = 1;

    public SimilarTvAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        db = new Database(context);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1) && isAddLoadded ? LOADING : ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            return new SimilarTvHolder(inflater.inflate(R.layout.item_similar_movie, parent, false));
        } else {
            return new SimilarTvLoading(inflater.inflate(R.layout.similar_movie_loading, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM) {
            SimilarTvHolder mHolder = (SimilarTvHolder) holder;
            TvModel.Result item = list.get(position);
            mHolder.movieName.setText(item.getName());
            LoadImage.LoadImage(context, Constanc.IMAGE_BASE_URL + item.getPosterPath(), mHolder.poster, mHolder.progressBar);
            if (db.selectItem(Constanc.TV_TABLE, String.valueOf(item.getId()))) {
                mHolder.save.setVisibility(View.VISIBLE);
                mHolder.noSave.setVisibility(View.GONE);
            } else {
                mHolder.save.setVisibility(View.GONE);
                mHolder.noSave.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void addLoadingFooter() {
        isAddLoadded = true;
        list.add(new TvModel.Result());
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeFooter() {
        isAddLoadded = false;
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


    class SimilarTvHolder extends RecyclerView.ViewHolder {
        private ImageView poster, noSave, save;
        private ProgressBar progressBar;
        private TextView movieName;

        public SimilarTvHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster_similarMovie);
            progressBar = itemView.findViewById(R.id.progresbar_similar);
            noSave = itemView.findViewById(R.id.noSave_similarMovie);
            save = itemView.findViewById(R.id.save_similarMovie);
            movieName = itemView.findViewById(R.id.movieName_similarMovie);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TvDetailsActivity.class);
                    intent.putExtra(Constanc.OPENT_DETAILS_KEY, list.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

            noSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TvModel.Result item = list.get(getAdapterPosition());
                    if (db.add(Constanc.TV_TABLE, new DatabaseModel(String.valueOf(item.getId()), item.getName(), item.getPosterPath()))) {
                        noSave.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);
                    }
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.delete(Constanc.TV_TABLE, String.valueOf(list.get(getAdapterPosition()).getId()));
                    save.setVisibility(View.GONE);
                    noSave.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    class SimilarTvLoading extends RecyclerView.ViewHolder {
        public SimilarTvLoading(View itemView) {
            super(itemView);
        }
    }
}
