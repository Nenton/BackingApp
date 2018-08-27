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
import com.nenton.backingapp.data.storage.dto.DetailDto;
import com.nenton.backingapp.data.storage.dto.DetailDto.DetailType;
import com.nenton.backingapp.data.storage.dto.RecipeDto;
import com.nenton.backingapp.data.storage.dto.StepDto;
import com.nenton.backingapp.ui.adapters.DetailsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsFragment extends Fragment {
    public static final String RECIPE_KEY = "RECIPE_KEY";
    private DetailsAdapter mAdapter = new DetailsAdapter();
    private RecipeDto mRecipeRealm;

    public DetailsFragment() {
    }

    public void setDetailsAndSteps(RecipeDto recipeRealm) {
        mRecipeRealm = recipeRealm;
        mAdapter.swapAdapter(transferRecipeToList(mRecipeRealm));
    }

    private List<DetailDto> transferRecipeToList(RecipeDto recipeRealm) {
        List<DetailDto> details = new ArrayList<>();
        String textIngredients = "Ingredients on " + recipeRealm.getServings() + " portions";
        details.add(new DetailDto.Builder()
                .setType(DetailType.INGREDIENTS)
                .setText(textIngredients).build());
        int i = 1;
        for (StepDto step : recipeRealm.getSteps()) {
            DetailDto.Builder builder = new DetailDto.Builder();
            builder.setId(step.getId())
                    .setText("Step " + i + ": " + step.getShortDescription())
                    .setType(DetailType.STEP)
                    .setIsVideo(step.getVideoURL() != null && !step.getVideoURL().isEmpty());
            details.add(builder.build());
            i++;
        }
        return details;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(RECIPE_KEY, mRecipeRealm);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            OnDetailOrStepClickListener mListener = (OnDetailOrStepClickListener) context;
            Objects.requireNonNull(getActivity()).setTitle(mRecipeRealm.getName());
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

        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.details_rv);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)) {
            mRecipeRealm = ((RecipeDto) savedInstanceState.getSerializable(RECIPE_KEY));
        }

        if (mRecipeRealm != null) {
            mAdapter.swapAdapter(transferRecipeToList(mRecipeRealm));
        }
        return view;
    }

    public interface OnDetailOrStepClickListener {
        void onDetailOrStepSelected(DetailDto.DetailType type, int stepId, boolean haveVideo);
    }
}
