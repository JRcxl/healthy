package com.itheima.utils;
import okhttp3.*;
import java.io.IOException;
import java.math.BigInteger;

import org.json.JSONObject;

public class SentenceEncoder {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "http://localhost:5000/simhash";

    // 获取单个SimHash
    static public BigInteger getSimHash(String medicalText) throws IOException {
        RequestBody body = RequestBody.create(
                String.format("{\"text\":\"%s\"}", medicalText),
                MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(API_URL).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            // 提取simhash字段并转换为BigInteger处理大数值
            String simhashStr = (String) jsonObject.get("simhash");
            return new BigInteger(simhashStr);
        }
    }

    // 比较两个医疗文本的相似度
    static public int compareMedicalTexts(String text1, String text2) throws IOException {
        BigInteger hash1 = getSimHash(text1);
        BigInteger hash2 = getSimHash(text2);
        return hammingDistance(hash1, hash2);
    }

    // 汉明距离计算优化
    private static int hammingDistance(BigInteger hash1, BigInteger hash2) {
        BigInteger xor = hash1.xor(hash2);
        return xor.bitCount(); // 直接计算二进制位差异
    }


}