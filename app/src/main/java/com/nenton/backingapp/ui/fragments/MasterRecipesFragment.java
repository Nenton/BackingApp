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
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.ui.adapters.MasterRecipesAdapter;

import java.util.List;

public class MasterRecipesFragment extends Fragment {

    private OnRecipeClickListener mListener;
    private MasterRecipesAdapter mAdapter;
    private LinearLayoutManager mManager;

    public MasterRecipesFragment() {
        mAdapter = new MasterRecipesAdapter();
    }

    public void setRecipes(List<RecipeRealm> recipes) {
        mAdapter.swapAdapter(recipes);
    }

    public interface OnRecipeClickListener {
        void onRecipeSelected(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnRecipeClickListener) context;
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


        mManager = new LinearLayoutManager(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.recipes_rv);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mManager);
        return view;
    }
}
