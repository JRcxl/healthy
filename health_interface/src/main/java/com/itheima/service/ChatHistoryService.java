package com.itheima.service;

import com.itheima.pojo.Message;

import java.util.List;

public interface ChatHistoryService {


    // 保存新消息并保持最多5轮
    void saveMessage(String userId, Message message);

    // 获取完整对话历史
    List<Message> getHistory(String userId);
}
