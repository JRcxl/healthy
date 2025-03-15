package com.itheima.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DietPlan implements Serializable {
    private List<Recipe> recommendedMeals = new ArrayList<>();;
    private List<String> warnings= new ArrayList<>();;

    public List<Recipe> getRecommendedMeals() {
        return this.recommendedMeals;
    }

    public void setRecommendedMeals(List<Recipe> recommendedMeals) {
        this.recommendedMeals = recommendedMeals;
    }

    public List<String> getWarnings() {
        return this.warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    @Override
    public String toString() {
        return "DietPlan{" +
                "recommendedMeals=" + recommendedMeals +
                ", warnings=" + warnings +
                '}';
    }
}
