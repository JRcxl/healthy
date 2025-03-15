package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.RecipeDao;
import com.itheima.entity.DietPlan;
import com.itheima.entity.MedicalReport;
import com.itheima.entity.MemberProfile;
import com.itheima.entity.Recipe;
import com.itheima.service.DietService;


import com.itheima.service.RecommendService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service(interfaceClass = DietService.class)
@Transactional
public class DietServiceImpl implements DietService {
    private static final int topN=5;

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    RecommendService recommendService;


    @Override
    public DietPlan generateDietPlan(MemberProfile memberProfile, MedicalReport report) {
        DietPlan plan = new DietPlan();
        List<Recipe> allRecipes = recipeDao.findAll();

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("dietPlan", plan);
        kieSession.insert(memberProfile);
        kieSession.insert(report);

        allRecipes.forEach(kieSession::insert);
        kieSession.fireAllRules();
        kieSession.dispose();

        List<Integer> recommendFoodIds = recommendService.recommendByItemCF(memberProfile.getMemberId(), topN);

        // 3. 将协同过滤推荐结果合并到DietPlan中
        if (recommendFoodIds != null && !recommendFoodIds.isEmpty()) {
            // 3.1 获取当前已推荐的食谱ID集合（用于去重）
            Set<Integer> existingRecipeIds = plan.getRecommendedMeals().stream()
                    .map(Recipe::getRecipe_id)
                    .collect(Collectors.toSet());

            // 3.2 将所有食谱转换为ID到Recipe的映射（便于快速查找）
            Map<Integer, Recipe> recipeMap = allRecipes.stream()
                    .collect(Collectors.toMap(Recipe::getRecipe_id, Function.identity()));

            // 3.3 遍历推荐ID，过滤并添加新食谱
            recommendFoodIds.stream()
                    .filter(recipeMap::containsKey)      // 过滤掉无效ID
                    .map(recipeMap::get)                 // 转换为Recipe对象
                    .filter(recipe -> !existingRecipeIds.contains(recipe.getRecipe_id()))  // 去重
                    .forEach(recipe -> plan.getRecommendedMeals().add(recipe));     // 添加到推荐列表
        }


        return plan;
    }
}
