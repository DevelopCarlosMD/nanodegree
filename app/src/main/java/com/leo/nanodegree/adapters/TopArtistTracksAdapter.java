package com.leo.nanodegree.adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.squareup.picasso.Picasso;


import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by leo on 6/4/15.
 */
public class TopArtistTracksAdapter extends CommonAdapter<Track> {

    @Override
    public View getView(int i, View view, @Nullable ViewGroup viewGroup) {

        TopArtistSongsHolder topArtistSongsHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_artist_songs_row_layout, viewGroup, false);
            topArtistSongsHolder = new TopArtistSongsHolder(view);
            view.setTag(topArtistSongsHolder);
        } else {
            topArtistSongsHolder = (TopArtistSongsHolder) view.getTag();
        }

        if(getItems().get(i).album.images != null && getItems().get(i).album.images.get(1).url != null) {
            Picasso.with(viewGroup.getContext())
                    .load(getItems().get(i).album.images.get(1).url)
                    .into(topArtistSongsHolder.artistImage);
        }
        topArtistSongsHolder.artistSongName.setText(getItems().get(i).name);
        topArtistSongsHolder.artistAlbumName.setText(getItems().get(i).album.name);

        return view;
    }

    public static class TopArtistSongsHolder {
        @InjectView(R.id.artist_image) ImageView artistImage;
        @InjectView(R.id.artist_song_name) TextView artistSongName;
        @InjectView(R.id.artist_album_name) TextView artistAlbumName;

        public TopArtistSongsHolder(View view){
            ButterKnife.inject(this,view);
        }
    }
}
