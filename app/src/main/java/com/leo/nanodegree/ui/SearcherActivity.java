package com.leo.nanodegree.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leo.nanodegree.R;
import com.leo.nanodegree.fragments.TopArtistSongsFragment;
import com.leo.nanodegree.interfaces.OnSearchItemClickListener;

public class SearcherActivity extends BaseActivity implements OnSearchItemClickListener {

    @Override
    int contentViewId() {
        return R.layout.activity_spotify_streamer;
    }

    @Override
    boolean activateBackButton() {
        return false;
    }

    @Override
    public void onSearchItemClick(String artistId) {

        TopArtistSongsFragment topArtistSongsFragment = (TopArtistSongsFragment)getSupportFragmentManager().findFragmentById(R.id.top_artist_songs_fragment);
        if(topArtistSongsFragment == null){
            Intent topArtistSongsIntent = new Intent(SearcherActivity.this, TopArtistSongsActivity.class);
            topArtistSongsIntent.putExtra("artist_id", artistId);
            startActivity(topArtistSongsIntent);
        }else{
            topArtistSongsFragment.onSearchArtistTopTracks(artistId);
        }

    }
}
