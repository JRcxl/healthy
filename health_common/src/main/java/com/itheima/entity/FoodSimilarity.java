package com.itheima.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class FoodSimilarity implements Serializable {
    private Integer food_id1;
    private Integer food_id2;
    private double similarity;

    public FoodSimilarity(Integer foodId2, Integer foodId1, double similarity) {
        this.food_id1=foodId2;
        this.food_id2=foodId1;
        this.similarity=similarity;
    }

    public FoodSimilarity(){}

    public Integer getFood_id1() {
        return food_id1;
    }

    public void setFood_id1(Integer food_id1) {
        this.food_id1 = food_id1;
    }

    public Integer getFood_id2() {
        return food_id2;
    }

    public void setFood_id2(Integer food_id2) {
        this.food_id2 = food_id2;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return "FoodSimilarity{" +
                "food_id1=" + food_id1 +
                ", food_id2=" + food_id2 +
                ", similarity=" + similarity +
                '}';
    }
}
