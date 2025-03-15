package com.itheima.Repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.URISyntaxException;

// 百度地图服务工具类
@Component
public class BaiduMapUtil {

    @Value("")
    private String ak;

    private static final String REVERSE_GEOCODE_URL =
            "http://api.map.baidu.com/reverse_geocoding/v3/";

    public JSONObject reverseGeocode(double lat, double lng) throws URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder(REVERSE_GEOCODE_URL);
        builder.addParameter("ak", ak)
                .addParameter("output", "json")
                .addParameter("coordtype", "wgs84ll")
                .addParameter("location", lat + "," + lng);

        try {
            HttpGet request = new HttpGet(builder.build());
            CloseableHttpResponse response = httpClient.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(result);
        } catch (Exception e) {
            throw new RuntimeException("百度地图API调用失败", e);
        }
    }
}
