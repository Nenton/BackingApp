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

public class IngredientsFragment extends Fragment {
    private IngredientsAdapter mAdapter;


    public IngredientsFragment() {
        mAdapter = new IngredientsAdapter();
    }

    public void setIngredients(RecipeRealm recipeRealm) {
        mAdapter.swapAdapter(recipeRealm.getIngredients());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.ingredients_rv);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        return view;
    }
}
