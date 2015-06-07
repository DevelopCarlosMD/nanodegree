package com.leo.nanodegree.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leo.nanodegree.R;

public class SearcherActivity extends BaseActivity {

    @Override
    int contentViewId() {
        return R.layout.activity_spotify_streamer;
    }

    @Override
    boolean activeBackButton() {
        return false;
    }

    @Override
    void onCreateActivity(@Nullable Bundle savedInstanceState) {
    }


}
