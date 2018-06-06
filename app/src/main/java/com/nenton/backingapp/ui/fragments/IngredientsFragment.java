package com.nenton.backingapp.ui.fragments;

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
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.ui.adapters.IngredientsAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class IngredientsFragment extends Fragment {
    public static final String INGREDIENTS_KEY = "INGREDIENTS_KEY";
    private IngredientsAdapter mAdapter = new IngredientsAdapter();
    private RecipeRealm mRecipeRealm;

    public IngredientsFragment() {
    }

    public void setIngredients(RecipeRealm recipeRealm) {
        this.mRecipeRealm = recipeRealm;
        mAdapter.swapAdapter(mRecipeRealm.getIngredients());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(INGREDIENTS_KEY, mRecipeRealm);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.ingredients_rv);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        if (savedInstanceState != null && savedInstanceState.containsKey(INGREDIENTS_KEY)) {
            mRecipeRealm = (RecipeRealm) savedInstanceState.getSerializable(INGREDIENTS_KEY);
        }
        if (mRecipeRealm != null){
            mAdapter.swapAdapter(mRecipeRealm.getIngredients());
        }
        return view;
    }
}
