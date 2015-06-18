package com.leo.nanodegree.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leo.nanodegree.R;
import com.leo.nanodegree.fragments.SpotifyPlayerFragment;

public class SpotifyPlayerActivity extends BaseActivity {

    @Override
    int contentViewId() {
        return R.layout.activity_spotify_player;
    }

    @Override
    boolean activateBackButton() {
        return true;
    }

    @Override
    protected void onCreateActivity(@Nullable Bundle savedInstanceState) {

        if(savedInstanceState == null){
            if(getIntent().getExtras() != null) {
                    SpotifyPlayerFragment spotifyPlayerFragment = new SpotifyPlayerFragment();
                    spotifyPlayerFragment.onPlayTracks(getIntent().getExtras().getString("list_items"), getIntent().getExtras().getInt("track_selected_position"));
                    getSupportFragmentManager().beginTransaction().replace(R.id.spotify_player_fragment, spotifyPlayerFragment).commit();
                }
        }
    }
}
