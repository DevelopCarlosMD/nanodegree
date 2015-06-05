package com.leo.nanodegree.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.leo.nanodegree.R;
import com.leo.nanodegree.fragments.TopArtistSongsFragment;

public class TopArtistSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_artist_songs);

        if(getIntent().getExtras() != null){
            TopArtistSongsFragment topArtistSongsFragment = (TopArtistSongsFragment) getSupportFragmentManager().findFragmentById(R.id.top_artist_songs_fragment);
            topArtistSongsFragment.showArtistTopTrack(getIntent().getExtras().getString("artist_id"));
        }
    }
}
