package com.itheima.dao;

import com.itheima.pojo.ExamData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ExamDao {

    String findExamByids(@Param("memberid") int memberid, @Param("mealid") int mealid);




    List<ExamData> findAllExamDataByMember(int memberId);
}
