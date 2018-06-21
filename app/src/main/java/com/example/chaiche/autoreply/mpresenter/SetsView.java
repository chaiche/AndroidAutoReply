package com.example.chaiche.autoreply.mpresenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.chaiche.autoreply.BaseFragment;
import com.example.chaiche.autoreply.R;
import com.example.chaiche.autoreply.model.Config;
import com.example.chaiche.autoreply.mservice.SharedPreference;
import com.f2prateek.rx.preferences2.Preference;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SetsView extends BaseFragment {

    @BindView(R.id.switch_line) Switch mSwitch_line;
    @BindView(R.id.switch_fb) Switch mSwitch_fb;

    private Preference<Boolean> mLinePrefs;
    private Preference<Boolean> mFbPrefs;

    @SuppressWarnings("unchecked")
    @Override
    protected void initial(Bundle savedInstanceState, View view) {

        mLinePrefs = SharedPreference.getSubscribeBooleanValue(getContext(), SharedPreference.RECEIVE_LINE);
        mFbPrefs = SharedPreference.getSubscribeBooleanValue(getContext(), SharedPreference.RECEIVE_FB);

        mSwitch_line.setChecked(mLinePrefs.get());
        mSwitch_fb.setChecked(mFbPrefs.get());

        mSwitch_line.setOnCheckedChangeListener((compoundButton, b) -> {
            Observable.just(b)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(c -> {
                        mLinePrefs.set(c);
                        Config.RECEIVE_LINE = c;
                    });
        });

        mSwitch_fb.setOnCheckedChangeListener((compoundButton, b) -> {
            Observable.just(b)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(c -> {
                        mFbPrefs.set(c);
                        Config.RECEIVE_FB = c;
                    });
        });
    }

    @Override
    protected int getResource() {
        return R.layout.fragment_sets;
    }
}
