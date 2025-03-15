package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.ExamData;
import com.itheima.service.ExamService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ExamDataController {

   @Reference
    ExamService examService;

    @RequestMapping("/findExamData")
    Map<String,Object> findAllData(int memberid){
        Map<String,Object>map=examService.findAllExamData(memberid);
        return map;
    }
}
