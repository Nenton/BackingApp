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
import com.nenton.backingapp.data.storage.dto.RecipeDto;
import com.nenton.backingapp.ui.adapters.MasterRecipesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MasterRecipesFragment extends Fragment {
    private static final String RECIPES_KEY = "RECIPES_KEY";
    private List<RecipeDto> mRecipes;
    private MasterRecipesAdapter mAdapter = new MasterRecipesAdapter();


    public MasterRecipesFragment() {
    }

    public void setRecipes(List<RecipeDto> recipes) {
        this.mRecipes = recipes;
        mAdapter.swapAdapter(mRecipes);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(RECIPES_KEY, new ArrayList<>(mRecipes));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            OnRecipeClickListener mListener = (OnRecipeClickListener) context;
            Objects.requireNonNull(getActivity()).setTitle("Backing App");
            if (mAdapter != null) {
                mAdapter.setListener(mListener);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must be implemented OnRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recipes_rv);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_KEY)) {
            mRecipes = (ArrayList<RecipeDto>) savedInstanceState.getSerializable(RECIPES_KEY);
        }

        if (mRecipes != null){
            mAdapter.swapAdapter(mRecipes);
        }
        return view;
    }

    public interface OnRecipeClickListener {
        void onRecipeSelected(int id);
    }
}
