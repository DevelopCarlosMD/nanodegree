package com.leo.nanodegree.adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by leo on 6/4/15.
 */
public class TopArtistSongsAdapter extends BaseAdapter {

    private List<Track> topTracks;

    public TopArtistSongsAdapter() {
        topTracks = new ArrayList<>();
    }

    public TopArtistSongsAdapter(List<Track> topTracks) {
        this.topTracks = topTracks;
    }

    @Override
    public int getCount() {
        return topTracks == null?0:topTracks.size();
    }

    @Override
    public Object getItem(int i) {
        return topTracks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return topTracks.indexOf(topTracks.get(i));
    }

    @Override
    public View getView(int i, View view,@Nullable ViewGroup viewGroup) {
        TopArtistSongsHolder topArtistSongsHolder = new TopArtistSongsHolder();
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_artist_songs_row_layout,viewGroup,false);
            topArtistSongsHolder.artistImage = (ImageView)view.findViewById(R.id.artist_image);
            topArtistSongsHolder.artistSongName = (TextView)view.findViewById(R.id.artist_song_name);
            topArtistSongsHolder.artistAlbumName = (TextView)view.findViewById(R.id.artist_album_name);
            view.setTag(topArtistSongsHolder);
        }else{
            topArtistSongsHolder = (TopArtistSongsHolder)view.getTag();
        }

        Picasso.with(viewGroup.getContext())
                .load(topTracks.get(i).album.images.get(1).url)
                .into(topArtistSongsHolder.artistImage);
        topArtistSongsHolder.artistSongName.setText(topTracks.get(i).name);
        topArtistSongsHolder.artistAlbumName.setText(topTracks.get(i).album.name);

        return view;
    }

    public void setDataToAdapter(List<Track> topTracks){
        clearAllData();
        if(topTracks != null) {
            this.topTracks.addAll(topTracks);
            notifyDataSetChanged();
        }
    }

    public void clearAllData(){
        if(this.topTracks != null && this.topTracks.size() > 0)
            this.topTracks.clear();
    }

    public static class TopArtistSongsHolder{
        public ImageView artistImage;
        public TextView artistSongName;
        public TextView artistAlbumName;
    }
}
