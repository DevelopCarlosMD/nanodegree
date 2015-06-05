package com.leo.nanodegree.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.leo.nanodegree.R;
import com.leo.nanodegree.adapters.TopArtistSongsAdapter;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by leo on 6/4/15.
 */
public class TopArtistSongsFragment extends Fragment {

    private TopArtistSongsAdapter topArtistSongsAdapter;
    public TopArtistSongsFragment newInstance(){
        return new TopArtistSongsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topArtistSongsAdapter = new TopArtistSongsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.top_arttist_songs_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView topArtistSongsLits = (ListView)view.findViewById(R.id.top_artist_songs_list);
        topArtistSongsLits.setAdapter(topArtistSongsAdapter);

    }
    public void showArtistTopTrack(String artistId){
        getArtistTopTrack(artistId);
    }

    private void getArtistTopTrack(String artistId){
        SpotifyApi spotifyApi = new SpotifyApi();
        SpotifyService spotifyService = spotifyApi.getService();
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("country","SE");
        spotifyService.getArtistTopTrack(artistId,parameters,new Callback<Tracks>() {
            @Override
            public void success(final Tracks tracks, Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topArtistSongsAdapter.setDataToAdapter(tracks.tracks);
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}