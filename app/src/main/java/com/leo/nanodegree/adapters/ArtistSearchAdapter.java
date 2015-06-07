package com.leo.nanodegree.adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.nanodegree.R;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;



/**
 * Created by leo on 6/2/15.
 */
public class ArtistSearchAdapter extends SpotifyBaseAdapter<Artist> {

    @Override
    public View getView(int i, View view, @Nullable ViewGroup viewGroup) {

        ArtistsSearchHolder ArtistsSearchHolder = new ArtistsSearchHolder();
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spotify_streamer_row_layout,viewGroup,false);
            ArtistsSearchHolder.artistsImage = (ImageView) view.findViewById(R.id.artist_image);
            ArtistsSearchHolder.artistsName = (TextView) view.findViewById(R.id.artist_name);
            view.setTag(ArtistsSearchHolder);
        }else{
            ArtistsSearchHolder = (ArtistsSearchHolder)view.getTag();
        }

        if(this.getItems().get(i).images.size() >0) {
            Picasso.with(viewGroup.getContext())
                    .load(getItems().get(i).images.get(1).url)
                    .resize(200, 200)
                    .into(ArtistsSearchHolder.artistsImage);
        }
        ArtistsSearchHolder.artistsName.setText(getItems().get(i).name);

        return view;
    }

    public static class ArtistsSearchHolder {
        public ImageView artistsImage;
        public TextView artistsName;
    }
}
