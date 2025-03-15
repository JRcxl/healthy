package com.itheima.service;

import java.util.List;

public interface RecommendService {
    List<Integer> recommendByItemCF(Integer userId, int topN);

    // 1. 从数据库加载用户-菜品交互矩阵
    // 2. 计算菜品相似度（可调用Python脚本或Java数学库）
    // 3. 批量写入food_similarity表
    void updateSimilarities();
}
