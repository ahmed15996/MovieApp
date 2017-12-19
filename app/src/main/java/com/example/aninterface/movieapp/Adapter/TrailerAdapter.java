package com.example.aninterface.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aninterface.movieapp.Model.Trailer;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by interface on 04/12/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder>{
    private Context context;
    private List<Trailer.Result> list;

    public TrailerAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_trailer_movie,parent,false));
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        Trailer.Result model = list.get(position);
        if(model.getSite().equals("YouTube") && model.getName() != null){
            Picasso.with(context).load(Constanc.getTrailerImage(model.getKey())).into(holder.poster);
            holder.trailerName.setText(model.getName());
        }


    }
    public void addList(List<Trailer.Result> newList){
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(),newList.size()-1);

    }
    @Override
    public int getItemCount() {
        return list != null?list.size():0;
    }

    class TrailerHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView trailerName;
        public TrailerHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_trailler_movie);
            trailerName = itemView.findViewById(R.id.trailer_name_movie);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constanc.YOUTUBE_URL_WATCH+list.get(getAdapterPosition()).getKey())));
                }
            });
        }
    }
}
