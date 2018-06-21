package com.example.chaiche.autoreply;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.chaiche.autoreply.mpresenter.MessageView;
import com.example.chaiche.autoreply.mpresenter.SetsView;
import com.example.chaiche.autoreply.mservice.NotificationService;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.Observable;

public class MainActivity extends BaseActivity {

    private final String[] mPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RxPermissions mRxPermissions;

    private FragmentPagerItemAdapter mAdapter;

    @BindView(R.id.smart_pager) SmartTabLayout mSmartPager;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    @Override
    protected int getResource() {
        return R.layout.activity_main;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initial(Bundle savedInstanceState) {
        mRxPermissions = new RxPermissions(this);
        initSmartPage();
    }

    @SuppressLint("CheckResult")
    private void initSmartPage() {

        Observable.just(mRxPermissions)
                .compose(mRxPermissions.ensureEach(mPermissions))
                .map(permission -> permission.granted)
                .contains(false)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        return;
                    }

                    FragmentPagerItems.Creator creator = FragmentPagerItems.with(mContext);
                    creator.add("主要", MessageView.class)
                            .add("接收的通訊軟體", SetsView.class);
                    mAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setCurrentItem(0);
                    mSmartPager.setViewPager(mViewPager);
                });
    }
}
