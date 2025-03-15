package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.ExamDao;
import com.itheima.pojo.OcrResponseDto;
import com.itheima.pojo.OcrResponseDto;
import com.itheima.service.BaiduAIService;
import okhttp3.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

@Service(interfaceClass = BaiduAIService.class)
@Transactional
public class BaiduAIServiceImpl implements BaiduAIService {

    private static final String API_KEY = "SR9PLBfvhuTgyOkpcTPFVNqB";
    private static final String SECRET_KEY = "0sPAZtPlTRRkimpGM2RRES9B9PqVeg17";


    private static final String OCR_API_KEY = "bQ8aR8Ow4ATjENZWwjhdPvmK";
    private static final String OCR_SECRET_KEY = "1qTh7lPsO9B04Lf7TbI9rzKZEAj45OIA";
    private static final String OCR_ACCESS_TOKEN_URL="https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + OCR_API_KEY + "&client_secret=" + OCR_SECRET_KEY;
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
    private static final String API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-4.0-8k-preview?access_token=";

    @Autowired
    ExamDao examDao;



    @Override
    public String analyzeSentiment(int memberid,int mealid) {
        String exam_data=examDao.findExamByids(memberid,mealid);    // bug:可能查到同一个人的多个同一个套餐 报错

        try {
            // 获取 access_token
            String accessToken = getAccessToken(ACCESS_TOKEN_URL);

            // 调用接口
            return  callBaiduAI(accessToken,exam_data);
        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();
        }
        return "failed";
    }




    private static String getAccessToken(String access_token_url) throws IOException {
        // 创建 URL
        URL url = new URL(access_token_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // 获取返回的响应
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        // 解析 JSON 响应
        String response = responseBuilder.toString();
        JSONObject jsonResponse = new JSONObject(response);

        // 返回 access_token
        return jsonResponse.getString("access_token");
    }

    // 调用百度接口
    private static String callBaiduAI(String accessToken,String exam_data) throws IOException {
        // 创建 URL
        URL url = new URL(API_URL + accessToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");


        String promptTemplate = "你好，AI。我有一组体检数据，帮助我分析它们并给出建议。";
        // 设置请求体内容
        JSONObject requestBody = new JSONObject();
        requestBody.put("messages", new Object[]{
                new JSONObject().put("role", "user").put("content", promptTemplate),  // 添加 promptTemplate
                new JSONObject().put("role", "user").put("content", exam_data)           // 添加用户数据
        });

        // 写入请求体
        OutputStream os = connection.getOutputStream();
        byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        // 获取返回的响应
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        // 输出返回的响应
        System.out.println(responseBuilder);
        return responseBuilder.toString();
    }


    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();



    @Override
    public void analyzeStreamOut(int memberid, int mealid,HttpServletResponse response) throws IOException{

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");


        String reult_data=examDao.findExamByids(memberid,mealid);
        String promptTemplate = "你好，AI。我有一组体检数据，帮助我分析它们并给出建议。";
        String prompt=promptTemplate+reult_data;
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        JSONArray messages = new JSONArray();
        messages.put(message);
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("messages", messages);
        requestBodyJson.put("stream", true);
        String jsonBody = requestBodyJson.toString();


        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-lite-8k?access_token=" + getAccessToken(ACCESS_TOKEN_URL))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        // 流式响应式接口
        try (ResponseBody responseBody = HTTP_CLIENT.newCall(request).execute().body()) {
            BufferedReader reader = new BufferedReader(responseBody.charStream());
            String line;
            PrintWriter writer = response.getWriter();
            while ((line = reader.readLine()) != null) {
                // 可选：按需处理或直接转发原始数据
                writer.write("data: " + line + "\n\n"); // SSE 格式
                writer.flush(); // 确保实时发送
            }


        }
    }



    @Override
    public void analyzeChatStreamOut()  {


    }

    @Override
    public String findExamByids(int memberid, int mealid) {
        return examDao.findExamByids(memberid,mealid);
    }


    @Override
    public OcrResponseDto CallOcrService(String text1, String text2) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        // 1. 安全构造请求体

        JSONObject textbody = new JSONObject();
        textbody.put("text_1", text1);
        textbody.put("text_2", text2);


        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                textbody.toString()
        );

        // 2. 构建请求
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v2/simnet?charset=UTF-8&access_token=" + getAccessToken(OCR_ACCESS_TOKEN_URL))
                .post(body)
                .build();

        // 3. 执行请求并解析响应
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败，状态码: " + response.code());
            }

            // 4. 直接反序列化为 DTO
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, OcrResponseDto.class);
        }

    }




}
