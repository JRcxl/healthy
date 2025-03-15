package com.itheima.dao;

import com.itheima.entity.FoodSimilarity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FoodSimilarityDao {



    List<FoodSimilarity> selectTopNSimilar(
           @Param("foodId") Integer foodId,
           @Param("topN") Integer topN
    );


    void batchInsert(List<FoodSimilarity> similarities);

    void deleteAll();


    // 查询总记录数（用于验证）
    int countAll();
}
