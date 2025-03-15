package com.itheima.service;

import com.itheima.pojo.ExamData;

import java.util.Map;

public interface ExamService {
    Map<String,Object> findAllExamData(int memberid);
}
