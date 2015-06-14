package com.leo.nanodegree.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by leo on 6/13/15.
 */
public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(activateRetainInstance());
        onCreateFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View commonView = inflater.inflate(fragmentLayoutResource(), container, false);
        ButterKnife.inject(BaseDialogFragment.this, commonView);
        return commonView;
    }

    public int fragmentLayoutResource() {
        return 0;
    }

    public boolean activateRetainInstance() {
        return false;
    }

    public void onCreateFragment(Bundle savedInstanceState) {
    }


}
