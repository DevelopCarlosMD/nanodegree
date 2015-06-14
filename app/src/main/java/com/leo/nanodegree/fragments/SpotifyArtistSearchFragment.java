package com.leo.nanodegree.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.leo.nanodegree.R;
import com.leo.nanodegree.adapters.ArtistSearchAdapter;
import com.leo.nanodegree.ui.TopArtistSongsActivity;
import com.leo.nanodegree.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executors;


import butterknife.InjectView;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

/**
 * Created by leo on 6/2/15.
 */
public class SpotifyArtistSearchFragment extends BaseListFragment {

    @InjectView(R.id.artist_search_error_text)
    TextView errorText;
    @InjectView(R.id.artist_search_list)
    ListView artistAlbumsList;

    private ArtistSearchAdapter artistSearchAdapter;

    public static SpotifyArtistSearchFragment newInstance() {
        return new SpotifyArtistSearchFragment();
    }

    @Override
    protected void onCreateFragment(Bundle savedInstanceState) {
        super.onCreateFragment(savedInstanceState);
        artistSearchAdapter = new ArtistSearchAdapter();
    }

    @Override
    protected boolean activateRetainInstance() {
        return true;
    }

    @Override
    protected int fragmentLayoutResource() {
        return R.layout.spotify_search_fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new GsonBuilder().create();
        Type artistAdapterType = new TypeToken<List<Artist>>() {
        }.getType();
        String adapterItems = gson.toJson(artistSearchAdapter.getItems(), artistAdapterType);
        outState.putString("adapter_items", adapterItems);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artistAlbumsList.setAdapter(artistSearchAdapter);
        startArtistSearch((EditText) view.findViewById(R.id.search_spotify_streamer), view.getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getString("adapter_items") != null) {
                Gson gson = new GsonBuilder().create();
                Type artistAdapterType = new TypeToken<List<Artist>>() {
                }.getType();
                List<Artist> artistList = gson.fromJson(savedInstanceState.getString("adapter_items"), artistAdapterType);
                artistSearchAdapter.setItems(artistList);
            }
        }
    }

    private void searchArtistAlbums(Context context, String artist) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.downloading_title));

        SpotifyApi spotifyApi = new SpotifyApi(Executors.newSingleThreadExecutor(), new MainThreadExecutor());

        SpotifyService spotifyService = spotifyApi.getService();
        spotifyService.searchArtists(artist, new Callback<ArtistsPager>() {
            @Override
            public void success(final ArtistsPager artistsPager, Response response) {

                if (response.getStatus() == 200) {
                    if (artistsPager != null && getActivity() != null) {
                        if (artistsPager.artists.items.size() > 0) {
                            errorText.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            artistSearchAdapter.setItems(artistsPager.artists.items);

                        } else {
                            artistSearchAdapter.clearData();
                            progressDialog.dismiss();
                            errorText.setVisibility(View.VISIBLE);
                        }
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

    private void startArtistSearch(final EditText artistSearcher, final Context context) {
        artistSearcher.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchArtistAlbums(context, artistSearcher.getText().toString());
                        Utils.hideKeyBoard(textView);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @OnItemClick(R.id.artist_search_list)
    public void onItemClick(@Nullable AdapterView<?> adapterView, @Nullable View view, int i, long l) {
        Artist artist = (Artist) adapterView.getAdapter().getItem(i);
        Intent topArtistSongsIntent = new Intent(getActivity(), TopArtistSongsActivity.class);
        topArtistSongsIntent.putExtra("artist_id", artist.id);
        startActivity(topArtistSongsIntent);
    }


}
