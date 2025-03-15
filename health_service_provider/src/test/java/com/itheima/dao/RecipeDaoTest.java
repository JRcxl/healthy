package com.itheima.dao;

import com.itheima.entity.Recipe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-dao.xml")
public class RecipeDaoTest {

    @Autowired RecipeDao recipeDao;

    //测试json与List<String> 映射
    @Test
    public void findAll() {
        List<Recipe> recipeList=recipeDao.findAll();
        /*for(Recipe r:recipeList){
            System.out.println(r.getRecipe_name());
        }*/
        if(recipeList!=null)
            System.out.println("Ok");

    }
}