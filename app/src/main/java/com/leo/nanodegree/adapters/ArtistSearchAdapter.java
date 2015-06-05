package com.leo.nanodegree.adapters;

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


import kaaes.spotify.webapi.android.models.Artist;



/**
 * Created by leo on 6/2/15.
 */
public class ArtistSearchAdapter extends BaseAdapter {

    private List<Artist> artists;

    public ArtistSearchAdapter(List<Artist> artists) {
        this.artists = artists;
    }

    public ArtistSearchAdapter(){
        artists = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return artists == null?0:artists.size();
    }

    @Override
    public Object getItem(int i) {
        return artists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return artists.indexOf(artists.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        artistsSearchHolder artistsSearchHolder = new artistsSearchHolder();
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spotify_streamer_row_layout,viewGroup,false);
            artistsSearchHolder.artistsImage = (ImageView) view.findViewById(R.id.artist_image);
            artistsSearchHolder.artistsName = (TextView) view.findViewById(R.id.artist_name);
            view.setTag(artistsSearchHolder);
        }else{
            artistsSearchHolder = (artistsSearchHolder)view.getTag();
        }

        if(artists.get(i).images.size() >0) {
            Picasso.with(viewGroup.getContext())
                    .load(artists.get(i).images.get(1).url)
                    .resize(200, 200)
                    .into(artistsSearchHolder.artistsImage);
        }
        artistsSearchHolder.artistsName.setText(artists.get(i).name);

        return view;
    }

    public void setDataToAdapter(List<Artist> artists){
        clearAllData();
        if(artists != null) {
          this.artists.addAll(artists);
            notifyDataSetChanged();
        }
    }

    public void clearAllData(){
        if(this.artists != null && this.artists.size() > 0)
            this.artists.clear();
    }

    public static class artistsSearchHolder {
        public ImageView artistsImage;
        public TextView artistsName;
    }
}
