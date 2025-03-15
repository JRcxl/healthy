package com.itheima.dao;

import com.itheima.entity.MemberFoodInteraction;


import java.util.List;

public interface MemberFoodInteractionDao {
    // 获取用户的历史行为数据
    List<MemberFoodInteraction> selectByMemberId(Integer memberId);

    List<MemberFoodInteraction> findAllInteraction();
}
