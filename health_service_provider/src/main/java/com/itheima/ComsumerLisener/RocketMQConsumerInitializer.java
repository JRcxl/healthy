package com.itheima.ComsumerLisener;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RocketMQConsumerInitializer implements ServletContextListener {

    private DefaultMQPushConsumer consumer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            consumer = new DefaultMQPushConsumer("reservation_consumer_group");
            consumer.setNamesrvAddr("localhost:9876");
            consumer.subscribe("RESERVATION_TOPIC", "*");
            consumer.registerMessageListener(new OrderConsumerListener());
            consumer.start();
        } catch (Exception e) {
            // 处理初始化异常
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
