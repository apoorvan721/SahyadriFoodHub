package com.example.sahyadrifoodhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahyadrifoodhub.R;
import com.example.sahyadrifoodhub.model.ModelFoodCount;
import com.example.sahyadrifoodhub.model.ModelFoodList;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<ModelFoodCount> foodList;

    public FoodAdapter(List<ModelFoodCount> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelFoodCount food = foodList.get(position);

        // Bind the data to the ViewHolder
        holder.foodNameTextView.setText("Item: "+food.getFoodName());
        holder.quantityTextView.setText("Quantity: "+String.valueOf(food.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView, quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
