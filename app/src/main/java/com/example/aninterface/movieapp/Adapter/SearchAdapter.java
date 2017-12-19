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
import com.example.aninterface.movieapp.Model.MovieModel;
import com.example.aninterface.movieapp.Model.SearchResult;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by interface on 13/12/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<SearchResult.Result> list;
    private boolean isLoading;
    private int ITEM = 1, LOADING = 0;

    public SearchAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1) && isLoading ? LOADING : ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM){
            holder = new SearchHolder(inflater.inflate(R.layout.item_search,parent,false));
        }else {
            holder = new SearchLoading(inflater.inflate(R.layout.loading_item,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchResult.Result item = list.get(position);
        if (getItemViewType(position) == ITEM && item != null && item.getFirstAirDate() != null &&
                (item.getMediaType().equals("tv")||item.getMediaType().equals("movie") )){
            SearchHolder mHolder = (SearchHolder)holder;
            Picasso.with(context).load(Constanc.IMAGE_BASE_URL+item.getPosterPath()).into(mHolder.poster);
            mHolder.title.setText(item.getName());
            mHolder.type.setText(item.getMediaType());
            mHolder.overView.setText(item.getOverview());
            if (!item.getFirstAirDate().isEmpty() || item.getFirstAirDate().length() >=4){
                mHolder.year.setText(item.getFirstAirDate().substring(0,4));
            }else {
                mHolder.year.setText("");
            }
        }
    }

    public void addList(List<SearchResult.Result> newList){
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(),newList.size()-1);
    }
    public void addLoadingFooter(){
        isLoading = true;
        list.add(new SearchResult.Result());
        notifyItemInserted(getItemCount() - 1);
    }
    public void removeFooter(){
        isLoading = false;
        int position = list.size() - 1;
        SearchResult.Result item = list.get(position);
        if(item != null){
            list.remove(item);
            notifyItemRemoved(position);
        }
    }
    @Override
    public int getItemCount() {
        return list != null ?list.size() : 0;
    }

    class SearchHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView title, type, overView,year;
        public SearchHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_search);
            title = itemView.findViewById(R.id.title_search);
            type = itemView.findViewById(R.id.type_search);
            overView = itemView.findViewById(R.id.overView_search);
            year = itemView.findViewById(R.id.year_search);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(getAdapterPosition()).getMediaType().equals("tv")){
                        startActivity(TvDetailsActivity.class);
                    }else if (list.get(getAdapterPosition()).getMediaType().equals("movie")){
                      startActivity(MovieDetailsActivity.class);
                    }
                }
            });

        }
        private void startActivity(Class c){
            Intent i = new Intent(context,c);
            i.putExtra(Constanc.OPENT_DETAILS_KEY,list.get(getAdapterPosition()).getId());
            context.startActivity(i);
        }
    }
    class SearchLoading extends RecyclerView.ViewHolder {
        public SearchLoading(View itemView) {
            super(itemView);
        }
    }
}
