package com.example.aninterface.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aninterface.movieapp.Activity.MovieDetailsActivity;
import com.example.aninterface.movieapp.Activity.TvDetailsActivity;
import com.example.aninterface.movieapp.Fragment.Callback;
import com.example.aninterface.movieapp.Model.DatabaseModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.example.aninterface.movieapp.Utils.Database;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by interface on 18/12/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {

    private Context context;
    private List<DatabaseModel> list;
    private Database database;
    private String table;
    private Callback callback;

    public FavoriteAdapter(Context context, String table, Callback callback) {
        this.context = context;
        this.callback = callback;
        database = new Database(context);
        this.table = table;
        list = database.selectAll(table);
    }


    @Override
    public FavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteHolder(LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(FavoriteHolder holder, int position) {
        DatabaseModel model = list.get(position);
        Picasso.with(context).load(Constanc.IMAGE_BASE_URL + model.getPoster_path()).into(holder.poster);
        holder.name.setText(model.getMovie_name());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void addList(List<DatabaseModel> newList) {
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(), newList.size() - 1);
    }

    public boolean isEmpty() {
        if (list == null || list.size() == 0)
            return true;
        return false;
    }

    public void deleteItem(int position) {
        database.delete(table, list.get(position).getMovie_id());
        list.remove(position);
        notifyItemRemoved(position);
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        private ImageView poster, save;
        private TextView name;

        public FavoriteHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.favorite_poster);
            save = itemView.findViewById(R.id.favoriteSaved);
            name = itemView.findViewById(R.id.favoriteName);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(getAdapterPosition(), view);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i;
                    if (table.equals(Constanc.MOVIE_TABLE))
                        i = new Intent(context, MovieDetailsActivity.class);
                    else
                        i = new Intent(context, TvDetailsActivity.class);
                    i.putExtra(Constanc.OPENT_DETAILS_KEY, Integer.parseInt(list.get(getAdapterPosition()).getMovie_id()));
                    context.startActivity(i);
                }
            });
        }

    }
}
