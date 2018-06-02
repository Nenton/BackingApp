package com.nenton.backingapp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.pojo.Recipe;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment.OnRecipeClickListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterRecipesAdapter extends RecyclerView.Adapter<MasterRecipesAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private OnRecipeClickListener mListener;

    public MasterRecipesAdapter() {
    }

    public void swapAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void setListener(OnRecipeClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.recipeText.setText(recipes.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecipeSelected(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeText;

        ViewHolder(View itemView) {
            super(itemView);
            recipeText = itemView.findViewById(R.id.recipe_tv);
        }
    }
}
