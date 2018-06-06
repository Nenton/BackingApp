package com.nenton.backingapp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.DetailDto;
import com.nenton.backingapp.ui.fragments.DetailsFragment.OnDetailOrStepClickListener;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    private OnDetailOrStepClickListener mListener;
    private List<DetailDto> mDetails;

    public DetailsAdapter() {
    }

    public void swapAdapter(List<DetailDto> details) {
        this.mDetails = details;
        notifyDataSetChanged();
    }

    public void setListener(OnDetailOrStepClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(view -> mListener.onDetailOrStepSelected(
                mDetails.get(holder.getAdapterPosition()).getType(),
                mDetails.get(holder.getAdapterPosition()).getId()));
        holder.mTextView.setText(mDetails.get(position).getText());
        if (mDetails.get(position).isVideo()){
            holder.mImageView.setVisibility(View.VISIBLE);
        }
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
        private ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.detail_tv);
            mImageView = itemView.findViewById(R.id.video_detail_iv);
        }
    }
}
