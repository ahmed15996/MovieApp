package com.example.aninterface.movieapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InternetReceiver extends BroadcastReceiver {
    private InternetListener listener;

    public InternetReceiver() {
    }

    public InternetReceiver(InternetListener listener){
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(listener != null && Network.isNetworkConnection(context)){
            listener.networkConnected();
        }
    }
   public interface InternetListener{
        void networkConnected();
    }
}
