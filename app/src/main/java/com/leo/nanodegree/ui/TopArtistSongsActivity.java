package com.leo.nanodegree.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leo.nanodegree.R;
import com.leo.nanodegree.fragments.TopArtistSongsFragment;

public class TopArtistSongsActivity extends BaseActivity {

    @Override
    int contentViewId() {
        return R.layout.activity_top_artist_songs;
    }

    @Override
    boolean activateBackButton() {
        return true;
    }

    @Override
    protected void onCreateActivity(@Nullable Bundle savedInstanceState) {

        if(savedInstanceState == null) {
            if (getIntent().getExtras() != null) {
                TopArtistSongsFragment topArtistSongsFragment = (TopArtistSongsFragment) getSupportFragmentManager().findFragmentById(R.id.top_artist_songs_fragment);
                topArtistSongsFragment.onSearchArtistTopTracks(getIntent().getExtras().getString("artist_id"));
            }
        }
    }
}
