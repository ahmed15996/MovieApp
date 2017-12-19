package com.example.aninterface.movieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aninterface.movieapp.Model.CreditsModel;
import com.example.aninterface.movieapp.R;
import com.example.aninterface.movieapp.Utils.Constanc;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by interface on 04/12/2017.
 */

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.MovieCastHolder> {
    private Context context;
    private List<CreditsModel.CastModel> list;
    private int ITEM = 0, LOADING = 1;
    private boolean isLoadingAdd = false;

    public MovieCastAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public MovieCastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MovieCastHolder(inflater.inflate(R.layout.item_cast_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieCastHolder holder, int position) {
        final MovieCastHolder mHolder = (MovieCastHolder) holder;
        CreditsModel.CastModel model = list.get(position);
        if (model.getProfilePath() != null && model.getCharacter() != null && model.getName() != null) {
            mHolder.castName.setText(model.getName());
            mHolder.castAs.setText(model.getCharacter());
            Picasso.with(context).load(Constanc.IMAGE_BASE_URL + model.getProfilePath())
                    .placeholder(R.drawable.loading).into(holder.castImage, new Callback() {
                @Override
                public void onSuccess() {
                    if (mHolder.progressBar != null){
                        mHolder.progressBar.setVisibility(View.GONE);
                        mHolder.form.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() - 1 : 0;
    }

    public void addList(List<CreditsModel.CastModel> newList) {
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(), newList.size() - 1);
    }

    public CreditsModel.CastModel getItem(int position) {
        return list.get(position);
    }

    class MovieCastHolder extends RecyclerView.ViewHolder {
        private CircleImageView castImage;
        private TextView castName, castAs;
        private LinearLayout form;
        private ProgressBar progressBar;

        public MovieCastHolder(View itemView) {
            super(itemView);
            form = itemView.findViewById(R.id.castForm);
            progressBar = itemView.findViewById(R.id.progresbar_cast);
            castImage = itemView.findViewById(R.id.cast_image_movie);
            castName = itemView.findViewById(R.id.cast_name_movie);
            castAs = itemView.findViewById(R.id.cast_as_movie);
        }
    }
}
