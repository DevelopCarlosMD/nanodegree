package com.leo.nanodegree.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.leo.nanodegree.R;

public abstract class SpotifyBaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(contentViewId());
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (activeBackButton()) {
                getSupportActionBar().setHomeButtonEnabled(activeBackButton());
                getSupportActionBar().setDisplayHomeAsUpEnabled(activeBackButton());
            }
        }

        onCreateActivity(savedInstanceState);
    }

    abstract int contentViewId();

    abstract boolean activeBackButton();

    abstract void onCreateActivity(@Nullable Bundle savedInstanceState);


}
