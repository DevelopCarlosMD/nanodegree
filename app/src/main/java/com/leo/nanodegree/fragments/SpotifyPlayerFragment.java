package com.leo.nanodegree.fragments;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.leo.nanodegree.R;
import com.leo.nanodegree.interfaces.OnPlayTracksListener;
import com.leo.nanodegree.services.SpotifyPlayerService;
import com.leo.nanodegree.utils.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
    @InjectView(R.id.track_progress)
    SeekBar trackProgress;
    @InjectView(R.id.track_duration)
    TextView trackDurationView;
    @InjectView(R.id.track_time_start)
    TextView trackElapsedTime;

    private List<Track> trackList;
    private int trackListPosition = 0;
    private SpotifyPlayerService spotifyPlayerService;
    private boolean isServiceBounded = false;
    private boolean isPlayerPlaying = false;
    private boolean isPlayerPaused = false;
    private int trackDuration = 0;
    private int trackCurrentPosition;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SpotifyPlayerService.SpotifyPlayerBinder playerBinder = (SpotifyPlayerService.SpotifyPlayerBinder)iBinder;
            spotifyPlayerService = playerBinder.getService();
            isServiceBounded = true;
            if(!isPlayerPlaying){
                isPlayerPlaying = true;
            }
            setTrackDuration();
            spotifyPlayerService.setSpotifyPlayerHandler(playerHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBounded = false;
        }
    };

    private final Handler playerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(trackDuration == 0){
                setTrackDuration();
            }
            trackCurrentPosition = msg.getData().getInt(SpotifyPlayerService.CURRENT_TRACK_POSITION);
            trackProgress.setProgress(trackCurrentPosition);
            Log.d(SpotifyPlayerFragment.class.getSimpleName(), "" + trackProgress);
            trackElapsedTime.setText("00:" + String.format("%02d",trackCurrentPosition));

            if(trackCurrentPosition == trackDuration && trackCurrentPosition != 0){
                isPlayerPlaying = false;
                isPlayerPaused = false;
                trackCurrentPosition = 0;
            }

        }
    };



    @Override
    protected int fragmentLayoutResource() {
        return R.layout.spotify_player_fragment;
    }

    @Override
    protected boolean activateRetainInstance() {
        return true;
    }

    @Override
    protected void onCreateFragment(Bundle savedInstanceState) {
        super.onCreateFragment(savedInstanceState);
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
            startSpotifyService(trackList.get(trackListPosition).preview_url);
        }

        trackProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                trackElapsedTime.setText("00:" + String.format("%02d",i));
                trackCurrentPosition = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (isPlayerPlaying) {
                    spotifyPlayerService.noUpdateUI();
                }
            }

            @Override
            public void onStopTrackingTouch(@NonNull SeekBar seekBar) {
                trackCurrentPosition = seekBar.getProgress();
                if (spotifyPlayerService != null) {
                    spotifyPlayerService.toSeekTrack(trackCurrentPosition, isPlayerPaused);
                }
            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (spotifyPlayerService!= null) {
            spotifyPlayerService.noUpdateUI();
            if (isServiceBounded) {
                getActivity().unbindService(serviceConnection);
                isServiceBounded = false;
            }
        }
        if (!isPlayerPaused && !isPlayerPlaying) {
            getActivity().stopService(new Intent(getActivity(), SpotifyPlayerService.class));
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

    public void setTrackDuration(){
        if(spotifyPlayerService != null){
            trackDuration = spotifyPlayerService.getTrackDuration();
            trackProgress.setMax(trackDuration);
            trackDurationView.setText(spotifyPlayerService.getTrackDurationString());
        }
    }

    public void resetTrackDuration(){
        trackProgress.setProgress(0);
        trackDuration = 0;
    }

    private void startSpotifyService(String trackUrl){
        Intent spotifyServiceIntent = new Intent(getActivity(),SpotifyPlayerService.class);
        spotifyServiceIntent.putExtra(SpotifyPlayerService.TRACK_PREVIEW_URL,trackUrl);

        if (Utils.isServiceRunning(SpotifyPlayerService.class, getActivity()) && !isPlayerPlaying) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().stopService(spotifyServiceIntent);
            getActivity().getApplicationContext().startService(spotifyServiceIntent);
        }else if(!Utils.isServiceRunning(SpotifyPlayerService.class,getActivity())){
            trackCurrentPosition  = 0;
            getActivity().startService(spotifyServiceIntent);
        }

        if(spotifyPlayerService == null){
            getActivity().bindService(spotifyServiceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }
}
