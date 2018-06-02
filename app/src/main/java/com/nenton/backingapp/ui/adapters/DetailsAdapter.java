package com.nenton.backingapp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.backingapp.R;
import com.nenton.backingapp.ui.fragments.DetailsFragment.OnDetailOrStepClickListener;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    private OnDetailOrStepClickListener mListener;
    private List<String> mDetails;
    private int mPosRecipe;

    public DetailsAdapter() {
    }

    public void swapAdapter(List<String> details, int posRecipe) {
        this.mDetails = details;
        this.mPosRecipe = posRecipe;
        notifyDataSetChanged();
    }

    public void setListener(OnDetailOrStepClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDetailOrStepSelected(mPosRecipe, holder.getAdapterPosition());
            }
        });
        holder.mTextView.setText(mDetails.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDetails == null) {
            return 0;
        }
        return mDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.recipe_tv);
        }
    }
}
