package com.tbs.tbsdisk.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tbsdisk.R;

/**
 * Created by TBS on 2017/8/10.
 */

public class MainDisk extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private SwipeRefreshLayout mSwipeLayout;
    private boolean isRefreshing = false;

    public static MainDisk newInstance(String info) {
        Bundle args = new Bundle();
        MainDisk fragment = new MainDisk();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.tab_disk, null);
        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swiper);
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setDistanceToTriggerSync(200);
        // 设定下拉圆圈的背景
        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
        //设置下拉刷新的监听
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setRefreshing(false);
        return mView;
    }
    public void refresh() {
        mSwipeLayout.setRefreshing(true);
    }


    @Override
    public void onRefresh() {

        if (!isRefreshing) {
            isRefreshing = true;
        }
        stopRefreshing();
        return;
    }

    public void stopRefreshing() {
        mSwipeLayout.setRefreshing(false);
        isRefreshing = false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
