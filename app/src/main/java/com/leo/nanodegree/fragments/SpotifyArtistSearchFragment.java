package com.leo.nanodegree.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.leo.nanodegree.R;
import com.leo.nanodegree.adapters.ArtistSearchAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by leo on 6/2/15.
 */
public class SpotifyArtistSearchFragment extends Fragment {

    private ArtistSearchAdapter artistSearchAdapter;
    public static SpotifyArtistSearchFragment newInstance(){
        return new SpotifyArtistSearchFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchArtistAlbums("eminem");
        artistSearchAdapter = new ArtistSearchAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spotify_search_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView artistAlbumsList = (ListView)view.findViewById(R.id.artist_search_list);
        artistAlbumsList.setAdapter(artistSearchAdapter);


    }

    private void searchArtistAlbums(String artist){

        SpotifyApi spotifyApi = new SpotifyApi();
        SpotifyService spotifyService = spotifyApi.getService();

        spotifyService.searchAlbums(artist, new Callback<AlbumsPager>() {
            @Override
            public void success(final AlbumsPager albumsPager, Response response) {
                Log.d(SpotifyArtistSearchFragment.class.getSimpleName(), albumsPager.albums.items.toString());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        artistSearchAdapter.setDataToAdapter(albumsPager.albums.items);

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
