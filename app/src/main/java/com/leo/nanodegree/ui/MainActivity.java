package com.leo.nanodegree.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.leo.nanodegree.R;
import com.leo.nanodegree.utils.Utils;


public class MainActivity extends SpotifyBaseActivity {

    @Override
    int contentViewId() {
        return R.layout.activity_main;
    }

    @Override
    boolean activeBackButton() {
        return false;
    }

    @Override
    void onCreateActivity(@Nullable Bundle savedInstanceState) {

    }


    //TODO: Create the string messages in string file
    public void onStartSpotifyStreamer(View view) {
        Intent spotifySearch = new Intent(MainActivity.this, SpotifySearcherActivity.class);
        startActivity(spotifySearch);
    }

    public void onStartScoresApp(View view) {
        Utils.showToast(MainActivity.this, "Scores App", Toast.LENGTH_SHORT);
    }

    public void onStartLibraryApp(View view) {
        Utils.showToast(MainActivity.this, "Library App", Toast.LENGTH_SHORT);
    }

    public void onStartBuildItBetter(View view) {
        Utils.showToast(MainActivity.this, "Build it better", Toast.LENGTH_SHORT);
    }

    public void onStartXYZReader(View view) {
        Utils.showToast(MainActivity.this, "XYZ Reader", Toast.LENGTH_SHORT);
    }

    public void onStartMyOwnApp(View view) {
        Utils.showToast(MainActivity.this, "My own app", Toast.LENGTH_SHORT);
    }

}
