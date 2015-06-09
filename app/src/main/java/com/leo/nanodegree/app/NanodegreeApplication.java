package com.leo.nanodegree.app;

import android.app.Application;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Copyright;
import kaaes.spotify.webapi.android.models.Followers;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.LinkedTrack;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;

/**
 * Created by leo on 6/7/15.
 */
@ParcelClasses(value = {
        @ParcelClass(value = Pager.class),
        @ParcelClass(Artist.class),
        @ParcelClass(value = TrackSimple.class),
        @ParcelClass(Track.class),
        @ParcelClass(Followers.class),
        @ParcelClass(AlbumSimple.class),
        @ParcelClass(ArtistSimple.class),
        @ParcelClass(LinkedTrack.class),
        @ParcelClass(Album.class),
        @ParcelClass(Image.class),
        @ParcelClass(Copyright.class),
        @ParcelClass(Object.class)
        })
public class NanodegreeApplication extends Application {
}
