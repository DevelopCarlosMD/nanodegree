package com.leo.nanodegree.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import kaaes.spotify.webapi.android.models.AlbumSimple;

/**
 * Created by leo on 6/2/15.
 */
public class ArtistSearchAdapter extends BaseAdapter {

    private List<AlbumSimple> artistAlbums;

    public ArtistSearchAdapter(List<AlbumSimple> artistAlbums) {
        this.artistAlbums = artistAlbums;
    }

    public ArtistSearchAdapter(){
    }


    @Override
    public int getCount() {
        return artistAlbums == null?0:artistAlbums.size();
    }

    @Override
    public Object getItem(int i) {
        return artistAlbums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return artistAlbums.indexOf(artistAlbums.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ArtistSearchHolder artistSearchHolder = new ArtistSearchHolder();
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spotify_streamer_row_layout,viewGroup,false);
            artistSearchHolder.artistImage = (ImageView) view.findViewById(R.id.artist_image);
            artistSearchHolder.artistName = (TextView) view.findViewById(R.id.artist_name);
            view.setTag(artistSearchHolder);
        }else{
            artistSearchHolder = (ArtistSearchHolder)view.getTag();
        }

        Picasso.with(viewGroup.getContext())
                .load(artistAlbums.get(i).images.get(1).url)
                .resize(150, 150)
                .into(artistSearchHolder.artistImage);
        artistSearchHolder.artistName.setText(artistAlbums.get(i).name);

        return view;
    }

    public void setDataToAdapter(List<AlbumSimple> artistAlbums){
        if(artistAlbums != null && artistAlbums.size() > 0)
            artistAlbums.clear();
        this.artistAlbums = artistAlbums;
        notifyDataSetChanged();
    }
    public static class ArtistSearchHolder {
        public ImageView artistImage;
        public TextView artistName;
    }
}
