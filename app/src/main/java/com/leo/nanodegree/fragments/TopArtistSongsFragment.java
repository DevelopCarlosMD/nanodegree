package com.leo.nanodegree.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.leo.nanodegree.R;
import com.leo.nanodegree.adapters.TopArtistTracksAdapter;
import com.leo.nanodegree.interfaces.OnSearchArtistTopTracksListener;
import com.leo.nanodegree.ui.SpotifyPlayerActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

import butterknife.InjectView;
import butterknife.OnItemClick;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

/**
 * Created by leo on 6/4/15.
 */
public class TopArtistSongsFragment extends BaseListFragment implements OnSearchArtistTopTracksListener {

    @InjectView(R.id.top_track_error_text)
    TextView errorText;
    @InjectView(R.id.top_artist_songs_list)
    ListView topArtistSongsList;

    private TopArtistTracksAdapter topArtistSongsAdapter;

    public TopArtistSongsFragment newInstance() {
        return new TopArtistSongsFragment();
    }

    @Override
    protected void onCreateFragment(Bundle savedInstanceState) {
        super.onCreateFragment(savedInstanceState);
        topArtistSongsAdapter = new TopArtistTracksAdapter();
    }

    @Override
    protected int fragmentLayoutResource() {
        return R.layout.top_arttist_songs_fragment;
    }

    @Override
    protected boolean activateRetainInstance() {
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topArtistSongsList.setAdapter(topArtistSongsAdapter);
    }

    private void getArtistTopTrack(String artistId) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.downloading_title));

        SpotifyApi spotifyApi = new SpotifyApi(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
        SpotifyService spotifyService = spotifyApi.getService();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("country", Locale.getDefault().getCountry());
        spotifyService.getArtistTopTrack(artistId, parameters, new Callback<Tracks>() {
            @Override
            public void success(final Tracks tracks, Response response) {

                if (response.getStatus() == 200) {
                    if (tracks != null && getActivity() != null) {

                                progressDialog.dismiss();
                                errorText.setVisibility(View.GONE);
                                topArtistSongsAdapter.setItems(tracks.tracks);

                    } else {
                                topArtistSongsAdapter.clearData();
                                progressDialog.dismiss();
                                errorText.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressDialog.dismiss();
                    errorText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                        progressDialog.dismiss();
                        errorText.setVisibility(View.VISIBLE);

            }
        });
    }

    @OnItemClick(R.id.top_artist_songs_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent playerIntent = new Intent(getActivity(), SpotifyPlayerActivity.class);
        Gson gson = new GsonBuilder().create();
        Type trackAdapterType = new TypeToken<List<Track>>(){}.getType();
        playerIntent.putExtra("list_items",gson.toJson(((TopArtistTracksAdapter)adapterView.getAdapter()).getItems(),trackAdapterType));
        playerIntent.putExtra("track_selected_position",i);
        startActivity(playerIntent);
    }

    @Override
    public void onSearchArtistTopTracks(String artistId) {
        getArtistTopTrack(artistId);
    }
}
