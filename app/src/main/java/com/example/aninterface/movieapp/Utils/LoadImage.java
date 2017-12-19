package com.example.aninterface.movieapp.Utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.aninterface.movieapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by interface on 16/12/2017.
 */

public class LoadImage {
    public static void LoadImage(Context context, String path, final ImageView imageView, final ProgressBar progressBar){
        Picasso.with(context)
                .load(path)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar!= null){
                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
