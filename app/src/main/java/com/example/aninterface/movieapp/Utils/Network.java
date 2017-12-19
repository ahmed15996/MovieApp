package com.example.aninterface.movieapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by interface on 04/12/2017.
 */

public class Network {
    public static boolean isNetworkConnection(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting()&&info.isConnected();

    }

    public static void snakBar(View v){
        Snackbar.make(v,"No Internet Connection",Snackbar.LENGTH_LONG).show();
    }
    public static void toast(Context context){
        Toast.makeText(context,"No Internet Connection", Toast.LENGTH_LONG).show();
    }
}
