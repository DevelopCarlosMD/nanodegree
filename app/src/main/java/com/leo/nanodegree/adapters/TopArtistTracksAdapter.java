package com.leo.nanodegree.adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by leo on 6/4/15.
 */
public class TopArtistTracksAdapter extends SpotifyBaseAdapter<Track> {

    @Override
    public View getView(int i, View view, @Nullable ViewGroup viewGroup) {
        TopArtistSongsHolder topArtistSongsHolder = new TopArtistSongsHolder();
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_artist_songs_row_layout, viewGroup, false);
            topArtistSongsHolder.artistImage = (ImageView) view.findViewById(R.id.artist_image);
            topArtistSongsHolder.artistSongName = (TextView) view.findViewById(R.id.artist_song_name);
            topArtistSongsHolder.artistAlbumName = (TextView) view.findViewById(R.id.artist_album_name);
            view.setTag(topArtistSongsHolder);
        } else {
            topArtistSongsHolder = (TopArtistSongsHolder) view.getTag();
        }

        Picasso.with(viewGroup.getContext())
                .load(getItems().get(i).album.images.get(1).url)
                .into(topArtistSongsHolder.artistImage);
        topArtistSongsHolder.artistSongName.setText(getItems().get(i).name);
        topArtistSongsHolder.artistAlbumName.setText(getItems().get(i).album.name);

        return view;
    }

    public static class TopArtistSongsHolder {
        public ImageView artistImage;
        public TextView artistSongName;
        public TextView artistAlbumName;
    }
}
