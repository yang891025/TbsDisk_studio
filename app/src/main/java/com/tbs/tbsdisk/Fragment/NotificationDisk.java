package com.tbs.tbsdisk.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbs.tbsdisk.R;

/**
 * Created by TBS on 2017/8/10.
 */

public class NotificationDisk extends Fragment
{
    public static NotificationDisk newInstance(String info) {
        Bundle args = new Bundle();
        NotificationDisk fragment = new NotificationDisk();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_notification, null);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
