package com.leo.nanodegree.ui;

import android.support.annotation.Nullable;
import android.os.Bundle;


import com.leo.nanodegree.R;

public class SpotifyPlayerActivity extends BaseActivity {

    @Override
    int contentViewId() {
        return R.layout.activity_spotify_player;
    }

    @Override
    boolean activeBackButton() {
        return true;
    }

    @Override
    void onCreateActivity(@Nullable Bundle savedInstanceState) {

    }
}
