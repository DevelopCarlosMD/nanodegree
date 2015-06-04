package com.leo.nanodegree.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.leo.nanodegree.R;
import com.leo.nanodegree.fragments.SpotifyArtistSearchFragment;

public class SpotifyStreamerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_streamer);
        addSpotifyArtistSearchFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spotify_streamer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSpotifyArtistSearchFragment(){

        SpotifyArtistSearchFragment spotifyArtistSearchFragment = (SpotifyArtistSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(spotifyArtistSearchFragment == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SpotifyArtistSearchFragment.newInstance()).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,spotifyArtistSearchFragment).commit();
    }
}
