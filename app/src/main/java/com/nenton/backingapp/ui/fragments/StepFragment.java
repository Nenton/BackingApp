package com.nenton.backingapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.realm.StepRealm;
import com.nenton.backingapp.utils.Playable;

public class StepFragment extends Fragment {
    private StepRealm mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private TextView description;

    public StepFragment() {
    }

    public void setStep(StepRealm step) {
        this.mStep = step;
        updateView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mExoPlayer = ((Playable) context).getPlayer();
        } catch (ClassCastException e) {
            throw new ClassCastException("Must be implemented Playable");
        }
        updateView();
    }

    private void updateView() {
        if (mStep != null && getContext() != null) {
            String backingApp = Util.getUserAgent(getContext(), "BackingApp");
            DefaultDataSourceFactory factory = new DefaultDataSourceFactory(getContext(), backingApp, new DefaultBandwidthMeter());
            MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(mStep.getVideoURL()));
            mExoPlayer.prepare(mediaSource);
        }
        if (description != null && mStep != null) {
            description.setText(mStep.getDescription());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step, container, false);
        description = view.findViewById(R.id.desc_step_tv);
        if (description != null && mStep != null) {
            description.setText(mStep.getDescription());
        }

        mExoPlayerView = view.findViewById(R.id.player_exo);
        mExoPlayerView.setPlayer(mExoPlayer);
        updateView();
        return view;
        // TODO: 02.06.2018 FullScreen
    }
}
