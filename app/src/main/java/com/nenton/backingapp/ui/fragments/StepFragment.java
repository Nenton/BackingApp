package com.nenton.backingapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.StepDto;
import com.nenton.backingapp.utils.ExoEventListener;
import com.nenton.backingapp.utils.Playable;
import com.squareup.picasso.Picasso;

public class StepFragment extends Fragment {
    public static final String STEP_KEY = "STEP_KEY";
    public static final String POSITION_PLAYER_KEY = "POSITION_PLAYER_KEY";
    public static final String PLAY_KEY = "PLAY_KEY";
    private StepDto mStep;
    private SimpleExoPlayerView mExoPlayerView;
    private TextView description;
    private ImageView imageView;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long mPosition = 0;

    public StepFragment() {
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initPlayer(Context context) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
            mExoPlayer.addListener(new ExoEventListener(mStateBuilder, mExoPlayer, context));
        }
    }

    public void setStep(StepDto step) {
        this.mStep = step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(STEP_KEY, mStep);
        outState.putLong(POSITION_PLAYER_KEY, mExoPlayer.getCurrentPosition());
        outState.putBoolean(PLAY_KEY, mExoPlayer.getPlayWhenReady());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMediaSession = ((Playable) context).getMediaSession();
            mMediaSession.setCallback(new MySessionCallback());
            mStateBuilder = ((Playable) context).getStatePlayback();
            initPlayer(context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Must be implemented Playable");
        }
    }

    private void updateView() {
        if (mStep != null && getContext() != null && mStep.getVideoURL() != null && !mStep.getVideoURL().isEmpty()) {
            String backingApp = Util.getUserAgent(getContext(), "BackingApp");
            DefaultDataSourceFactory factory = new DefaultDataSourceFactory(getContext(), backingApp, new DefaultBandwidthMeter());
            MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(mStep.getVideoURL()));
            mExoPlayer.seekTo(mPosition);
            mExoPlayer.prepare(mediaSource);
        }
        if (mExoPlayerView != null && mStep != null) {
            if (!mStep.getVideoURL().isEmpty()) {
                mExoPlayerView.setVisibility(View.VISIBLE);
            } else {
                mExoPlayerView.setVisibility(View.GONE);
            }
        }
        if (description != null && mStep != null) {
            description.setText(mStep.getDescription());
        }
        if (imageView != null) {
            if (mStep != null && mStep.getThumbnailURL() != null &&
                    !mStep.getThumbnailURL().isEmpty() &&
                    !mStep.getThumbnailURL().contains(".mp4")) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(mStep.getThumbnailURL())
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step, container, false);
        description = view.findViewById(R.id.desc_step_tv);
        mExoPlayerView = view.findViewById(R.id.player_exo);
        imageView = view.findViewById(R.id.step_iv);
        mExoPlayerView.setPlayer(mExoPlayer);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STEP_KEY)) {
                mStep = ((StepDto) savedInstanceState.getSerializable(STEP_KEY));
            }
            if (savedInstanceState.containsKey(POSITION_PLAYER_KEY)) {
                mPosition = savedInstanceState.getLong(POSITION_PLAYER_KEY);
            }
            if (savedInstanceState.containsKey(PLAY_KEY)) {
                mExoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAY_KEY));
            }
        }
        updateView();
        return view;
        // TODO: 02.06.2018 FullScreen
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            releasePlayer();
        }
        mMediaSession.setActive(false);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            releasePlayer();
        }
        super.onStop();
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
