package com.pktworld.chatdemo.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;
import com.pktworld.chatdemo.R;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    Context mContext;

    public Utils(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            showToastMessage(context,"No Internet Connection..!");
        return false;
    }

    /*send push notification*/
    public static void sendPush(String msg, String objectId) {
        ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", objectId);
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(msg);
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Notification Send");
                }
            }
        });

    }
    public static void showToastMessage(Context mContext, String msg) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View layout = li.inflate(R.layout.custom_toast, null);
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtToast);
        txtMsg.setText(msg);
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
