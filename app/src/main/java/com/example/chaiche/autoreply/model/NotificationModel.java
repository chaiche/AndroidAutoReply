package com.example.chaiche.autoreply.model;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.graphics.Bitmap;
import android.os.Bundle;

public class NotificationModel {

    private String packageName;
    private String name;
    private String message;
    private PendingIntent action;
    private RemoteInput[] remoteInputs = null;
    private Bitmap bitmap;
    private Bundle bundle;
    private long timeStamp;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PendingIntent getAction() {
        return action;
    }

    public void setAction(PendingIntent action) {
        this.action = action;
    }

    public RemoteInput[] getRemoteInputs() {
        return remoteInputs;
    }

    public void setRemoteInputs(RemoteInput[] remoteInputs) {
        this.remoteInputs = remoteInputs;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
