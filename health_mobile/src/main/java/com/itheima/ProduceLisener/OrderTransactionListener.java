package com.itheima.ProduceLisener;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

public class OrderTransactionListener implements TransactionListener {
    @Reference
    private OrderService orderService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            Order order = JSON.parseObject(msg.getBody(), Order.class);
            orderService.createPendingOrder(order);
            //插入订单如下
            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (Exception e) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        String jsonStr = new String(msg.getBody(), StandardCharsets.UTF_8);
        // 2. 解析 JSON 到 Order 对象
        Order order = JSON.parseObject(jsonStr, Order.class);
        // 3. 获取 txId
        String txId = String.valueOf(order.getId());
        return orderService.isOrderExists(txId) ?
                LocalTransactionState.COMMIT_MESSAGE :
                LocalTransactionState.ROLLBACK_MESSAGE;
    }
}