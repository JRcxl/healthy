package com.itheima.service.impl;

import com.itheima.config.DroolsConfig;
import com.itheima.dao.RecipeDao;
import com.itheima.entity.DietPlan;
import com.itheima.entity.MedicalReport;
import com.itheima.entity.MemberProfile;
import com.itheima.entity.Recipe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DroolsConfig.class)
public class DietServiceImplTest {

    @Autowired
    private KieContainer kieContainer;

    @Test
    public void testKieContainer() {
        assertNotNull(kieContainer); // 验证注入成功
    }

    @Test
    public void generateDietPlan() {
        KieSession kieSession = kieContainer.newKieSession();

        // 创建测试用户 (对花生过敏)
        MemberProfile user = new MemberProfile();

        user.setAllergies(Arrays.asList("花生")); // ✅ 替换为 Arrays.asList
        user.setDiseases(Arrays.asList("高血压", "糖尿病", "神经病")); // 空列表

        // 创建测试食谱
        Recipe riskyRecipe = createRecipe(
                "花生酱三明治",
                Arrays.asList("花生", "酱", "果酱"), // ✅ 替换为 Arrays.asList
                400, 300, 10.0
        );

        Recipe healthyRecipe = createRecipe(
                "蔬菜炒饭",
                Arrays.asList("蔬菜", "胡萝卜", "鸡蛋"), // ✅ 替换为 Arrays.asList
                300, 500, 12.0
        );

        // 创建饮食计划容器
        DietPlan dietPlan = new DietPlan();
        kieSession.setGlobal("dietPlan", dietPlan);

        // 插入事实对象到工作内存
        kieSession.insert(user);

        kieSession.insert(riskyRecipe);
        kieSession.insert(healthyRecipe);
        kieSession.insert(new MedicalReport(8.0,150.0));

        // 执行规则引擎
        int firedRules = kieSession.fireAllRules();

        kieSession.dispose();

        //System.out.println(dietPlan);

    }


    private Recipe createRecipe(String name, List<String> ingredients, int calories, int sodium, double protein) {
        Recipe recipe = new Recipe();
        recipe.setRecipe_name(name);
        recipe.setIngredients(ingredients);
        recipe.setCalories(calories);
        recipe.setSodium(sodium);
        recipe.setProtein(protein);
        return recipe;
    }
}