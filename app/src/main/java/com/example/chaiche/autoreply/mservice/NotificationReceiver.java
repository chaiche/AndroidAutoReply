package com.example.chaiche.autoreply.mservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.example.chaiche.autoreply.model.Config;
import com.example.chaiche.autoreply.model.NotificationModel;

import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unused")
public class NotificationReceiver extends NotificationListenerService {

    private static final String TAG_WEARABLE = "android.wearable.EXTENSIONS";
    private static final String TAG_ACTIONS = "actions";

    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        super.onNotificationPosted(notification);

        Bundle bundle = notification.getNotification().extras;
        String packageName = notification.getPackageName();
        String name = bundle.getString("android.title", "");
        String message = bundle.getString("android.text", "");

        Log.i("1", "packageName:" + packageName + ", title:" + name + ", message:" + message);
        if (filter(packageName, name, message)) {
            NotificationModel model = new NotificationModel();
            model.setPackageName(packageName);
            model.setName(name);
            model.setMessage(message);
            model.setBitmap(notification.getNotification().largeIcon);
            model.setTimeStamp(System.currentTimeMillis());

            for (String s : bundle.keySet()) {
                if (TAG_WEARABLE.equals(s)) {
                    Bundle bundle2 = ((Bundle) bundle.get(s));
                    for (String s2 : Objects.requireNonNull(bundle2).keySet()) {
                        Object object = bundle2.get(s2);
                        if (s2 != null && object != null) {
                            if (TAG_ACTIONS.equals(s2) && object instanceof ArrayList) {
                                ArrayList<Notification.Action> actions = new ArrayList<>((ArrayList) object);
                                for (Notification.Action action : actions) {
                                    if (action.getRemoteInputs() != null) {
                                        model.setAction(action.actionIntent);
                                        model.setBundle(action.getExtras());
                                        model.setRemoteInputs(action.getRemoteInputs());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            NotificationService.getInstance().receive(model);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
    }

    private boolean filter(String packageName, String name, String text) {

        if (Config.RECEIVE) {
            if ("jp.naver.line.android".equals(packageName)) {
                return filterLine(name, text);
            } else if ("com.facebook.orca".equals(packageName)) {
                return filterFb(name, text);
            }
        }
        return false;
    }

    private boolean filterLine(String name, String text) {
        if (!TextUtils.isEmpty(name)
                && !name.equals("LINE")
                && !text.equals("您有新訊息")
                && !text.contains("傳送了照片")
                && !text.contains("LINE語音通話來電中")
                && !text.contains("LINE未接來電")
                && Config.RECEIVE_LINE) {
            return true;
        }
        return false;
    }

    private boolean filterFb(String name, String text) {
        if (!TextUtils.isEmpty(name)
                && !name.equals("聊天大頭貼使用中")
                && !text.equals("開始對話")
                && !text.contains("1個對話")
                && Config.RECEIVE_FB) {
            return true;
        }
        return false;
    }

}
