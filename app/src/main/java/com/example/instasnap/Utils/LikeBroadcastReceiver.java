package com.example.instasnap.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class LikeBroadcastReceiver extends BroadcastReceiver {

    private static LikeBroadcastReceiver receiver;

    public static LikeBroadcastReceiver getInstance(){
        if(receiver == null) {
            receiver = new LikeBroadcastReceiver();
            return receiver;
        }
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Your post received a new like",Toast.LENGTH_SHORT).show();
    }
}
