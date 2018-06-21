package com.example.chaiche.autoreply;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;

public abstract class BaseActivity extends AppCompatActivity{

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResource());
        ButterKnife.bind(this);

        mContext = this;
        initial(savedInstanceState);
    }

    protected Observable<Object> onClicks(@NonNull View view) {
        return RxView.clicks(view)
                .throttleFirst(600, TimeUnit.MILLISECONDS);
    }

    protected Observable<Object> onLongClick(@NonNull View view) {
        return RxView.longClicks(view)
                .throttleFirst(600, TimeUnit.MILLISECONDS);
    }

    protected abstract int getResource();
    protected abstract void initial(Bundle savedInstanceState);
}
