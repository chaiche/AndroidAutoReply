package com.example.chaiche.autoreply;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@SuppressWarnings("unused")
public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getResource(), null);
        ButterKnife.bind(this, view);

        mContext = getActivity();
        initial(savedInstanceState, view);

        return view;
    }

    protected abstract void initial(Bundle savedInstanceState, View view);
    protected abstract int getResource();

    protected Observable<Object> onClicks(@NonNull View view) {
        return RxView.clicks(view)
                .throttleFirst(600, TimeUnit.MILLISECONDS);
    }

    protected Observable<Object> onLongClick(@NonNull View view) {
        return RxView.longClicks(view)
                .throttleFirst(600, TimeUnit.MILLISECONDS);
    }

    protected Observable<String> onChanges(@NonNull TextView view) {
        return RxTextView.textChanges(view)
                .skip(1)
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(String::valueOf);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }
}
