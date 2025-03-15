package com.itheima.dao;

import com.itheima.entity.Recipe;

import java.util.List;

public interface RecipeDao {
    List<Recipe> findAll();


    Recipe findById(int recipe_id);
}
