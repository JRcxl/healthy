package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Message;
import com.itheima.service.BaiduAIService;
import com.itheima.service.ChatHistoryService;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service(interfaceClass = ChatHistoryService.class)
public class ChatHistoryServiceImpl implements ChatHistoryService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Redis键生成规则
    private String buildKey(String userId) {
        return String.format("user:%s:chat_history", userId);
    }

    @Override
    public void saveMessage(String userId, Message message) {
        // 保证线程安全的事务操作
        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            public Void execute(RedisOperations operations) {
                String key = buildKey(userId);

                // 1. 插入新消息到List头部
                //operations.opsForList().leftPush(key, message);
                //operations.opsForList().trim(key, 0, 4);

                operations.opsForList().rightPush(key, message);
                // 2. 修剪列表保留最新5条（尾部最后5条）
                operations.opsForList().trim(key, -5, -1);  // 使用负数索引

                // 3. 设置24小时过期时间
                operations.expire(key, 86400, TimeUnit.SECONDS);
                return null;
            }
        });
    }

    @Override
    public List<Message> getHistory(String userId) {
        String key = buildKey(userId);
        // 获取全部元素（0到-1）
        List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);

        // 类型转换
        return rawList.stream()
                .map(obj -> (Message) obj)
                .collect(Collectors.toList());
    }




}