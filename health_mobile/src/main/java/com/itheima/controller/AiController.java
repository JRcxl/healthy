package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.pojo.Message;
import com.itheima.pojo.RequestMessage;
import com.itheima.service.BaiduAIService;
import com.itheima.service.ChatHistoryService;
import com.itheima.utils.RedisCache;
import com.itheima.utils.SentenceEncoder;
import okhttp3.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
public class AiController {

    @Reference
    BaiduAIService baiduAIService;


    @Reference
    ChatHistoryService chatHistoryService;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();


    private static final String API_KEY = "SR9PLBfvhuTgyOkpcTPFVNqB";
    private static final String SECRET_KEY = "0sPAZtPlTRRkimpGM2RRES9B9PqVeg17";

    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
    private static final String API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-4.0-8k-preview?access_token=";

    @RequestMapping("/analyzeSentiment")
    public String analyzeSentiment(@RequestParam("memberid") int memberid, @RequestParam("mealid")int mealid) {

        String result = baiduAIService.analyzeSentiment(memberid,mealid);
        JSONObject jsonFromString = new JSONObject(result);
        System.out.println(jsonFromString.getString("result"));
        return jsonFromString.getString("result");
        //return jsonFromString.toMap();也可用map接收
    }

    @RequestMapping("/analyzestream")  //流式响应
    public void handleStreamRequest(@RequestParam("memberid")int memberid,@RequestParam("mealid")int mealid, HttpServletResponse response) throws IOException {
        //baiduAIService.analyzeStreamOut(memberid,mealid,response);
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");


        String reult_data=baiduAIService.findExamByids(memberid,mealid);
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
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-lite-8k?access_token=" + getAccessToken())
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
                String result = extractContentFromLine(line,"result");

                if (!result.isEmpty()) {
                    writer.write("data: " + result + "\n\n"); // 仅发送 result 内容
                    writer.flush();
                }
            }


        }
    }



    @PostMapping("/chatstream")  //对话模式
    public void handleChatStreamRequest(@org.springframework.web.bind.annotation.RequestBody RequestMessage requestMessage,
                                        HttpServletResponse response) throws IOException {

        MediaType mediaType = MediaType.parse("application/json");
        String jsonStr = JSON.toJSONString(requestMessage);

        List<Message>messageList=requestMessage.getMessages();
        if(messageList.size()>1)
            // 保存用户消息
            chatHistoryService.saveMessage(requestMessage.getUser_id(), messageList.get(messageList.size() - 1));


        String LastQuestion=messageList.get(messageList.size() - 1).getContent();

        // 1. 生成语义向量

        BigInteger simHash =  SentenceEncoder.getSimHash(LastQuestion);
        // 3. 尝试直接查询缓存
        String cachedAnswer = RedisCache.findSimilarAnswer(simHash, 6); // 允许汉明距离≤3

        if (cachedAnswer != null) {
            PrintWriter writer = response.getWriter();
            writer.write("data: " + cachedAnswer + "\n\n"); // 仅发送 result 内容
            writer.flush();
            chatHistoryService.saveMessage(requestMessage.getUser_id(),new Message("assistant",cachedAnswer) );

        }

        else{
            RequestBody body = RequestBody.create(jsonStr, mediaType);
            Request request = new Request.Builder()
                    .url("https://qianfan.baidubce.com/v2/chat/completions")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("appid", "")
                    .addHeader("Authorization", "Bearer bce-v3/ALTAK-11blgGv1kWW1v7nEj2kFI/060a0e65956ac74cf4c4661a0342ade300d08964")
                    .build();

            // 流式响应式接口
            try (ResponseBody responseBody = HTTP_CLIENT.newCall(request).execute().body()) {
                BufferedReader reader = new BufferedReader(responseBody.charStream());
                String line;
                PrintWriter writer = response.getWriter();
                StringBuilder airesult=new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    // 可选：按需处理或直接转发原始数据
                    String content = extractContentFromLine2(line);

                    airesult.append(content);
                    if (!content.isEmpty()) {
                        writer.write("data: " + content + "\n\n"); // 仅发送 result 内容
                        writer.flush();
                    }
                }
                // 保存AI回复
                chatHistoryService.saveMessage(requestMessage.getUser_id(),new Message("assistant",airesult.toString()) );
                RedisCache.cacheQuestion(LastQuestion, airesult.toString(), simHash);
            }
        }


    }


    @GetMapping("/history")
    public List<Message> getHistory(@RequestParam String userId) {
        return chatHistoryService.getHistory(userId);
    }



    private static String getAccessToken() throws IOException {
        // 创建 URL
        URL url = new URL(ACCESS_TOKEN_URL);
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

    private String extractContentFromLine(String line,String keystr) {
        try {
            // 1. 移除 SSE 前缀 "data: " 并去除首尾空白
            String jsonStr = line.replaceFirst("^data:\\s*", "").trim();

            // 2. 空行或非 JSON 数据直接跳过
            if (jsonStr.isEmpty() || !jsonStr.startsWith("{")) {
                return "";
            }

            // 3. 解析 JSON
            JSONObject json = new JSONObject(jsonStr);
            //return json.optString("result", "");
            return json.optString(keystr, "");
        } catch (JSONException e) {
            System.err.println("JSON 解析失败，原始数据: " + line);
            return "";
        }
    }

    private String extractContentFromLine2(String line) {
        try {
            // 1. 解析为JSONObject
            com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(line);

            // 2. 按路径逐层提取
            com.alibaba.fastjson.JSONObject message = json.getJSONArray("choices")
                    .getJSONObject(0)       // 第一个choice
                    .getJSONObject("message"); // message对象

            return message.getString("content"); // 直接返回content值

        } catch (Exception e) {
            System.err.println("解析失败: " + line);
            return ""; // 返回空字符串代替null
        }
    }



}
