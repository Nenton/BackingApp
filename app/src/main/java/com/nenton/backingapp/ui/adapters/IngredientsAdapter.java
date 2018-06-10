package com.nenton.backingapp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.IngredientDto;
import com.nenton.backingapp.data.storage.realm.IngredientRealm;

import java.util.List;

import io.realm.RealmList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<IngredientDto> mIngredients;

    public IngredientsAdapter() {
    }

    public void swapAdapter(List<IngredientDto> ingredients) {
        this.mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientDto ingredient = mIngredients.get(position);
        if (ingredient != null) {
            String text = ingredient.getIngredient() + " "
                    + ingredient.getQuantity() + " "
                    + ingredient.getMeasure();
            holder.textView.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ingredient_tv);
        }
    }
}
