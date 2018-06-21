package com.example.chaiche.autoreply;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.chaiche.autoreply.model.Config;
import com.example.chaiche.autoreply.mservice.NotificationService;
import com.example.chaiche.autoreply.mservice.SharedPreference;

import static java.security.AccessController.getContext;

@SuppressWarnings("unused")
public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Config.RECEIVE = (Boolean) SharedPreference.getSubscribeBooleanValue(this, SharedPreference.RECEIVE).get();
        Config.RECEIVE_LINE = (Boolean) SharedPreference.getSubscribeBooleanValue(this, SharedPreference.RECEIVE_LINE).get();
        Config.RECEIVE_FB = (Boolean) SharedPreference.getSubscribeBooleanValue(this, SharedPreference.RECEIVE_FB).get();
        Config.REPLY_TEXT = (String) SharedPreference.getSubscribeStringValue(this, SharedPreference.REPLY_TEXT).get();
        NotificationService.initialize(this);
    }
}
