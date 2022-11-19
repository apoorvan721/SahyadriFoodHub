package com.example.sahyadrifoodhub.model;

public class ModelFoodList {

    String FoodName,FoodImg,FoodCategory,FoodId,FoodPrice;

    public ModelFoodList() {
    }

    public ModelFoodList(String foodName, String foodImg, String foodPrice, String foodCategory) {
        FoodName = foodName;
        FoodImg = foodImg;
        FoodPrice = foodPrice;
        FoodCategory = foodCategory;
    }

    public String getFoodName() {
        return FoodName;
    }

    public String getFoodImg() {
        return FoodImg;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public String getFoodCategory() {
        return FoodCategory;
    }

    public String getFoodId() {
        return FoodId;
    }
}
