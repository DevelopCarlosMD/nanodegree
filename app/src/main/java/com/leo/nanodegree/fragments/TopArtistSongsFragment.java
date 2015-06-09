package com.leo.nanodegree.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.leo.nanodegree.adapters.TopArtistTracksAdapter;

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

    private TopArtistTracksAdapter topArtistSongsAdapter;
    private TextView errorText;

    public TopArtistSongsFragment newInstance() {
        return new TopArtistSongsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topArtistSongsAdapter = new TopArtistTracksAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.top_arttist_songs_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView topArtistSongsLits = (ListView) view.findViewById(R.id.top_artist_songs_list);
        topArtistSongsLits.setAdapter(topArtistSongsAdapter);
        errorText = (TextView) view.findViewById(R.id.top_track_error_text);

    }

    public void showArtistTopTrack(String artistId) {
        getArtistTopTrack(artistId);
    }

    private void getArtistTopTrack(String artistId) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.downloading_title));

        SpotifyApi spotifyApi = new SpotifyApi();
        SpotifyService spotifyService = spotifyApi.getService();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("country", "SE");
        spotifyService.getArtistTopTrack(artistId, parameters, new Callback<Tracks>() {
            @Override
            public void success(final Tracks tracks, Response response) {

                if (response.getStatus() == 200) {
                    if (tracks != null && getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                errorText.setVisibility(View.GONE);
                                topArtistSongsAdapter.setItems(tracks.tracks);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                topArtistSongsAdapter.clearData();
                                progressDialog.dismiss();
                                errorText.setVisibility(View.VISIBLE);

                            }
                        });

                    }
                }else{
                    progressDialog.dismiss();
                    errorText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        errorText.setVisibility(View.VISIBLE);

                    }
                });
            }
        });
    }
}
