package com.example.sahyadrifoodhub.model;

public class ModelFoodCount {
        private String foodName;
        private String quantity;

        public ModelFoodCount() {
            // Required empty constructor for Firebase
        }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ModelFoodCount(String foodName, String quantity) {
            this.foodName = foodName;
            this.quantity = quantity;
        }
    }
