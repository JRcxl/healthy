package com.itheima.entity;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    int recipe_id;

    String recipe_name;


    List<String> ingredients;

    int calories;


    double sodium;

    double protein;




    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipe_id=" + recipe_id +
                ", recipe_name='" + recipe_name + '\'' +
                ", ingredients=" + ingredients +
                ", calories=" + calories +
                ", sodium=" + sodium +
                ", protein=" + protein +
                '}';
    }
}
