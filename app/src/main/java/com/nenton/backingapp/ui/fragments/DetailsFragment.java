package com.nenton.backingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.StepDto;
import com.nenton.backingapp.ui.adapters.DetailsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    private OnDetailOrStepClickListener mListener;
    private DetailsAdapter mAdapter;
    private LinearLayoutManager mManager;

    public DetailsFragment() {
        mAdapter = new DetailsAdapter();
    }

    public interface OnDetailOrStepClickListener {
        void onDetailOrStepSelected(int positionRecipe, int positionDetail);
    }

    public void setDetailsAndSteps(List<StepDto> steps, int servings, int positionRecipe) {
        List<String> details = new ArrayList<>();
        details.add("Ingredients on " + servings + " portions");
        int i = 1;
        for (StepDto step : steps) {
            details.add("Step " + i + ": " + step.getShortDescription());
            i++;
        }
        mAdapter.swapAdapter(details, positionRecipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDetailOrStepClickListener) context;
            if (mAdapter != null) {
                mAdapter.setListener(mListener);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must be implemented OnDetailOrStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        mManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.details_rv);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        return view;
    }
}
