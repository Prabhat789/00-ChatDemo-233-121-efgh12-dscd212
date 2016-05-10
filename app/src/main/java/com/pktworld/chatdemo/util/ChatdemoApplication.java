package com.pktworld.chatdemo.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pktworld.chatdemo.model.Group;
import com.pktworld.chatdemo.model.Message;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class ChatdemoApplication extends Application{

    private static ChatdemoApplication mInstance;
    private static Context mAppContext;

    public static ChatdemoApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        ChatdemoApplication.mAppContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.setAppContext(getApplicationContext());
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(ParseUser.class);
        ParseObject.registerSubclass(Group.class);
        Parse.enableLocalDatastore(getAppContext());
        Parse.initialize(getAppContext());
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        subscribeforpushnotification();

    }
    private void subscribeforpushnotification() {
        // TODO Auto-generated method stub
        ParsePush.subscribeInBackground("ChatDemo", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}
