package com.example.chaiche.autoreply.mservice;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.example.chaiche.autoreply.model.Config;
import com.example.chaiche.autoreply.model.NotificationModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NotificationService {

    private static NotificationService mService;
    private static Context mContext;
    private Consumer<NotificationModel> mReceive = n -> {

    };

    public static void initialize(Context context) {
        if (mService == null) {
            mService = new NotificationService();
            mContext = context;
        }
    }

    public static NotificationService getInstance() {
        return mService;
    }

    @SuppressLint("CheckResult")
    public void receive(NotificationModel model) {
        Observable.just(model)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(m -> reply(model, Config.REPLY_TEXT))
                .subscribe(m -> mReceive.accept(m));
    }

    public void onReceive(Consumer<NotificationModel> receive) {
        this.mReceive = receive;
    }

    @SuppressLint("CheckResult")
    public void reply(NotificationModel model, String text) {
        Observable.just(text)
                .filter(a -> mContext != null)
                .filter(a -> !TextUtils.isEmpty(text))
                .subscribeOn(Schedulers.newThread())
                .doOnNext(s -> {
                    RemoteInput[] remoteInputs = model.getRemoteInputs();
                    Bundle bundle = model.getBundle();
                    PendingIntent action = model.getAction();
                    for (RemoteInput remoteInput : remoteInputs) {
                        bundle.putCharSequence(remoteInput.getResultKey(), s);
                    }
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    RemoteInput.addResultsToIntent(remoteInputs, intent, bundle);
                    action.send(mContext, 0, intent);
                })
                .onErrorReturn(Throwable::getMessage)
                .subscribe();
    }


}
