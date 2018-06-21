package com.example.chaiche.autoreply.mpresenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chaiche.autoreply.BaseFragment;
import com.example.chaiche.autoreply.MainActivity;
import com.example.chaiche.autoreply.R;
import com.example.chaiche.autoreply.model.Config;
import com.example.chaiche.autoreply.mservice.SharedPreference;
import com.example.chaiche.autoreply.mservice.Tools;
import com.f2prateek.rx.preferences2.Preference;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MessageView extends BaseFragment {

    @BindView(R.id.btn_lock_service) Button btn_lock_service;
    @BindView(R.id.edt_input) EditText edt_input;
    private Disposable mDisposable;
    private Preference<Boolean> mServicePrefs;
    private Preference<String> mReplyTextPrefs;


    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    @Override
    protected void initial(Bundle savedInstanceState, View view) {

        mServicePrefs = SharedPreference.getSubscribeBooleanValue(getContext(), SharedPreference.RECEIVE);
        mReplyTextPrefs = SharedPreference.getSubscribeStringValue(getContext(), SharedPreference.REPLY_TEXT);
        showEnable(mServicePrefs.get());

        edt_input.setText(mReplyTextPrefs.get());
        onChanges(edt_input).subscribe(s->{
            mReplyTextPrefs.set(s);
            Config.REPLY_TEXT = s;
        });
    }

    @SuppressLint("CheckResult")
    private void showEnable(boolean b) {
        if (!b) {
            btn_lock_service.setText("啟動自動回覆");
            onClicks(btn_lock_service).subscribe(v -> {
                if (!Tools.checkNotificationEnable(getContext())) {
                    Observable.just(1)
                            .doOnNext(i -> {
                                Intent it = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                                startActivity(it);
                            })
                            .subscribe(i -> {
                                mDisposable = Observable.interval(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                                        .takeWhile(aLong -> !Tools.checkNotificationEnable(getContext()))
                                        .count()
                                        .subscribe(aLong -> {
                                            mServicePrefs.set(true);
                                            Config.RECEIVE = true;
                                            showEnable(true);
                                            Intent it = new Intent(getContext(), MainActivity.class);
                                            startActivity(it);
                                        });
                            });
                } else {
                    mServicePrefs.set(true);
                    Config.RECEIVE = true;
                    showEnable(true);
                }
            });
        } else {
            btn_lock_service.setText("關閉自動回覆");
            onClicks(btn_lock_service).subscribe(v -> {
                mServicePrefs.set(false);
                Config.RECEIVE = false;
                showEnable(false);
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }


    @Override
    protected int getResource() {
        return R.layout.fragment_message;
    }
}
