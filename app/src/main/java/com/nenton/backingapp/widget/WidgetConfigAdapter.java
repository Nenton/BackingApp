package com.nenton.backingapp.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.WidgetDto;

import java.util.List;

public class WidgetConfigAdapter extends RecyclerView.Adapter<WidgetConfigAdapter.ViewHolder> {
    private List<WidgetDto> ingredients;
    private IngredientsConfigureActivity.Widgetable widgetable;

    public WidgetConfigAdapter(List<WidgetDto> ingredients, IngredientsConfigureActivity.Widgetable widgetable) {
        this.ingredients = ingredients;
        this.widgetable = widgetable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(ingredients.get(position).getName());
        holder.itemView.setOnClickListener
                (view -> widgetable.clickEvent(ingredients.get(holder.getAdapterPosition()).getId()));
    }

    @Override
    public int getItemCount() {
        if (ingredients != null) {
            return ingredients.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.widget_tv);
        }
    }
}
