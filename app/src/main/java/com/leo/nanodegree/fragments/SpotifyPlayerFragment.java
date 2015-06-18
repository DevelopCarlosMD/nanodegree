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
import android.widget.ImageButton;
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
import butterknife.OnClick;
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
    @InjectView(R.id.play_track_button)
    ImageButton playButton;

    private List<Track> trackList;
    private int trackListPosition = 0;
    private SpotifyPlayerService spotifyPlayerService;
    private boolean isServiceBounded;
    private boolean isPlayerPlaying = false;
    private boolean isPlayerPaused = false;
    private int trackDuration = 0;
    private int trackCurrentPosition;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SpotifyPlayerService.SpotifyPlayerBinder playerBinder = (SpotifyPlayerService.SpotifyPlayerBinder) iBinder;
            spotifyPlayerService = playerBinder.getService();
            isServiceBounded = true;
            if (!isPlayerPlaying) {
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

    private final Handler playerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (trackDuration == 0) {
                setTrackDuration();
            }
            trackCurrentPosition = msg.getData().getInt(SpotifyPlayerService.CURRENT_TRACK_POSITION);
            trackProgress.setProgress(trackCurrentPosition);
            trackElapsedTime.setText("00:" + String.format("%02d", trackCurrentPosition));

            if (trackCurrentPosition == trackDuration && trackCurrentPosition != 0) {
                isPlayerPlaying = false;
                isPlayerPaused = false;
                trackCurrentPosition = 0;

            }
            if(isPlayerPlaying){
                playButton.setImageResource(android.R.drawable.ic_media_pause);
            }else{
                playButton.setImageResource(android.R.drawable.ic_media_play);
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

        if (tracks != null) {
            Gson gson = new GsonBuilder().create();
            Type trackAdapterType = new TypeToken<List<Track>>() {
            }.getType();
            trackList = gson.fromJson(tracks, trackAdapterType);
            trackListPosition = trackSelectedPosition;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new GsonBuilder().create();
        Type trackAdapterType = new TypeToken<List<Track>>() {
        }.getType();
        outState.putString("track_list", gson.toJson(trackList, trackAdapterType));
        outState.putInt("track_list_position", trackListPosition);
        outState.putInt("seek_progress", trackCurrentPosition);
        outState.putBoolean("is_player_playing", isPlayerPlaying);
        outState.putBoolean("is_player_paused", isPlayerPaused);
        outState.putBoolean("is_service_bounded", isServiceBounded);
        outState.putString("track_duration", spotifyPlayerService.getTrackDurationString());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            if (trackList != null) {
                setPlayerInfo(trackListPosition);
                startSpotifyService(trackList.get(trackListPosition).preview_url);
                playButton.setImageResource(android.R.drawable.ic_media_pause);

            }

            trackProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(@NonNull SeekBar seekBar, int i, boolean b) {
                    trackElapsedTime.setText("00:" + String.format("%02d", i));
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
        } else {
            setPlayerInfo(savedInstanceState.getInt("track_list_position"));
            trackCurrentPosition = savedInstanceState.getInt("seek_progress");
            isPlayerPaused = savedInstanceState.getBoolean("is_player_paused");
            isPlayerPlaying = savedInstanceState.getBoolean("is_player_playing");
            if (isPlayerPlaying)
                playButton.setImageResource(android.R.drawable.ic_media_pause);
            isServiceBounded = savedInstanceState.getBoolean("is_service_bounded");
            trackDurationView.setText(savedInstanceState.getString("track_duration"));
        }


    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @OnClick(R.id.play_track_button)
    public void playTrackAction(View view) {
        if (isPlayerPlaying) {
            playButton.setImageResource(android.R.drawable.ic_media_play);
            spotifyPlayerService.pauseTrack();
            isPlayerPaused = true;
            isPlayerPlaying = false;
        } else {
            playButton.setImageResource(android.R.drawable.ic_media_pause);
            spotifyPlayerService.playTrack(trackCurrentPosition);
            isPlayerPaused = true;
            isPlayerPlaying = true;
        }
    }

    @OnClick(R.id.next_track_button)
    public void nextTrackAction(View view) {
        trackListPosition = (trackListPosition + 1) % trackList.size();
        changeTrack(trackListPosition);
    }

    @OnClick(R.id.previuos_track_button)
    public void previousTrackAction(View view) {
        trackListPosition = trackListPosition - 1;
        if (trackListPosition < 0) {
            trackListPosition = trackList.size() - 1;
        }
        changeTrack(trackListPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroySpotifyService();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Gson gson = new GsonBuilder().create();
            Type trackAdapterType = new TypeToken<List<Track>>() {
            }.getType();
            trackList = gson.fromJson(savedInstanceState.getString("track_list"), trackAdapterType);
            trackListPosition = savedInstanceState.getInt("track_list_position", 0);
        }
    }

    private void setAlbumImage(String imageUrl, ImageView imageView) {
        if (imageUrl != null) {
            Picasso.with(getActivity())
                    .load(imageUrl).resize(350, 350)
                    .into(imageView);
        }
    }

    public void setTrackDuration() {
        if (spotifyPlayerService != null) {
            trackDuration = spotifyPlayerService.getTrackDuration();
            trackProgress.setMax(trackDuration);
            trackDurationView.setText(spotifyPlayerService.getTrackDurationString());
        }
    }

    public void resetTrackDuration() {
        trackProgress.setProgress(0);
        trackDurationView.setText("");
        trackElapsedTime.setText("");
        trackDuration = 0;
    }

    private void setPlayerInfo(int trackListPosition) {

        playerArtistName.setText(trackList.get(trackListPosition).artists.get(0).name);
        playerAlbumName.setText(trackList.get(trackListPosition).album.name);
        setAlbumImage(trackList.get(trackListPosition).album.images.get(0).url, playerAlbumImage);
        playeTrackName.setText(trackList.get(trackListPosition).name);

    }

    private void changeTrack(int trackListPosition) {

        isPlayerPlaying = true;
        isPlayerPaused = false;
        setPlayerInfo(trackListPosition);
        spotifyPlayerService.setTrackUrlPreview(trackList.get(trackListPosition).preview_url);
        spotifyPlayerService.noUpdateUI();
        spotifyPlayerService.playTrack(0);
        resetTrackDuration();

    }

    private void destroySpotifyService(){

        if (spotifyPlayerService != null) {
            spotifyPlayerService.noUpdateUI();
            if (isServiceBounded) {
                getActivity().getApplicationContext().unbindService(serviceConnection);
                isServiceBounded = false;
            }
        }
        if (!isPlayerPaused && !isPlayerPlaying) {
            getActivity().getApplicationContext().stopService(new Intent(getActivity(), SpotifyPlayerService.class));
        }
    }

    private void startSpotifyService(String trackUrl) {

        Intent spotifyServiceIntent = new Intent(getActivity(), SpotifyPlayerService.class);
        spotifyServiceIntent.putExtra(SpotifyPlayerService.TRACK_PREVIEW_URL, trackUrl);

        if (Utils.isServiceRunning(SpotifyPlayerService.class, getActivity()) && !isPlayerPlaying) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().stopService(spotifyServiceIntent);
            getActivity().getApplicationContext().startService(spotifyServiceIntent);
        } else if (!Utils.isServiceRunning(SpotifyPlayerService.class, getActivity())) {
            trackCurrentPosition = 0;
            getActivity().getApplicationContext().startService(spotifyServiceIntent);
        }
        if (spotifyPlayerService == null) {
            Log.d(SpotifyPlayerFragment.class.getSimpleName(), "" + isServiceBounded);
            getActivity().getApplicationContext().bindService(spotifyServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }
}
