package com.leo.nanodegree.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.leo.nanodegree.R;
import com.leo.nanodegree.interfaces.OnPlayTracksListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by leo on 6/7/15.
 */
public class SpotifyPlayerFragment extends BaseDialogFragment implements OnPlayTracksListener {

    @InjectView(R.id.player_artist_name)
    TextView playerArtistName;
    @InjectView(R.id.player_album_name)
    TextView playerAlbumName;
    @InjectView(R.id.player_album_image)
    ImageView playerAlbumImage;
    @InjectView(R.id.player_track_name)
    TextView playeTrackName;

    private List<Track> trackList;
    private int trackListPosition = 0;

    @Override
    public int fragmentLayoutResource() {
        return R.layout.spotify_player_fragment;
    }

    @Override
    public boolean activateRetainInstance() {
        return true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onPlayTracks(String tracks, int trackSelectedPosition) {
        if(tracks != null){
            Gson gson = new GsonBuilder().create();
            Type trackAdapterType = new TypeToken<List<Track>>(){}.getType();
            trackList = gson.fromJson(tracks,trackAdapterType);
            trackListPosition = trackSelectedPosition;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new GsonBuilder().create();
        Type trackAdapterType = new TypeToken<List<Track>>(){}.getType();
        outState.putString("track_list", gson.toJson(trackList, trackAdapterType));
        outState.putInt("track_list_position",trackListPosition);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(trackList != null) {
            playerArtistName.setText(trackList.get(trackListPosition).artists.get(0).name);
            playerAlbumName.setText(trackList.get(trackListPosition).album.name);
            setAlbumImage(trackList.get(trackListPosition).album.images.get(0).url, playerAlbumImage);
            playeTrackName.setText(trackList.get(trackListPosition).name);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            Gson gson = new GsonBuilder().create();
            Type trackAdapterType = new TypeToken<List<Track>>(){}.getType();
            trackList = gson.fromJson(savedInstanceState.getString("track_list"),trackAdapterType);
            trackListPosition = savedInstanceState.getInt("track_list_position",0);
        }
    }

    private void setAlbumImage(String imageUrl,ImageView imageView){
        if(imageUrl != null){
            Picasso.with(getActivity())
                    .load(imageUrl).resize(350,350)
                    .into(imageView);
        }
    }

}
