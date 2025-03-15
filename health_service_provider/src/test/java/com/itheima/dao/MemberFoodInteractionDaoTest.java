package com.itheima.dao;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.FoodSimilarity;
import com.itheima.entity.MemberFoodInteraction;
import com.itheima.service.RecommendService;
import com.itheima.service.impl.DietServiceImpl;
import com.itheima.service.impl.RecommendServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml"})
public class MemberFoodInteractionDaoTest {


    @Autowired
    MemberFoodInteractionDao memberFoodInteractionDao;


    @Autowired
    FoodSimilarityDao foodSimilarityDao;







    @Test
    public void test1() {
        //List<MemberFoodInteraction> memberFoodInteractionList=memberFoodInteractionDao.selectByMemberId(93);
        //System.out.println(memberFoodInteractionList);
        //List<FoodSimilarity>foodSimilarityList=foodSimilarityDao.selectTopNSimilar(1,2);
        //System.out.println(foodSimilarityList);
        //System.out.println(foodSimilarityDao);
        //System.out.println("done");
    }

    @Test
    public void test2(){
        //GET\SET 注入
        RecommendServiceImpl recommendService=new RecommendServiceImpl(memberFoodInteractionDao,foodSimilarityDao);
        List<Integer>scores=recommendService.recommendByItemCF(93,5);
        System.out.println(scores);
    }

    @Test
    public void test3(){
        //foodSimilarityDao.deleteAll();
        // 验证：查询总数应为0
        //assertEquals(0, foodSimilarityDao.countAll());
        // 准备数据
        // 创建可变列表（支持 Java 7+）
        //List<FoodSimilarity> data = new ArrayList<>();
        //data.add(new FoodSimilarity(1, 2, 0.8));
        //data.add(new FoodSimilarity(1, 3, 0.7));
        //foodSimilarityDao.batchInsert(data);
        //System.out.println("total:"+foodSimilarityDao.countAll());

    }

    @Test
    public void test4(){
        RecommendServiceImpl recommendService=new RecommendServiceImpl(memberFoodInteractionDao,foodSimilarityDao);
        recommendService.updateSimilarities();
        System.out.println("done");
    }


}