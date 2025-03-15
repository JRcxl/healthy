package com.itheima.ComsumerLisener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itheima.service.DietService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml",
        "classpath:spring-service.xml","classpath:spring-redis.xml"})
public class OrderConsumerListenerTest {



    //@Autowired
    //private DefaultMQProducer mqProducer;


    @Test
    public void consumeMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException, JsonProcessingException {

      /* ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 禁用时间戳
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));                // 指定日期格式

        Map<String, Object> mockObject = new HashMap<>();
        mockObject.put("memberId", 93);                 // Integer
        mockObject.put("orderDate", "2025-03-10");         // Date
        mockObject.put("orderType", "微信预约");          // String
        mockObject.put("orderStatus", "已到诊");          // String
        mockObject.put("setmealId", "16");                  // Integer
        mockObject.put("resource_type", "门诊预约");      // String
        mockObject.put("status", "有效");                 // String
        mockObject.put("retry_count", 0);                // int
        mockObject.put("create_time", "");       // Date
        mockObject.put("update_time",  "");       // Date



        String jsonStr =objectMapper.writeValueAsString(mockObject); // 使用 Jackson
        String orderDate = (String) mockObject.get("orderDate");
        Message msg = new Message("RESERVATION_TOPIC",
                jsonStr.getBytes(StandardCharsets.UTF_8));
        msg.setDelayTimeLevel(3); // RocketMQ预设延迟级别
        mqProducer.send(msg);*/
        System.out.println("done");


    }







}