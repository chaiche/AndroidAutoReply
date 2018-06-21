package com.example.chaiche.autoreply.mservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("unused")
public class SharedPreference {

    public static final String RECEIVE_LINE = "RECEIVE_LINE";
    public static final String RECEIVE_FB = "RECEIVE_FB";
    public static final String RECEIVE = "RECEIVE";
    public static final String REPLY_TEXT = "REPLY_TEXT";

    private static CompositeDisposable mDisposable;

    public static void subscribe(Context context, Disposable disposable) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    public static <T> Preference getSubscribeStringValue(Context context, String key) {
        return getSubscribeValue(context, key, String.class);
    }

    public static <T> Preference getSubscribeBooleanValue(Context context, String key) {
        return getSubscribeValue(context, key, Boolean.class);
    }

    public static <T> Preference getSubscribeValue(Context context, String key, Class<T> object) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

        if (object.equals(Integer.class)) {
            return rxPreferences.getInteger(key, 0);
        } else if (object.equals(Float.class)) {
            return rxPreferences.getFloat(key, -1f);
        } else if (object.equals(Boolean.class)) {
            return rxPreferences.getBoolean(key, false);
        }
        return rxPreferences.getString(key, "");
    }
}
