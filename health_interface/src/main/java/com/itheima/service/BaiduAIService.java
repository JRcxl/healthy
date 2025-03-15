package com.itheima.service;

import com.itheima.pojo.OcrResponseDto;
import com.itheima.pojo.OcrResponseDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public interface BaiduAIService {
    public String analyzeSentiment(int memberid,int mealid);



    void analyzeStreamOut(int memberid, int mealid, HttpServletResponse response) throws IOException;


    public void analyzeChatStreamOut();


    String findExamByids(int memberid, int mealid);


    OcrResponseDto CallOcrService(String text1, String test2) throws IOException;
}
