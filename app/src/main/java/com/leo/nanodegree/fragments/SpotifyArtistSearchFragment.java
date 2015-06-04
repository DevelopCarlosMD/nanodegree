package com.leo.nanodegree.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.leo.nanodegree.adapters.ArtistSearchAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by leo on 6/2/15.
 */
public class SpotifyArtistSearchFragment extends Fragment {

    private ArtistSearchAdapter artistSearchAdapter;

    public static SpotifyArtistSearchFragment newInstance() {
        return new SpotifyArtistSearchFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistSearchAdapter = new ArtistSearchAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spotify_search_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView artistAlbumsList = (ListView) view.findViewById(R.id.artist_search_list);
        artistAlbumsList.setAdapter(artistSearchAdapter);
        startArtistSearch((EditText) view.findViewById(R.id.search_spotify_streamer));

    }

    private void searchArtistAlbums(String artist) {

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

    private void startArtistSearch(final EditText artistSearcher){
        artistSearcher.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchArtistAlbums(artistSearcher.getText().toString());
                        hideKeyBoard(textView);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void hideKeyBoard(TextView textView){
        InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }
}
