package com.nenton.backingapp.utils;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.exoplayer2.SimpleExoPlayer;

public interface Playable {
    MediaSessionCompat getMediaSession();
    PlaybackStateCompat.Builder getStatePlayback();
}
