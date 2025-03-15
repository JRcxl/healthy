package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.FoodSimilarityDao;
import com.itheima.dao.MemberFoodInteractionDao;
import com.itheima.entity.FoodSimilarity;
import com.itheima.entity.MemberFoodInteraction;
import com.itheima.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.itheima.recommend.FoodSimilarityCalculate.buildItemUserMatrix;
import static com.itheima.recommend.FoodSimilarityCalculate.calculateAllSimilarities;

@Service(interfaceClass = RecommendService.class)
@Transactional
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private MemberFoodInteractionDao memberFoodInteractionDao;

    @Autowired
    private FoodSimilarityDao foodSimilarityDao;

    public RecommendServiceImpl(MemberFoodInteractionDao memberFoodInteractionDao,FoodSimilarityDao foodSimilarityDao){
        this.memberFoodInteractionDao=memberFoodInteractionDao;
        this.foodSimilarityDao=foodSimilarityDao;  //这里采用get set的方式注入 以便在测试时防止bean实例化失败
    }

    @Override
    public List<Integer> recommendByItemCF(Integer userId, int topN) {
        if(memberFoodInteractionDao==null){
            System.out.println("bean注入为空");
            return null;
        }
        // 1. 获取用户历史行为
        List<MemberFoodInteraction> interactions = memberFoodInteractionDao.selectByMemberId(userId);
        //System.out.println("用户交互菜单数量："+interactions.size());
        if (interactions.isEmpty()) return Collections.emptyList();

        // 2. 收集所有交互过的菜品ID
        Set<Integer> interactedFoodIds = interactions.stream()
                .map(MemberFoodInteraction::getFoodId)
                .collect(Collectors.toSet());

        // 3. 计算推荐得分
        Map<Integer, Double> scores = new HashMap<>();
        for (MemberFoodInteraction interaction : interactions) {
            List<FoodSimilarity> similarItems = foodSimilarityDao.selectTopNSimilar(
                    interaction.getFoodId(), 50 // 取Top50相似菜品其未交互过的菜品
            );
            //System.out.println("关联食物："+similarItems.size());
            for (FoodSimilarity sim : similarItems) {
                Integer candidateFoodId = sim.getFood_id2();
                if (!interactedFoodIds.contains(candidateFoodId)) {
                    double score = interaction.getRating() * sim.getSimilarity();
                    scores.merge(candidateFoodId, score, Double::sum);
                }
            }
        }

        // 4. 按得分排序返回Top-N
        return scores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(topN)
                .collect(Collectors.toList());
    }


    // 1. 从数据库加载用户-菜品交互矩阵
    // 2. 计算菜品相似度0
    // 3. 批量写入food_similarity表
    @Override
    public void updateSimilarities() {
        // 1. 加载所有用户-菜品交互数据
        List<MemberFoodInteraction> allInteractions = memberFoodInteractionDao.findAllInteraction();

        // 2. 构建数据矩阵
        Map<Integer, Map<Integer, Double>> itemUserMatrix = buildItemUserMatrix(allInteractions);

        // 3. 计算相似度
        List<FoodSimilarity> similarities = calculateAllSimilarities(itemUserMatrix);

        // 4. 批量写入数据库（先清空旧数据）
        foodSimilarityDao.deleteAll();
        foodSimilarityDao.batchInsert(similarities);
    }


}
