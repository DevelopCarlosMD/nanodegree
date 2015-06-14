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
import kaaes.spotify.webapi.android.models.Artist;



/**
 * Created by leo on 6/2/15.
 */
public class ArtistSearchAdapter extends CommonAdapter<Artist> {

    @Override
    public View getView(int i, View view, @Nullable ViewGroup viewGroup) {

        ArtistsSearchHolder ArtistsSearchHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spotify_streamer_row_layout,viewGroup,false);
            ArtistsSearchHolder = new ArtistsSearchHolder(view);
            view.setTag(ArtistsSearchHolder);
        }else{
            ArtistsSearchHolder = (ArtistsSearchHolder)view.getTag();
        }

        if(getItems().get(i).images.size() >0 && getItems().get(i).images.get(1).url != null) {
            Picasso.with(viewGroup.getContext())
                    .load(getItems().get(i).images.get(1).url)
                    .resize(200, 200)
                    .into(ArtistsSearchHolder.artistsImage);
        }
        ArtistsSearchHolder.artistsName.setText(getItems().get(i).name);

        return view;
    }

    public static class ArtistsSearchHolder {
        @InjectView(R.id.artist_image) ImageView artistsImage;
        @InjectView(R.id.artist_name) TextView artistsName;

        public ArtistsSearchHolder(View view){
            ButterKnife.inject(this,view);
        }
    }
}
