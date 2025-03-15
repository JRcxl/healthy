package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.ExamDao;
import com.itheima.pojo.ExamData;
import com.itheima.service.BaiduAIService;
import com.itheima.service.ExamService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ExamService.class)
@Transactional
public class ExamServiceImpl implements ExamService {


    @Autowired
    ExamDao examDao;

    @Override
    public Map<String,Object> findAllExamData(int memberid) {

        List<ExamData> examDataList=examDao.findAllExamDataByMember(memberid);
        int index=0;
        Map<String,Object>Exams=new HashMap<>();
        for(ExamData examData:examDataList){

            Map<String, Object> data = new HashMap<>();
            data.put("username",examData.getUsername());
            data.put("mealname",examData.getMealname());
            data.put("sex",examData.getSex());
            data.put("mealid",examData.getMealid());
            data.put("orderDate",examData.getOrderDate());
            JSONObject jsonFromString = new JSONObject(examData.getResult_data());
            data.put("result_data",jsonFromString.toMap());
            Exams.put(String.valueOf(index),data);
            index++;
        }

        return Exams;

    }
}
