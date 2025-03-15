package com.itheima.utils;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import com.alibaba.fastjson.JSON;


public class RocketMQUtil {
    public static SendResult sendTransactionMessage(
            DefaultMQProducer producer,
            String topic,
            String tag,
            Object messageBody
    ) throws Exception {
        Message msg = new Message(topic, tag, JSON.toJSONBytes(messageBody));
        return producer.send(msg);
    }
}