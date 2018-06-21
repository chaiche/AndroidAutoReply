package com.example.chaiche.autoreply.mservice;

import android.content.Context;
import android.provider.Settings;

public class Tools {

    public static Boolean checkNotificationEnable(Context context) {
        if (Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners") != null) {
            if (Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners").contains(context.getApplicationContext().getPackageName())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
