package com.itheima.recommend;

import com.itheima.entity.FoodSimilarity;
import com.itheima.entity.MemberFoodInteraction;

import java.util.*;

public class FoodSimilarityCalculate {

    // 相似度计算配置参数
    private static final int TOP_SIMILAR_ITEMS = 50; // 每个菜品保留的相似菜品数
    private static final double CLICK_WEIGHT = 0.3; // 点击行为的权重
    private static final double FAVORITE_WEIGHT = 0.5; // 收藏行为的权重


    /**
     * 构建物品-用户矩阵（包含综合权重）
     */
    public static Map<Integer, Map<Integer, Double>> buildItemUserMatrix(List<MemberFoodInteraction> interactions) {
        // 临时存储用户维度的数据
        Map<Integer, Map<Integer, Double>> userItemMap = new HashMap<>();

        // 遍历原始数据，聚合用户行为
        for (MemberFoodInteraction interaction : interactions) {
            int userId = interaction.getMemberId();
            int itemId = interaction.getFoodId();

            // 计算综合权重（可根据业务需求调整公式）
            double weight = interaction.getRating()
                    + interaction.getClickCount() * CLICK_WEIGHT
                    + (interaction.getFavorite() ? FAVORITE_WEIGHT : 0);

            // 合并用户-物品数据
            userItemMap.computeIfAbsent(userId, k -> new HashMap<>())
                    .merge(itemId, weight, Double::sum);
        }

        // 转换为物品-用户矩阵
        Map<Integer, Map<Integer, Double>> itemUserMatrix = new HashMap<>();
        userItemMap.forEach((userId, itemWeights) -> {
            itemWeights.forEach((itemId, weight) -> {
                itemUserMatrix.computeIfAbsent(itemId, k -> new HashMap<>())
                        .put(userId, weight);
            });
        });

        return itemUserMatrix;
    }

    /**
     * 计算所有物品的相似度
     */
    public static List<FoodSimilarity> calculateAllSimilarities(
            Map<Integer, Map<Integer, Double>> itemUserMatrix) {

        List<Integer> itemIds = new ArrayList<>(itemUserMatrix.keySet());
        List<FoodSimilarity> allSimilarities = new ArrayList<>();

        // 遍历每个菜品计算相似度
        for (int i = 0; i < itemIds.size(); i++) {
            Integer itemA = itemIds.get(i);
            Map<Integer, Double> userWeightsA = itemUserMatrix.get(itemA);

            // 存储当前菜品的相似度计算结果
            PriorityQueue<FoodSimilarity> topQueue = new PriorityQueue<>(
                    Comparator.comparingDouble(FoodSimilarity::getSimilarity));

            // 与其他菜品计算相似度
            for (int j = i + 1; j < itemIds.size(); j++) {
                Integer itemB = itemIds.get(j);
                Map<Integer, Double> userWeightsB = itemUserMatrix.get(itemB);

                double similarity = cosineSimilarity(userWeightsA, userWeightsB);

                // 保留有效相似度
                if (similarity > 0) {
                    topQueue.add(new FoodSimilarity(itemA, itemB, similarity));
                    // 保持队列只保留TOP_N
                    if (topQueue.size() > TOP_SIMILAR_ITEMS) {
                        topQueue.poll();
                    }
                }
            }

            // 将当前菜品的TOP-N相似度存入结果集
            while (!topQueue.isEmpty()) {
                FoodSimilarity sim = topQueue.poll();
                allSimilarities.add(sim);
                // 同时添加反向关系保证对称性
                allSimilarities.add(new FoodSimilarity(sim.getFood_id2(),
                        sim.getFood_id1(),
                        sim.getSimilarity()));
            }
        }

        return allSimilarities;
    }

    /**
     * 余弦相似度计算
     */
    private static double cosineSimilarity(Map<Integer, Double> vectorA,
                                    Map<Integer, Double> vectorB) {
        // 计算共同用户的权重点积
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        // 优化遍历：只遍历较小的集合
        Map<Integer, Double> smallerVector = vectorA.size() < vectorB.size() ? vectorA : vectorB;
        Map<Integer, Double> largerVector = vectorA.size() < vectorB.size() ? vectorB : vectorA;

        for (Map.Entry<Integer, Double> entry : smallerVector.entrySet()) {
            Integer userId = entry.getKey();
            double a = entry.getValue();
            Double b = largerVector.get(userId);
            if (b != null) {
                dotProduct += a * b;
                normA += a * a;
                normB += b * b;
            }
        }
        // 处理无交集的情况
        if (dotProduct == 0) return 0.0;

        // 计算完整范式（处理未重叠的部分）
        if (normA == 0) {
            for (double a : vectorA.values()) {
                normA += a * a;
            }
        }
        if (normB == 0) {
            for (double b : vectorB.values()) {
                normB += b * b;
            }
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
