package com.leo.nanodegree.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.leo.nanodegree.R;
import com.leo.nanodegree.utils.Utils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    //TODO: Create the string messages in string file

    public void onStartSpotifyStreamer(View view) {
        Intent spotifySearch = new Intent(MainActivity.this,SpotifyStreamerActivity.class);
        startActivity(spotifySearch);
    }

    public void onStartScoresApp(View view) {
        Utils.showToast(MainActivity.this,"Scores App", Toast.LENGTH_SHORT);
    }

    public void onStartLibraryApp(View view) {
        Utils.showToast(MainActivity.this,"Library App", Toast.LENGTH_SHORT);
    }

    public void onStartBuildItBetter(View view) {
        Utils.showToast(MainActivity.this,"Build it better", Toast.LENGTH_SHORT);
    }

    public void onStartXYZReader(View view) {
        Utils.showToast(MainActivity.this,"XYZ Reader", Toast.LENGTH_SHORT);
    }

    public void onStartMyOwnApp(View view) {
        Utils.showToast(MainActivity.this,"My own app", Toast.LENGTH_SHORT);
    }
}
